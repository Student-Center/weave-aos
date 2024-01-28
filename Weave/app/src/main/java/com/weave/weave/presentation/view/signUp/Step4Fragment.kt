package com.weave.weave.presentation.view.signUp

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentSignUpStep4Binding
import com.weave.weave.domain.usecase.LoginUseCase
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.presentation.util.CustomAutoCompleteViewAdapter
import com.weave.weave.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class Step4Fragment: BaseFragment<FragmentSignUpStep4Binding>(R.layout.fragment_sign_up_step_4) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if(viewModel.currentUniv.value != ""){
            viewModel.setNextBtn(true)
        } else {
            viewModel.setNextBtn(false)
        }

        binding.appBar.ibAppBarSignUpBack.setOnClickListener {
            viewModel.setNextBtn(true)
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.SIGN_UP_CANCEL).show(requireActivity().supportFragmentManager, "cancelDialog")
        }

        binding.ibNext.setOnClickListener {
            if(viewModel.nextBtn.value!!){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_sign_up, Step5Fragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

         binding.clStep4.setOnClickListener {
             binding.etAuto.clearFocus()
             val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
         }

        var univData = listOf<String>()
        runBlocking(Dispatchers.IO){
            when(val res = LoginUseCase().getUnivList()){
                is Resource.Success -> {
                    univData = res.data.universities
                }
                is Resource.Error -> {
                    Log.e(TAG, res.message)
                }
                else -> {}
            }
        }

        val autoAdapter = CustomAutoCompleteViewAdapter(requireContext(), R.layout.item_dropdown_univ, univData)

        binding.etAuto.setDropDownBackgroundResource(R.drawable.shape_dropdown_layout)
        binding.etAuto.setAdapter(autoAdapter)

        binding.etAuto.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            viewModel.setUniv(selectedItem)
        }


        binding.etAuto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // clear btn 활성화 체크
                if(s.toString().isNotEmpty()){
                    viewModel.setIsEmpty(false)
                } else {
                    viewModel.setIsEmpty(true)
                }

                // BackStack으로 돌아왔을 때 체크
                if(s.toString() == viewModel.currentUniv.value){
                    viewModel.setNextBtn(true)
                } else {
                    viewModel.setNextBtn(false)
                }

                // 검색 결과 사이즈에 따른 DropDown 크기 지정
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
    }
}