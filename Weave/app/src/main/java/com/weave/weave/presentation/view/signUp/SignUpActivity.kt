package com.weave.weave.presentation.view.signUp

import androidx.activity.viewModels
import com.weave.weave.R
import com.weave.weave.databinding.ActivitySignUpBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weave.weave.data.repositoryImpl.TestRepositoryImpl
import com.weave.weave.domain.repository.TestRepository
import com.weave.weave.domain.usecase.TestUseCase

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