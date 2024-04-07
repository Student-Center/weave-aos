package com.studentcenter.weave.presentation.view.signUp

import androidx.activity.viewModels
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.networkState
import com.studentcenter.weave.databinding.ActivitySignUpBinding

import com.studentcenter.weave.presentation.base.BaseActivity
import com.studentcenter.weave.presentation.util.NetworkDialog
import com.studentcenter.weave.presentation.viewmodel.SignUpViewModel

class SignUpActivity: BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    private val viewModel by viewModels<SignUpViewModel>()
    private var networkDialog: NetworkDialog = NetworkDialog.getInstance()

    override fun init() {
        networkState.observe(this){
            if(!it){
                if(!networkDialog.isAdded){
                    networkDialog.show(supportFragmentManager, "network_dialog")
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_sign_up, Step1Fragment())
            .commit()
    }
}