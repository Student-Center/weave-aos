package com.studentcenter.weave.presentation.view.my

import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentEmailCompleteBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity

class EmailCompleteFragment: BaseFragment<FragmentEmailCompleteBinding>(R.layout.fragment_email_complete) {
    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}