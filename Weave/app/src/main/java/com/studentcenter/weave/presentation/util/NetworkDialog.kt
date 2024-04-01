package com.studentcenter.weave.presentation.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.databinding.DialogNetworkBinding
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.NetworkManager.Companion.checkNetworkState

class NetworkDialog: DialogFragment() {
    private var _binding: DialogNetworkBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val widthInDp = 330

        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        dialog?.window?.setLayout(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_network, container, false)
        binding.lifecycleOwner = this

        binding.dialogBtnYes.setOnClickListener {
            if(checkNetworkState(requireContext())){
                isRefresh.value = true
                dismiss()
            } else {
                CustomToast.createToast(requireContext(), "네트워크 설정을 확인해주세요").show()
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}