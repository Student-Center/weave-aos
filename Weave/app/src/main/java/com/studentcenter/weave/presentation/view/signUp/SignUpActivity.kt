package com.studentcenter.weave.presentation.view.signUp

import androidx.activity.viewModels
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.ActivitySignUpBinding

import com.studentcenter.weave.presentation.base.BaseActivity
import com.studentcenter.weave.presentation.viewmodel.SignUpViewModel

class SignUpActivity: BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel by viewModels<SignUpViewModel>()


    override fun init() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_sign_up, Step1Fragment())
        transaction.commit()
    }
}