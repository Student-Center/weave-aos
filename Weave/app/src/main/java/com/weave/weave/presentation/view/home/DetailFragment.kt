package com.weave.weave.presentation.view.home

import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentDetailBinding
import com.weave.weave.presentation.view.MainActivity

class DetailFragment: BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail){
    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}