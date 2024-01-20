package com.weave.presentation.view

import android.content.Intent
import com.weave.presentation.R
import com.weave.presentation.base.BaseActivity
import com.weave.presentation.databinding.ActivityMainBinding
import com.weave.presentation.view.signIn.SignInActivity

class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override val TAG: String get() = this.javaClass.simpleName

    override fun init() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}