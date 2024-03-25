package com.studentcenter.weave.presentation.view.my

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.studentcenter.weave.domain.usecase.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.user.UploadCallbackReq
import com.studentcenter.weave.databinding.BottomSheetDialogProfileBinding
import com.studentcenter.weave.domain.usecase.profile.UploadProfileImageUseCase
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileEditBottomSheetDialog(private val vm: MyViewModel): BottomSheetDialogFragment(){
    private val uploadUsecase = UploadProfileImageUseCase()

    companion object {
        private var instance: ProfileEditBottomSheetDialog? = null

        fun getInstance(vm: MyViewModel): ProfileEditBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: ProfileEditBottomSheetDialog(vm).also { instance = it }
            }
        }
    }

    private var _binding: BottomSheetDialogProfileBinding? = null
    val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog_profile, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        vm.setSaveBtn(false)


        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnPhoto.setOnClickListener {
            if((activity as MainActivity).checkCameraPermission()){
                pictureUri = createImageFile()
                getTakePicture.launch(pictureUri)
            } else {
                (activity as MainActivity).requestCameraPermission()
            }
        }

        binding.btnGallery.setOnClickListener {
            if((activity as MainActivity).checkGalleryPermission()){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
            } else {
                (activity as MainActivity).requestGalleryPermission()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data!!.data

            if (imageUri != null) {
                uploadImage(imageUri)
            }
        }
        dismiss()
    }

    private var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        result?.let { isSuccess ->
            if (isSuccess) {
                if (pictureUri != null) {
                    uploadImage(pictureUri!!)
                } else {
                    Toast.makeText(requireContext(), "사진을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "사진 찍기를 취소했습니다.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "사진 찍기에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
        dismiss()
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmm-ss", Locale.KOREA).format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }

    private fun absolutelyPath(path: Uri): String{
        val result: String
        val c: Cursor? = requireContext().contentResolver.query(path, null, null, null, null)
        result = if(c == null){
            path.path.toString()
        } else {
            c.moveToFirst()
            val index = c.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            c.getString(index)
        }
        c?.close()
        return result
    }

    private fun checkImageFileSize(filePath: String): Boolean {
        val file = File(filePath)
        if (!file.exists()) {
            return false
        }
        if (file.isDirectory) {
            return false
        }

        val fileSizeInBytes = file.length()
        val fileSizeInMB = fileSizeInBytes / (1024 * 1024)

        return fileSizeInMB > 4
    }

    private fun uploadImage(imageUri: Uri){
        try {
            val imagePath = absolutelyPath(imageUri)
            val file = File(imagePath)

            if(checkImageFileSize(imagePath)){
                val accessToken = runBlocking(Dispatchers.IO){
                    app.getUserDataStore().getLoginToken().first().accessToken
                }

                val fileExtension = getFileExtension(imagePath).uppercase()
                var uploadUrl: String? = null
                var imageId: String? = null

                runBlocking(Dispatchers.IO){
                    when(val res = uploadUsecase.getUploadUrl(accessToken, fileExtension)){
                        is Resource.Success -> {
                            uploadUrl = res.data.uploadUrl
                            imageId = res.data.imageId
                        }
                        is Resource.Error -> {
                            Log.e("UPLOAD", "업로드 URL 발급 실패")
                            null
                        }
                        else -> { null }
                    }
                }

                if(uploadUrl != null){
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

                    CoroutineScope(Dispatchers.IO).launch {
                        when(val res = uploadUsecase.uploadImage(uploadUrl!!, requestFile)){
                            is Resource.Success -> {
                                when(val res = uploadUsecase.uploadCallback(accessToken, UploadCallbackReq(imageId!!, extension = fileExtension))) {
                                    is Resource.Success -> {
                                        Log.i("UPLOAD", "업로드 성공")
                                        launch(Dispatchers.Main){
                                            vm.setProfileImg(imagePath)
                                        }
                                    }
                                    is Resource.Error -> {
                                        Log.e("UPLOAD", "업로드 실패: ${res.message}")
                                    }
                                    else -> {}
                                }
                            }
                            is Resource.Error -> {
                                Log.e("UPLOAD", "S3 업로드 실패: ${res.message}")
                            }
                            else -> {}
                        }
                    }
                }
            } else {
                Log.i("UPLOAD", "4MB 넘음")
                Toast.makeText(requireContext(), "4MB 미만 파일만 가능합니다", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "파일을 찾지 못했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(uri: String): String {
        val contentResolver = requireContext().contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri.parse(uri))) ?: "JPEG"
    }
}