package com.studentcenter.weave.presentation.view.signUp

import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentSignUpStep1Binding
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.viewmodel.SignUpViewModel

class Step1Fragment: BaseFragment<FragmentSignUpStep1Binding>(R.layout.fragment_sign_up_step_1) {
    private val viewModel: SignUpViewModel by activityViewModels()
    private var backPressedTime: Long = 0L

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                requireActivity().finishAffinity()
            } else {
                backPressedTime = System.currentTimeMillis()
                CustomToast.createToast(requireContext(), "한 번 더 누르면 종료됩니다.").show()
            }
        }
    }

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.appBar.ibAppBarSignUpBack.isVisible = false
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.SIGN_UP_CANCEL, null).show(requireActivity().supportFragmentManager, "cancelDialog")
        }

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