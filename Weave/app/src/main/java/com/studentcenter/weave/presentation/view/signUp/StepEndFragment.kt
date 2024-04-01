package com.studentcenter.weave.presentation.view.signUp

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication
import com.studentcenter.weave.core.GlobalApplication.Companion.registerToken
import com.studentcenter.weave.databinding.FragmentSignUpStepEndBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.auth.RegisterUserUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class StepEndFragment: BaseFragment<FragmentSignUpStepEndBinding>(R.layout.fragment_sign_up_step_end) {
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun init() {
        viewModel.setNextBtn(false)
        viewModel.setIsEmpty(true)

        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.SIGN_UP_CANCEL, null).show(requireActivity().supportFragmentManager, "cancelDialog")
        }
        binding.appBar.ibAppBarSignUpBack.setOnClickListener {
            viewModel.setMajor("")
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if(binding.cbTosAll.isChecked){
                if(registerUser()){
                    GlobalApplication.loginState = true
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    CustomToast.createToast(requireContext(), "재접속 후 다시 시도해주세요.").show()
                }
            } else {
                CustomToast.createToast(requireContext(), "필수 동의 항목을 체크 해주세요.").show()
            }
        }

        with(binding){
            cbTosAll.setOnClickListener { onClickListenerCb(cbTosAll.id) }
            cbTosAge.setOnClickListener { onClickListenerCb(cbTosAge.id) }
            cbTosPrivacy.setOnClickListener { onClickListenerCb(cbTosPrivacy.id) }
            cbTosTos.setOnClickListener { onClickListenerCb(cbTosTos.id) }

            tvTosAll.setOnClickListener { onClickListenerTv(tvTosAll.id) }
            tvTosAge.setOnClickListener { onClickListenerTv(tvTosAge.id) }
            tvTosPrivacy.setOnClickListener { onClickListenerTv(tvTosPrivacy.id) }
            tvTosTos.setOnClickListener { onClickListenerTv(tvTosTos.id) }
        }

        binding.tvLinkTos.setOnClickListener { openInternet(BuildConfig.TOS_URL) }
        binding.tvLinkPrivacy.setOnClickListener { openInternet(BuildConfig.PRIVACY_POLICY_URL) }
    }

    private fun openInternet(url: String){
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if(intent.resolveActivity(requireActivity().packageManager) != null){
            startActivity(intent)
        }
    }

    private fun onClickListenerCb(id: Int) {
        with(binding) {
            when (id) {
                cbTosAll.id -> {
                    if (cbTosAll.isChecked) {
                        cbTosAge.isChecked = true
                        cbTosPrivacy.isChecked = true
                        cbTosTos.isChecked = true
                    }else {
                        cbTosAge.isChecked = false
                        cbTosPrivacy.isChecked = false
                        cbTosTos.isChecked = false
                    }
                }
            }

            cbTosAll.isChecked = (cbTosAge.isChecked && cbTosTos.isChecked && cbTosPrivacy.isChecked)
            btnNext.alpha = if (cbTosAll.isChecked) 1f else 0.6f
        }
    }

    private fun onClickListenerTv(id: Int){
        with(binding){
            when(id){
                tvTosAll.id -> {
                    if (!cbTosAll.isChecked) {
                        cbTosAge.isChecked = true
                        cbTosPrivacy.isChecked = true
                        cbTosTos.isChecked = true
                    }else {
                        cbTosAge.isChecked = false
                        cbTosPrivacy.isChecked = false
                        cbTosTos.isChecked = false
                    }
                }

                tvTosAge.id -> { cbTosAge.isChecked = !cbTosAge.isChecked }
                tvTosPrivacy.id -> { cbTosPrivacy.isChecked = !cbTosPrivacy.isChecked }
                tvTosTos.id -> { cbTosTos.isChecked = !cbTosTos.isChecked }
            }

            cbTosAll.isChecked = (cbTosAge.isChecked && cbTosTos.isChecked && cbTosPrivacy.isChecked)
            btnNext.alpha = if (cbTosAll.isChecked) 1f else 0.6f
        }
    }

    private fun registerUser(): Boolean{
        Log.i(TAG, "회원가입 요청")

        if(registerToken == null) return false

        val flag: Boolean = runBlocking(Dispatchers.IO) {
            val result = viewModel.getResult()
            if(result != null){
                when(val res = RegisterUserUseCase().registerUser(registerToken!!, result)){
                    is Resource.Success -> {
                        Log.i(TAG, "회원가입 성공")
                        GlobalApplication.app.getUserDataStore().updatePreferencesAccessToken(res.data.accessToken)
                        GlobalApplication.app.getUserDataStore().updatePreferencesRefreshToken(res.data.refreshToken)

                        true
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "회원가입 실패: ${res.message}")
                        false
                    }
                    is Resource.Loading -> {
                        false
                    }
                }
            } else {
                false
            }
        }

        return flag
    }
}