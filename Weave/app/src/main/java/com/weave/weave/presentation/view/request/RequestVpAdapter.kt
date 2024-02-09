package com.weave.weave.presentation.view.request

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RequestVpAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RequestReceivedFragment()
            1 -> RequestSendFragment()
            else -> throw IllegalArgumentException("알 수 없는 유형")
        }
    }
}