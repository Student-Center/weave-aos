package com.studentcenter.weave.presentation.view.chat

import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentMatchDetailBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity

class MatchDetailFragment: BaseFragment<FragmentMatchDetailBinding>(R.layout.fragment_match_detail) {
    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)

        binding.ibBack.setOnClickListener{
            (requireActivity() as MainActivity).supportFragmentManager.popBackStack()
        }

        binding.btnMove.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.popBackStack()
        }
    }
}