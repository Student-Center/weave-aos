package com.weave.presentation.view.signUp

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentSignUpStep5Binding
import com.weave.weave.presentation.util.CustomAutoCompleteViewAdapter
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.view.signUp.CancelDialog
import com.weave.weave.presentation.viewmodel.SignUpViewModel

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
            CancelDialog.getInstance().show(requireActivity().supportFragmentManager, "cancelDialog")
        }

        binding.ibNext.setOnClickListener {
            if(viewModel.getResult()){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "잘못된 입력이 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

         binding.clStep5.setOnClickListener {
             binding.etAuto.clearFocus()
             val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
         }

        val autoAdapter = CustomAutoCompleteViewAdapter(requireContext(), R.layout.item_dropdown_major, autoData)

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
    }

    val autoData = listOf(
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