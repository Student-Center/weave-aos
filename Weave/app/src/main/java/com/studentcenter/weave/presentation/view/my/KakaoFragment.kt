package com.studentcenter.weave.presentation.view.my

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.content.res.AppCompatResources
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentKakaoBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.MyViewModel

class KakaoFragment(private val vm: MyViewModel): BaseFragment<FragmentKakaoBinding>(R.layout.fragment_kakao) {
    private var btnState = false

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ibEditClear.setOnClickListener {
            binding.etKakaoId.text = null
        }

        binding.btnKakaoSave.setOnClickListener {
            if(btnState){
                CustomDialog.getInstance(CustomDialog.DialogType.KAKAO_ID, binding.etKakaoId.text.toString()).apply {
                    setOnOKClickedListener {
                        vm.setKakaoId(binding.etKakaoId.text.toString())
                        this@KakaoFragment.requireActivity().supportFragmentManager.popBackStack()
                    }
                }.show(requireActivity().supportFragmentManager, "kakaoId")
            }
        }

        binding.etKakaoId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(!p0.isNullOrEmpty()){
                    btnState = true
                    binding.btnKakaoSave.alpha = 1.0f
                    binding.ibEditClear.background = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_btn_edit_clear_activate)
                } else {
                    btnState = false
                    binding.btnKakaoSave.alpha = 0.5f
                    binding.ibEditClear.background = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_btn_edit_clear)
                }
            }
        })
    }
}