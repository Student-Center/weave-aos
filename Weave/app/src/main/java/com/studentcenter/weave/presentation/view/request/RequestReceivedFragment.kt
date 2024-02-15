package com.studentcenter.weave.presentation.view.request

import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentRequestReceivedBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.RequestViewModel

class RequestReceivedFragment(private val vm: RequestViewModel): BaseFragment<FragmentRequestReceivedBinding>(R.layout.fragment_request_received) {
    private lateinit var adapter: RequestReceivedRvAdapter

    override fun init() {
        binding.btnMove.setOnClickListener {
            val mainActivity = (requireContext() as MainActivity)
            mainActivity.binding.bottomNavi.selectedItemId = mainActivity.binding.bottomNavi.menu[2].itemId
        }

        vm.getReceiveData()
        setRv()
    }

    private fun setRv(){
        if(vm.receiveData.value!!.isEmpty()){
            binding.llEmpty.visibility = View.VISIBLE
            binding.rvRequestReceived.visibility = View.GONE
        } else {
            binding.llEmpty.visibility = View.GONE
            binding.rvRequestReceived.visibility = View.VISIBLE

            adapter = RequestReceivedRvAdapter()
            adapter.dataList = vm.receiveData.value!!
            binding.rvRequestReceived.layoutManager = LinearLayoutManager(requireContext())
            binding.rvRequestReceived.adapter = adapter
        }
    }
}