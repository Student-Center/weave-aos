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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.user.UploadCallbackReq
import com.studentcenter.weave.databinding.BottomSheetDialogProfileBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.profile.UploadProfileImageUseCase
import com.studentcenter.weave.presentation.custom.CustomToast
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

class ProfileEditBottomSheetDialog(private val vm: MyViewModel) : BottomSheetDialogFragment() {
    private val uploadUsecase = UploadProfileImageUseCase()
    private var pictureUri: Uri? = null

    companion object {
        private var instance: ProfileEditBottomSheetDialog? = null

        fun getInstance(vm: MyViewModel): ProfileEditBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: ProfileEditBottomSheetDialog(vm).also { instance = it }
            }
        }
    }

    private var _binding: BottomSheetDialogProfileBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog_profile, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        vm.setSaveBtn(false)

        setupButtonListeners()

        return binding.root
    }

    private fun setupButtonListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnPhoto.setOnClickListener { handlePhotoButtonClicked() }
        binding.btnGallery.setOnClickListener { handleGalleryButtonClicked() }
    }

    private fun handlePhotoButtonClicked() {
        val mainActivity = activity as? MainActivity ?: return

        if(mainActivity.checkCameraPermission()) takePicture()
        else mainActivity.requestCameraPermission()
    }

    private fun handleGalleryButtonClicked() {
        val mainActivity = activity as? MainActivity ?: return

        if (mainActivity.checkGalleryPermission()) launchGallery()
        else mainActivity.requestGalleryPermission()
    }


    private fun takePicture() {
        pictureUri = createImageFile()
        getTakePicture.launch(pictureUri)
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
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
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let { uploadImage(it) }
        }
        dismiss()
    }

    // Activity result launcher for camera
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        result?.let { isSuccess ->
            if (isSuccess) {
                pictureUri?.let { uploadImage(it) }
            } else {
                CustomToast.createToast(requireContext(), "Cancelled taking picture").show()
            }
        } ?: run {
            CustomToast.createToast(requireContext(), "Failed to take picture").show()
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

    private fun uploadImage(imageUri: Uri) {
        try {
            val imagePath = absolutelyPath(imageUri)
            val file = File(imagePath)

            if (isFileSizeUnder4MB(imagePath)) {
                val accessToken = runBlocking(Dispatchers.IO) { app.getUserDataStore().getLoginToken().first().accessToken }

                val fileExtension = getFileExtension(imagePath).uppercase()
                var uploadUrl: String? = null
                var imageId: String? = null

                runBlocking(Dispatchers.IO) {
                    when (val res = uploadUsecase.getUploadUrl(accessToken, fileExtension)) {
                        is Resource.Success -> {
                            uploadUrl = res.data.uploadUrl
                            imageId = res.data.imageId
                        }
                        is Resource.Error -> {
                            Log.e("UPLOAD", "Failed to get upload URL")
                            null
                        }
                        else -> null
                    }
                }

                if (uploadUrl != null) {
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

                    CoroutineScope(Dispatchers.IO).launch {
                        when (val res = uploadUsecase.uploadImage(uploadUrl!!, requestFile)) {
                            is Resource.Success -> {
                                when (val res = uploadUsecase.uploadCallback(accessToken, UploadCallbackReq(imageId!!, extension = fileExtension))) {
                                    is Resource.Success -> {
                                        Log.i("UPLOAD", "Upload successful")
                                        launch(Dispatchers.Main) { vm.setProfileImg(imagePath) }
                                    }
                                    is Resource.Error -> {
                                        Log.e("UPLOAD", "Error Callback: ${res.message}")
                                        launch(Dispatchers.Main) { CustomToast.createToast(requireContext(), "Failed to upload image").show() }
                                    }
                                    else -> {}
                                }
                            }
                            is Resource.Error -> {
                                Log.e("UPLOAD", "Failed to upload image to S3: ${res.message}")
                                launch(Dispatchers.Main) { CustomToast.createToast(requireContext(), "Failed to upload image").show() }
                            }
                            else -> {}
                        }
                    }
                }
            } else {
                Log.i("UPLOAD", "Size exceeded 4MB")
                CustomToast.createToast(requireContext(), "4MB 이하의 파일만 가능합니다.").show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CustomToast.createToast(requireContext(), "Failed to find file").show()
        }
    }

    private fun absolutelyPath(path: Uri): String {
        val result: String
        val c: Cursor? = requireContext().contentResolver.query(path, null, null, null, null)
        result = if (c == null) {
            path.path.toString()
        } else {
            c.moveToFirst()
            val index = c.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            c.getString(index)
        }
        c?.close()
        return result
    }

    private fun isFileSizeUnder4MB(uri: String): Boolean {
        return try {
            val fileSizeInBytes = getFileSizeInBytes(uri.toUri())
            val fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0)
            fileSizeInMB <= 4.0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getFileSizeInBytes(uri: Uri): Long {
        val file = File(uri.path.toString())
        return file.length()
    }

    private fun getFileExtension(uri: String): String {
        val contentResolver = requireContext().contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri.parse(uri))) ?: "JPEG"
    }
}