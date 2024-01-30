package com.weave.weave.presentation.view.my

import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentMyPageBinding
import com.weave.weave.presentation.view.MainActivity

class MyFragment: BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)

        binding.ibSetting.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, SettingFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}