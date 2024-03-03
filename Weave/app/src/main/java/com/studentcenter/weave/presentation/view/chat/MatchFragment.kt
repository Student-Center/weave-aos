package com.studentcenter.weave.presentation.view.chat

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentMatchBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity

class MatchFragment: BaseFragment<FragmentMatchBinding>(R.layout.fragment_match) {
    private var backPressedTime: Long = 0L

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                requireActivity().finishAffinity()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


    }
}