package com.studentcenter.weave.presentation.view.signUp

import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentSignUpStep3Binding
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.viewmodel.SignUpViewModel

class Step3Fragment: BaseFragment<FragmentSignUpStep3Binding>(R.layout.fragment_sign_up_step_3) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if(viewModel.line1.value != "" && viewModel.line2.value != "" && viewModel.line3.value != "" && viewModel.line4.value != ""){
            viewModel.setNextBtn(true)
        } else {
            viewModel.setNextBtn(false)
        }
        attachListener()

        binding.appBar.ibAppBarSignUpBack.setOnClickListener {
            viewModel.setNextBtn(true)
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.SIGN_UP_CANCEL, null).show(requireActivity().supportFragmentManager, "cancelDialog")
        }

        binding.ibNext.setOnClickListener {
            if(viewModel.nextBtn.value!!){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_sign_up, Step4Fragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun handleTextViewClick(textView: TextView) {
        when (textView.text) {
            "E", "e", "i", "I" -> {
                viewModel.setLineValue(1, textView.text.toString())
            }
            "N", "n", "s", "S" -> {
                viewModel.setLineValue(2, textView.text.toString())

            }
            "F", "f", "t", "T" -> {
                viewModel.setLineValue(3, textView.text.toString())

            }
            "P", "p", "j", "J" -> {
                viewModel.setLineValue(4, textView.text.toString())
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
}