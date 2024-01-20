package com.weave.presentation.view

import com.weave.presentation.R
import com.weave.presentation.base.BaseActivity
import com.weave.presentation.databinding.ActivityMainBinding

class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override val TAG: String get() = this.javaClass.simpleName

    override fun init() {

    }
}