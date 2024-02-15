package com.studentcenter.weave.presentation.view.request

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentRequestBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.RequestViewModel

class RequestFragment: BaseFragment<FragmentRequestBinding>(R.layout.fragment_request) {
    private val viewModel by viewModels<RequestViewModel>()

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
        (requireContext() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val tabTextList = listOf(getString(R.string.request_tab_received), getString(R.string.request_tab_send))
        binding.vpRequest.adapter = RequestVpAdapter(requireActivity(), viewModel)

        TabLayoutMediator(binding.tlRequest, binding.vpRequest) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()

        binding.vpRequest.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val receivedTab = binding.tlRequest.getTabAt(0)!!
                val sendTab = binding.tlRequest.getTabAt(1)!!

                if(receivedTab.isSelected){
                    receivedTab.view.background = receivedGradient(true)
                } else {
                    receivedTab.view.background = ColorDrawable(Color.TRANSPARENT)
                }

                if(sendTab.isSelected){
                    sendTab.view.background = receivedGradient(false)
                } else {
                    sendTab.view.background = ColorDrawable(Color.TRANSPARENT)
                }

            }
        })
    }

    private fun receivedGradient(tab: Boolean): GradientDrawable{
        val receivedColors = intArrayOf(
            Color.parseColor("#CC2EAE"),
            Color.parseColor("#CC596E"),
            Color.parseColor("#CE8A36"),
            Color.parseColor("#CD9E0A")
        )

        val sendColors = intArrayOf(
            Color.parseColor("#CD9E0A"),
            Color.parseColor("#B7B115"),
            Color.parseColor("#99C016"),
            Color.parseColor("#73CF14")
        )

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            if(tab) receivedColors else sendColors
        )

        gradientDrawable.cornerRadius = 180f

        return gradientDrawable
    }
}