package com.weave.presentation.view.signUp

import androidx.activity.viewModels
import com.weave.presentation.R
import com.weave.presentation.base.BaseActivity
import com.weave.presentation.databinding.ActivitySignUpBinding
import com.weave.presentation.viewmodel.SignUpViewModel

class SignUpActivity: BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    override val TAG: String get() = this.javaClass.simpleName
    private val viewModel: SignUpViewModel by viewModels()

    override fun init() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_sign_up, Step1Fragment())
        transaction.commit()
    }
}