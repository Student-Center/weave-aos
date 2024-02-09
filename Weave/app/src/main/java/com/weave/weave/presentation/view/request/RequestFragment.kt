package com.weave.weave.presentation.view.request

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.weave.weave.R
import com.weave.weave.databinding.FragmentRequestBinding
import com.weave.weave.presentation.base.BaseFragment

class RequestFragment: BaseFragment<FragmentRequestBinding>(R.layout.fragment_request) {

    override fun init() {
        val tabTextList = listOf(getString(R.string.request_tab_received), getString(R.string.request_tab_send))

        binding.vpRequest.adapter = RequestVpAdapter(requireActivity())

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