package com.weave.presentation.view.signUp

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.weave.presentation.R
import com.weave.presentation.base.BaseFragment
import com.weave.presentation.databinding.FragmentSignUpStep3Binding
import com.weave.presentation.viewmodel.SignUpViewModel

class Step3Fragment: BaseFragment<FragmentSignUpStep3Binding>(R.layout.fragment_sign_up_step_3) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setNextBtn(false)
        attachListener()

        binding.appBar.ibAppBarSignUpBack.setOnClickListener {
            viewModel.setNextBtn(true)
            viewModel.setLineValue(1, "")
            viewModel.setLineValue(2, "")
            viewModel.setLineValue(3, "")
            viewModel.setLineValue(4, "")
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {  }

        binding.ibNext.setOnClickListener {
//            if(viewModel.nextBtn.value!!){
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fl_sign_up, Step4Fragment())
//                    .addToBackStack(null)
//                    .commit()
//            }

            Log.d(TAG, "${viewModel.line1.value}${viewModel.line2.value}${viewModel.line3.value}${viewModel.line4.value}")
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