package com.studentcenter.weave.presentation.view.chat

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.BottomSheetDialogReportBinding
import com.studentcenter.weave.presentation.util.CustomDialog

class ReportBottomSheetDialog(): BottomSheetDialogFragment(){

    companion object {
        private var instance: ReportBottomSheetDialog? = null

        fun getInstance(): ReportBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: ReportBottomSheetDialog().also { instance = it }
            }
        }
    }

    private var _binding: BottomSheetDialogReportBinding? = null
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
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog_report, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnReport.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.REPORT, null).apply {
                setOnOKClickedListener {
                    this@ReportBottomSheetDialog.dismiss()
                }
            }.show(requireActivity().supportFragmentManager, "report_dialog")
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
}