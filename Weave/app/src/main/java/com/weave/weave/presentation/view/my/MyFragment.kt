package com.weave.weave.presentation.view.my

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentMyPageBinding
import com.weave.weave.presentation.view.MainActivity

class MyFragment: BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
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

        binding.ibSetting.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, SettingFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}