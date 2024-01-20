package com.weave.presentation.view.signUp

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import com.weave.presentation.R
import com.weave.presentation.base.BaseFragment
import com.weave.presentation.databinding.FragmentSignUpStep2Binding
import com.weave.presentation.viewmodel.SignUpViewModel
import java.util.Calendar

class Step2Fragment : BaseFragment<FragmentSignUpStep2Binding>(R.layout.fragment_sign_up_step_2) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.appBar.ibAppBarSignUpBack.setOnClickListener {
            viewModel.setNextBtn(true)
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {  }

        viewModel.setNextBtn(false)

        binding.etYear.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.setFocusFlag(true)
            }
        }

        binding.etYear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                if (textLength == 4) {
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                    val enteredYear = s.toString().toInt()

                    val minValidYear = currentYear - 50
                    val maxValidYear = currentYear - 20 + 1

                    if (enteredYear in minValidYear..maxValidYear) {
                        viewModel.setNextBtn(true)
                    } else {
                        viewModel.setNextBtn(false)
                    }
                } else {
                    viewModel.setNextBtn(false)
                }
            }
        })
    }

}
