package com.weave.presentation.view.signUp

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.weave.presentation.R
import com.weave.presentation.base.BaseFragment
import com.weave.presentation.databinding.FragmentSignUpStep1Binding
import com.weave.presentation.viewmodel.SignUpViewModel

class Step1Fragment: BaseFragment<FragmentSignUpStep1Binding>(R.layout.fragment_sign_up_step_1) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.appBar.ibAppBarSignUpBack.isVisible = false
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {  }

        binding.ivBoyChecked.setOnClickListener{
            viewModel.setBoyChecked(!viewModel.boyChecked.value!!)
        }

        binding.ivGirlChecked.setOnClickListener {
            viewModel.setGirlChecked(!viewModel.girlChecked.value!!)
        }

        binding.ibNext.setOnClickListener {
            if(viewModel.nextBtn.value!!){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_sign_up, Step2Fragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}