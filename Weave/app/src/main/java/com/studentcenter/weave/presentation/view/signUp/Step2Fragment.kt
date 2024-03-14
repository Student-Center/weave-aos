package com.studentcenter.weave.presentation.view.signUp

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentSignUpStep2Binding
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.viewmodel.SignUpViewModel
import java.util.Calendar

class Step2Fragment : BaseFragment<FragmentSignUpStep2Binding>(R.layout.fragment_sign_up_step_2) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.appBar.ibAppBarSignUpBack.setOnClickListener {
            viewModel.setNextBtn(true)
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.SIGN_UP_CANCEL, null).show(requireActivity().supportFragmentManager, "cancelDialog")
        }

        viewModel.setNextBtn(false)

        binding.etYear.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.setStep2FocusFlag(true)
            }
        }

        binding.etYear.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(viewModel.nextBtn.value!!){
                    binding.ibNext.performClick()
                }
                handled = true
            }
            handled
        }

        binding.etYear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                if (textLength == 4) {
                    val enteredYear = s.toString().toInt()

                    val standardYear = Calendar.getInstance().get(Calendar.YEAR)
                    val minValidYear = standardYear - 28
                    val maxValidYear = standardYear - 20 + 2

                    if (enteredYear in minValidYear..maxValidYear) {
                        viewModel.setYear(enteredYear)
                        viewModel.setNextBtn(true)
                    } else {
                        viewModel.setNextBtn(false)
                    }
                } else {
                    viewModel.setNextBtn(false)
                }
            }
        })

        binding.ibNext.setOnClickListener {
            if(viewModel.nextBtn.value!!){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_sign_up, Step3Fragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.clStep2.setOnClickListener {
            binding.etYear.clearFocus()
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}
