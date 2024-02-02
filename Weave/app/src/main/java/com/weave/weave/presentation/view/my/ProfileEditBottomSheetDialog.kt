package com.weave.weave.presentation.view.my

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weave.weave.R
import com.weave.weave.databinding.BottomSheetDialogProfileBinding
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.viewmodel.MyViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileEditBottomSheetDialog(private val vm: MyViewModel): BottomSheetDialogFragment(){

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
                try {
                    vm.setProfileImg(imageUri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        dismiss()
    }

    private var pictureUri: Uri? = null
    private val getTakePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it) {
            pictureUri.let { vm.setProfileImg(pictureUri.toString()) }
        }
        dismiss()
    }

    private fun createImageFile(): Uri? {
        val now = SimpleDateFormat("yyMMdd_HHmm-ss", Locale.KOREA)
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }

}