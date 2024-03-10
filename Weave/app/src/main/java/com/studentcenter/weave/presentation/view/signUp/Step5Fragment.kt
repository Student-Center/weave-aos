package com.studentcenter.weave.presentation.view.signUp

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.databinding.FragmentSignUpStep5Binding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.univ.UnivUseCase
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.util.UnivAutoCompleteViewAdapter
import com.studentcenter.weave.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class Step5Fragment: BaseFragment<FragmentSignUpStep5Binding>(R.layout.fragment_sign_up_step_5) {
    private val viewModel: SignUpViewModel by activityViewModels()
    private var nameList = listOf<String>()

    private suspend fun setMajorList(){
        when(val res = UnivUseCase().getMajorList(viewModel.currentUnivId)){
            is Resource.Success -> {
                viewModel.majorList.addAll(res.data)
                nameList = res.data.map { it.name }
            }
            is Resource.Error -> {
                Log.e(TAG, res.message)
            }
            else -> {}
        }
    }

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        isRefresh.observe(this){
            if(it){
                isRefresh.value = false
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_sign_up, Step5Fragment())
                    .commit()
            }
        }

        viewModel.setNextBtn(false)
        viewModel.setIsEmpty(true)

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
                    .replace(R.id.fl_sign_up, StepEndFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

         binding.clStep5.setOnClickListener {
             binding.etAuto.clearFocus()
             val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
         }

        runBlocking(Dispatchers.IO){
            setMajorList() // init
        }

        viewModel.currentMajor.observe(this){
            if(it.isEmpty()) binding.etAuto.text = null
            viewModel.setCurrentMajorId()
        }

        val autoAdapter = UnivAutoCompleteViewAdapter(requireContext(), R.layout.item_dropdown_major, nameList)

        binding.etAuto.setDropDownBackgroundResource(R.drawable.shape_dropdown_layout)
        binding.etAuto.setAdapter(autoAdapter)

        binding.etAuto.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            viewModel.setMajor(selectedItem)
        }

        binding.etAuto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isNotEmpty()){
                    viewModel.setIsEmpty(false)
                } else {
                    viewModel.setIsEmpty(true)
                }
                viewModel.setNextBtn(false)

                if(binding.etAuto.adapter.count >= 3){
                    binding.etAuto.dropDownHeight = 56*4*3-48
                } else {
                    binding.etAuto.dropDownHeight = WRAP_CONTENT
                }
            }
        })

        binding.ibEditClear.setOnClickListener {
            if(!viewModel.inputIsEmpty.value!!){
                binding.etAuto.text = null
            }
        }

        binding.etAuto.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.setStep5FocusFlag(true)
            }
        }

        binding.etAuto.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(viewModel.nextBtn.value!!){
                    binding.ibNext.performClick()
                }
                handled = true
            }
            handled
        }
    }
}