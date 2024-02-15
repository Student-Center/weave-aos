package com.studentcenter.weave.presentation.view.my

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.databinding.FragmentEmailBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.profile.SendVerificationEmailUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EmailFragment: BaseFragment<FragmentEmailBinding>(R.layout.fragment_email) {
    var domain = "mju.ac.kr"

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)
        setDomain()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            // 요청 성공 다이얼로그 보이기
            if(binding.etMail.text.isNotEmpty()){
                sendEmail()
            }
        }

        binding.etMail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if(binding.etMail.text.isNotEmpty()){
                    binding.btnNext.alpha = 1f
                } else {
                    binding.btnNext.alpha = 0.7f
                }
            }

        })
    }

    private fun setDomain(){
        binding.tvDomain.text = getString(R.string.email_tv_domain, "mju.ac.kr")
    }

    private fun sendEmail(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken
            val email = "${binding.etMail.text}@${domain}"

            when(val res = SendVerificationEmailUseCase().sendVerificationEmail(accessToken, SendVerificationEmailReq(email))){
                is Resource.Success -> {
                    Log.i("EMAIL", "인증번호 발송 성공")
                    launch(Dispatchers.Main){
                        Toast.makeText(requireContext(), "인증번호 발송 성공", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    Log.e("EMAIL", "인증번호 발송 실패 ${res.message}")
                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {}
            }
        }
    }
}