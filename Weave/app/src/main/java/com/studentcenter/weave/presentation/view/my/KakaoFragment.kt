package com.studentcenter.weave.presentation.view.my

import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentKakaoBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity

class KakaoFragment: BaseFragment<FragmentKakaoBinding>(R.layout.fragment_kakao) {
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
                Toast.makeText(requireContext(), "카카오 ID 저장!", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
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