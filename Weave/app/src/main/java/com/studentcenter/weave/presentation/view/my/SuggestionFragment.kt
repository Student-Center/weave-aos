package com.studentcenter.weave.presentation.view.my

import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentSuggestionBinding
import com.studentcenter.weave.presentation.base.BaseFragment

class SuggestionFragment: BaseFragment<FragmentSuggestionBinding>(R.layout.fragment_suggestion) {
    override fun init() {
        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}