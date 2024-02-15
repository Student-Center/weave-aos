package com.studentcenter.weave.presentation.view.request

import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentRequestSendBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.RequestViewModel

class RequestSendFragment(private val vm: RequestViewModel): BaseFragment<FragmentRequestSendBinding>(R.layout.fragment_request_send) {
    private lateinit var adapter: RequestSendRvAdapter

    override fun init() {
        binding.btnMove.setOnClickListener {
            val mainActivity = (requireContext() as MainActivity)
            mainActivity.binding.bottomNavi.selectedItemId = mainActivity.binding.bottomNavi.menu[2].itemId
        }

        vm.getSendData()
        setRv()
    }

    private fun setRv(){
        if(vm.sendData.value!!.isEmpty()){
            binding.llEmpty.visibility = View.VISIBLE
            binding.rvRequestSend.visibility = View.GONE
        } else {
            binding.llEmpty.visibility = View.GONE
            binding.rvRequestSend.visibility = View.VISIBLE

            adapter = RequestSendRvAdapter()
            adapter.dataList = vm.sendData.value!!
            binding.rvRequestSend.layoutManager = LinearLayoutManager(requireContext())
            binding.rvRequestSend.adapter = adapter
        }
    }
}