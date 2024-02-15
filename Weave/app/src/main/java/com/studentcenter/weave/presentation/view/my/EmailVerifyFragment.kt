package com.studentcenter.weave.presentation.view.my

import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.data.remote.dto.user.VerifyUnivEmailReq
import com.studentcenter.weave.databinding.FragmentEmailVerifyBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.profile.SendVerificationEmailUseCase
import com.studentcenter.weave.domain.usecase.profile.VerifyUnivEmailUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EmailVerifyFragment(private val email: String): BaseFragment<FragmentEmailVerifyBinding>(R.layout.fragment_email_verify) {
    private var cert: String = ""
    private lateinit var certNum: Array<EditText>
//    5 * 60 * 1000, 1000
    private val timer = object : CountDownTimer(10 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val minutes = millisUntilFinished / 1000 / 60
            val seconds = (millisUntilFinished / 1000) % 60
            binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
        }

        override fun onFinish() {
            binding.tvTimer.text = "00:00"
            Toast.makeText(requireContext(), "시간 만료", Toast.LENGTH_SHORT).show()
        }
    }

    override fun init() {
        timer.start()

        runBlocking {
            setCertNum()
        }

        binding.ibBack.setOnClickListener {
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

                        val dialog = CustomDialog.getInstance(CustomDialog.DialogType.EMAIL, null)
                        dialog.setOnOKClickedListener {
                            (requireActivity() as MainActivity).replaceFragment(EmailVerifyFragment(email))
                        }
                        dialog.show(requireActivity().supportFragmentManager, "email")
                    }
                }
                is Resource.Error -> {
                    Log.e("EMAIL", "인증번호 발송 실패 ${res.message}")
                    launch(Dispatchers.Main) {
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        Toast.makeText(requireContext(), "인증번호 발송 실패", Toast.LENGTH_SHORT).show()
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
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        val dialog = CustomDialog.getInstance(CustomDialog.DialogType.EMAIL_VERIFY, null)
                        dialog.setOnOKClickedListener {
                            (requireActivity() as MainActivity).replaceFragment(MyFragment())
                        }
                        dialog.show(requireActivity().supportFragmentManager, "verify")
                    }
                }
                is Resource.Error -> {
                    Log.e("EMAIL", res.message)
                    launch(Dispatchers.Main){
                        (requireActivity() as MainActivity).dismissLoadingDialog()
                        binding.tvFailure.visibility = View.VISIBLE
                    }
                }
                else -> {}
            }

            cert = ""
        }
    }

    private fun onClickButtonListener(): View.OnClickListener {
        return View.OnClickListener {
            if (certNum.size != 6) {
                Toast.makeText(requireContext(), "인증번호를 전부 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                for (i in 0..5) cert += certNum[i].text
                (requireActivity() as MainActivity).showLoadingDialog(requireContext())
                verifyUnivEmail()
            }
        }
    }

    private fun onDelKeyListener() {
        for (idx in 1..5) certNum[idx].setOnKeyListener { view: View, i: Int, keyEvent: KeyEvent ->
            if (i == KeyEvent.KEYCODE_DEL) {
                certNum[idx - 1].requestFocus()
            }
            false
        }
    }

    private fun setCertNumOnTextChangedListener() {
        for (idx in 0 until certNum.size - 1) certNum[idx].addTextChangedListener {
            if (certNum[idx].length() == 1) {
                certNum[idx + 1].requestFocus()
                certNum[idx + 1].text = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }
}