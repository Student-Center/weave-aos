package com.weave.weave.presentation.view.signUp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.weave.weave.R
import com.weave.weave.databinding.DialogCustomBinding

class CustomDialog private constructor(private val dialogType: DialogType) : DialogFragment() {

    enum class DialogType {
        SIGN_UP_CANCEL,
        REGISTER,
        EMAIL,
        LOGOUT,
        UNLINK
    }

    companion object {
        private var instance: CustomDialog? = null

        fun getInstance(dialogType: DialogType): CustomDialog {
            return instance ?: synchronized(this) {
                instance ?: CustomDialog(dialogType).also { instance = it }
            }
        }
    }

    private var _binding: DialogCustomBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val widthInDp = 330
        val heightInDp = 195

        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        val heightInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, heightInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        dialog?.window?.setLayout(widthInPixels, heightInPixels)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_custom, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // 생성된 다이얼로그 종류에 따라 다른 작업 수행
        when (dialogType) {
            DialogType.SIGN_UP_CANCEL -> {
                setSignUpCancel()
            }
            DialogType.REGISTER -> {
                setRegister()
            }
            DialogType.EMAIL -> {}
            DialogType.LOGOUT -> {}
            DialogType.UNLINK -> {}
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
        Log.i("DIALOG", "destroy")
    }

    private fun setSignUpCancel(){
        binding.dialogTitle.text = getText(R.string.dialog_sign_up_cancel_title)
        binding.dialogComment.text = getText(R.string.dialog_sign_up_cancel_comment)
        binding.dialogBtnYes.text = getText(R.string.dialog_sign_up_cancel_yes)
        binding.dialogBtnNo.text = getText(R.string.dialog_sign_up_cancel_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            dismiss()
        }
    }

    private fun setRegister(){
        binding.dialogTitle.text = getText(R.string.dialog_register_title)
        binding.dialogComment.text = getText(R.string.dialog_register_comment)
        binding.dialogBtnYes.text = getText(R.string.dialog_register_yes)
        binding.dialogBtnNo.text = getText(R.string.dialog_register_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            dismiss()
        }
    }
}
