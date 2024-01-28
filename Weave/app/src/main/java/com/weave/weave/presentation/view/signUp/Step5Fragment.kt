package com.weave.weave.presentation.view.signUp

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.core.GlobalApplication.Companion.loginState
import com.weave.weave.core.GlobalApplication.Companion.registerToken
import com.weave.weave.databinding.FragmentSignUpStep5Binding
import com.weave.weave.domain.usecase.LoginUseCase
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.presentation.util.CustomAutoCompleteViewAdapter
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class Step5Fragment: BaseFragment<FragmentSignUpStep5Binding>(R.layout.fragment_sign_up_step_5) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setNextBtn(false)
        viewModel.setIsEmpty(true)

        binding.appBar.ibAppBarSignUpBack.setOnClickListener {
            viewModel.setNextBtn(true)
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.SIGN_UP_CANCEL).show(requireActivity().supportFragmentManager, "cancelDialog")
        }

        binding.ibNext.setOnClickListener {
            if(registerUser()){
                loginState = true
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        }

         binding.clStep5.setOnClickListener {
             binding.etAuto.clearFocus()
             val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
         }

        val autoAdapter = CustomAutoCompleteViewAdapter(requireContext(), R.layout.item_dropdown_major, testData)

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
    }

    private fun registerUser(): Boolean{
        Log.i(TAG, "회원가입 요청")
        var flag = false // 성공여부 반환 하기 위한 값

        runBlocking(Dispatchers.IO) {
            val result = viewModel.getResult()
            if(result != null){
                when(val res = LoginUseCase().registerUser(registerToken!!, result)){
                    is Resource.Success -> {
                        Log.i(TAG, "회원가입 성공")
                        app.getUserDataStore().updatePreferencesAccessToken(res.data.accessToken!!)
                        app.getUserDataStore().updatePreferencesRefreshToken(res.data.refreshToken!!)
                        registerToken = null

                        flag = true
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "회원가입 실패: ${res.message}")
                    }
                    is Resource.Loading -> {}
                }
            }
        }

        return flag
    }

    val testData = listOf(
        "컴퓨터 공학",
        "전자 공학",
        "화학 공학",
        "경제학",
        "사회학",
        "의학",
        "간호학",
        "물리학",
        "수학",
        "영어 학문",
        "국어 학문",
        "음악학",
        "미술사",
        "역사학",
        "심리학",
        "경영학",
        "법학",
        "정치학",
        "환경 공학"
    )
}