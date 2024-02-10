package com.weave.weave.presentation.view.request

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.weave.weave.presentation.viewmodel.RequestViewModel

class RequestVpAdapter(fragmentActivity: FragmentActivity, private val vm: RequestViewModel): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RequestReceivedFragment(vm)
            1 -> RequestSendFragment(vm)
            else -> throw IllegalArgumentException("알 수 없는 유형")
        }
    }
}