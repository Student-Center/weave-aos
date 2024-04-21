package com.studentcenter.weave.presentation.view.my

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.viewModels
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.databinding.FragmentEmailBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.profile.SendVerificationEmailUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EmailFragment(private val domainAddress: String): BaseFragment<FragmentEmailBinding>(R.layout.fragment_email) {
    private val timerViewModel: TimerViewModel by viewModels()

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)
        binding.tvDomain.text = getString(R.string.email_tv_domain, domainAddress)

        isRefresh.observe(this){
            if(it){
                (requireActivity() as MainActivity).replaceFragment(EmailFragment(domainAddress))
                isRefresh.value = false
            }
        }

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if(binding.etMail.text.isNotEmpty()){
                (requireActivity() as MainActivity).showLoadingDialog(requireContext())
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

    private fun sendEmail(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken
            val email = "${binding.etMail.text}@${domainAddress}".trim()

            when(val res = SendVerificationEmailUseCase().sendVerificationEmail(accessToken, SendVerificationEmailReq(email))){
                is Resource.Success -> {
                    Log.i("EMAIL", "인증번호 발송 성공")
                    launch(Dispatchers.Main){
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        timerViewModel.startTimer()

                        val dialog = CustomDialog.getInstance(CustomDialog.DialogType.EMAIL, null)
                        dialog.setOnOKClickedListener {
                            (requireActivity() as MainActivity).replaceFragmentWithStack(EmailVerifyFragment(email, timerViewModel))
                        }
                        dialog.show(requireActivity().supportFragmentManager, "email")
                    }
                }
                is Resource.Error -> {
                    Log.e("EMAIL", "인증번호 발송 실패 ${res.message}")
                    launch(Dispatchers.Main) {
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        CustomToast.createToast(app.applicationContext, res.message).show()
                    }
                }
                else -> {}
            }
        }
    }
}