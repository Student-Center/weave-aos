package com.weave.weave.presentation.view.signUp

import androidx.activity.viewModels
import com.weave.weave.R
import com.weave.weave.databinding.ActivitySignUpBinding

import com.weave.weave.presentation.base.BaseActivity
import com.weave.weave.presentation.viewmodel.SignUpViewModel

class SignUpActivity: BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel by viewModels<SignUpViewModel>()


    override fun init() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_sign_up, Step1Fragment())
        transaction.commit()
    }
}