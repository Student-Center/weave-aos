package com.studentcenter.weave.presentation.view.my

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.data.remote.dto.user.VerifyUnivEmailReq
import com.studentcenter.weave.databinding.FragmentEmailVerifyBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.profile.SendVerificationEmailUseCase
import com.studentcenter.weave.domain.usecase.profile.VerifyUnivEmailUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EmailVerifyFragment(private val email: String, private val vm: TimerViewModel): BaseFragment<FragmentEmailVerifyBinding>(R.layout.fragment_email_verify) {
    private var cert: String = ""
    private lateinit var certNum: Array<EditText>

    override fun init() {
        isRefresh.observe(this){
            if(it){
                isRefresh.value = false
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        vm.timeText.observe(this){
            binding.tvTimer.text = it
        }

        vm.isFinish.observe(this){
            if(it){
                binding.tvTimer.text = "00:00"
                CustomDialog.getInstance(CustomDialog.DialogType.EMAIL_TIME_OVER, null).apply {
                    setOnOKClickedListener {
                        vm.setIsFinish()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }.show(requireActivity().supportFragmentManager, "time_over")
            }
        }

        runBlocking {
            setCertNum()
        }

        binding.ibBack.setOnClickListener {
            vm.cancelTimer()
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvAgain.setOnClickListener {
            (requireActivity() as MainActivity).showLoadingDialog(requireContext())
            againSendEmail()
        }

        setCertNumOnTextChangedListener()
        onDelKeyListener()
        binding.btnNext.setOnClickListener(onClickButtonListener())
    }

    private fun setCertNum(){
        certNum = arrayOf(
            binding.signCertNum1,
            binding.signCertNum2,
            binding.signCertNum3,
            binding.signCertNum4,
            binding.signCertNum5,
            binding.signCertNum6
        )
    }

    private fun againSendEmail(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = SendVerificationEmailUseCase().sendVerificationEmail(accessToken, SendVerificationEmailReq(email))){
                is Resource.Success -> {
                    Log.i("EMAIL", "인증번호 발송 성공")
                    launch(Dispatchers.Main){
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        vm.resetTimer()

                        val dialog = CustomDialog.getInstance(CustomDialog.DialogType.EMAIL, null)
                        dialog.setOnOKClickedListener {}
                        dialog.show(requireActivity().supportFragmentManager, "email")

                    }
                }
                is Resource.Error -> {
                    Log.e("EMAIL", "인증번호 발송 실패 ${res.message}")
                    launch(Dispatchers.Main) {
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        CustomToast.createToast(requireContext(), "인증번호 발송 실패").show()
                    }
                }
                else -> {}
            }
        }
    }

    private fun verifyUnivEmail(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = VerifyUnivEmailUseCase().verifyUnivEmail(accessToken, VerifyUnivEmailReq(email, cert))){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        myInfo!!.isUniversityEmailVerified = true

                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        CustomDialog.getInstance(CustomDialog.DialogType.EMAIL_VERIFY, null).apply {
                            setOnOKClickedListener {
                                if(it == "yes"){
                                    (requireActivity() as MainActivity).naviItemChange(3)
                                } else {
                                    (requireActivity() as MainActivity).naviItemChange(4)
                                }
                            }
                        }.show(requireActivity().supportFragmentManager, "verify")
                    }
                }
                is Resource.Error -> {
                    Log.e("EMAIL", res.message)
                    launch(Dispatchers.Main){
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        binding.tvEmailComment.visibility = View.VISIBLE
                        binding.tvEmailComment.text = getString(R.string.email_verify_failure)
                        binding.tvEmailComment.setTextColor(requireContext().getColor(R.color.red))
                    }
                }
                else -> {}
            }

            cert = ""
        }
    }

    private fun onClickButtonListener(): View.OnClickListener {
        return View.OnClickListener {
            if (isTextViewTextNull()) {
                CustomToast.createToast(requireContext(), "인증번호를 전부 입력해주세요.").show()
            }
            else {
                for (i in 0..5) cert += certNum[i].text
                (requireActivity() as MainActivity).showLoadingDialog(requireContext())
                verifyUnivEmail()
            }
        }
    }

    private fun onDelKeyListener() {
        for (idx in 1 until certNum.size) {
            certNum[idx].setOnKeyListener { view: View, i: Int, keyEvent: KeyEvent ->
                if (i == KeyEvent.KEYCODE_DEL && certNum[idx].text.isEmpty()) {
                    certNum[idx - 1].requestFocus()
                    return@setOnKeyListener true
                }
                false
            }
        }
    }

    private fun setCertNumOnTextChangedListener() {
        for (idx in 0 until certNum.size - 1) {
            certNum[idx].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        certNum[idx + 1].requestFocus()
                        certNum[idx + 1].text = null
                    }

                    if(isTextViewTextNull()){
                        binding.tvEmailComment.visibility = View.VISIBLE
                        binding.tvEmailComment.text = getString(R.string.email_spam)
                        binding.tvEmailComment.setTextColor(requireContext().getColor(R.color.grey_80))
                    } else {
                        binding.tvEmailComment.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun isTextViewTextNull(): Boolean {
        for (textView in certNum) {
            if (textView.text.isNullOrEmpty()) {
                return true
            }
        }
        return false
    }

}