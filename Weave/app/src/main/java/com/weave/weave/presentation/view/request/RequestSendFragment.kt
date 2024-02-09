package com.weave.weave.presentation.view.request

import androidx.core.view.get
import com.weave.weave.R
import com.weave.weave.databinding.FragmentRequestSendBinding
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.presentation.view.MainActivity

class RequestSendFragment: BaseFragment<FragmentRequestSendBinding>(R.layout.fragment_request_send) {

    override fun init() {
        binding.btnMove.setOnClickListener {
            val mainActivity = (requireContext() as MainActivity)
            mainActivity.binding.bottomNavi.selectedItemId = mainActivity.binding.bottomNavi.menu[2].itemId
        }
    }
}