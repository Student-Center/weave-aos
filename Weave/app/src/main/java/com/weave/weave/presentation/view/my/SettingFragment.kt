package com.weave.weave.presentation.view.my

import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentSettingBinding
import com.weave.weave.presentation.view.MainActivity

class SettingFragment: BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)
    }
}