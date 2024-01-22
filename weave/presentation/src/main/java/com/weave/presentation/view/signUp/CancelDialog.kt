package com.weave.presentation.view.signUp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.weave.presentation.R
import com.weave.presentation.databinding.DialogSignUpCancelBinding

class CancelDialog private constructor() : DialogFragment() {
    companion object {
        private var instance: CancelDialog? = null

        fun getInstance(): CancelDialog {
            return instance ?: synchronized(this) {
                instance ?: CancelDialog().also { instance = it }
            }
        }
    }

    private var _binding: DialogSignUpCancelBinding? = null
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_sign_up_cancel, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.dialogBtnNo.setOnClickListener {
            // 앱 종료?
        }

        binding.dialogBtnYes.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
