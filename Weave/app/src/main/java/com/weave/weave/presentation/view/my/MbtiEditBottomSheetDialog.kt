package com.weave.weave.presentation.view.my

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weave.weave.R
import com.weave.weave.databinding.BottomSheetDialogMbtiBinding
import com.weave.weave.presentation.viewmodel.MyViewModel

class MbtiEditBottomSheetDialog(private val vm: MyViewModel): BottomSheetDialogFragment(){

    companion object {
        private var instance: MbtiEditBottomSheetDialog? = null

        fun getInstance(vm: MyViewModel): MbtiEditBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: MbtiEditBottomSheetDialog(vm).also { instance = it }
            }
        }
    }

    private var _binding: BottomSheetDialogMbtiBinding? = null
    private val binding get() = _binding!!

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog_mbti, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm

        if(vm.line1.value != "" && vm.line2.value != "" && vm.line3.value != "" && vm.line4.value != ""){
            vm.setSaveBtn(true)
        } else {
            vm.setSaveBtn(false)
        }

        attachListener()

        binding.ibClose.setOnClickListener {
            dismiss()
        }

        binding.ibSave.setOnClickListener {
            if(vm.saveBtn.value!!){
                println("MBTI: ${vm.line1.value}${vm.line2.value}${vm.line3.value}${vm.line4.value}")
                vm.setMbti()
                vm.setSaveBtn(false)
                dismiss()
            }
        }




        return binding.root
    }

    private fun handleTextViewClick(textView: TextView) {
        Log.i("MBTI", textView.text.toString())
        when (textView.text) {
            "E", "e", "i", "I" -> {
                vm.setLineValue(1, textView.text.toString())
            }
            "N", "n", "s", "S" -> {
                vm.setLineValue(2, textView.text.toString())

            }
            "F", "f", "t", "T" -> {
                vm.setLineValue(3, textView.text.toString())

            }
            "P", "p", "j", "J" -> {
                vm.setLineValue(4, textView.text.toString())
            }
        }
    }

    private fun attachListener(){
        val textViewList: List<TextView> = listOf(
            binding.tvMbtiBigE,
            binding.tvMbtiBigI,
            binding.tvMbtiBigF,
            binding.tvMbtiBigJ,
            binding.tvMbtiBigN,
            binding.tvMbtiBigP,
            binding.tvMbtiBigS,
            binding.tvMbtiBigT,

            binding.tvMbtiSmallE,
            binding.tvMbtiSmallI,
            binding.tvMbtiSmallF,
            binding.tvMbtiSmallJ,
            binding.tvMbtiSmallN,
            binding.tvMbtiSmallP,
            binding.tvMbtiSmallS,
            binding.tvMbtiSmallT,

            )

        val textViewListener = View.OnClickListener { view ->
            if (view is TextView) {
                handleTextViewClick(view)
            }
        }

        textViewList.forEach { textView ->
            textView.setOnClickListener(textViewListener)
        }
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