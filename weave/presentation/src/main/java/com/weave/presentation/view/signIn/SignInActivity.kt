package com.weave.presentation.view.signIn

import android.content.Intent
import android.widget.Toast
import com.weave.presentation.R
import com.weave.presentation.base.BaseActivity
import com.weave.presentation.databinding.ActivitySignInBinding
import com.weave.presentation.view.signUp.SignUpActivity

class SignInActivity: BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {
    override val TAG: String get() = this.javaClass.simpleName

    override fun init() {
        binding.btnKakaoLogin.setOnClickListener {
            Toast.makeText(this, "kakao", Toast.LENGTH_SHORT).show()
        }

        binding.btnAppleLogin.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}