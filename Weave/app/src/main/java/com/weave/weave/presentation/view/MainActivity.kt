package com.weave.weave.presentation.view


import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.registerToken
import com.weave.weave.databinding.ActivityMainBinding
import com.weave.weave.presentation.base.BaseActivity
import com.weave.weave.presentation.view.signUp.CustomDialog

class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun init() {
        if(registerToken != null){
            CustomDialog.getInstance(CustomDialog.DialogType.REGISTER).show(supportFragmentManager, "registerDialog")
            registerToken = null
        }
    }
}