package com.studentcenter.weave.presentation.view.request

import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentRequestRequestBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.RequestViewModel

class RequestRequestFragment(private val vm: RequestViewModel): BaseFragment<FragmentRequestRequestBinding>(R.layout.fragment_request_request) {
    private lateinit var adapter: RequestRequestRvAdapter
    private var initFlag = false

    override fun init() {
        binding.btnMove.setOnClickListener {
            val mainActivity = (requireContext() as MainActivity)
            mainActivity.binding.bottomNavi.selectedItemId = mainActivity.binding.bottomNavi.menu[2].itemId
        }

        vm.getRequestData()

        adapter = RequestRequestRvAdapter()
        binding.rvRequestSend.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRequestSend.adapter = adapter

        vm.requestData.observe(this){
            adapter.changeList(vm.requestData.value!!)
            if(initFlag) { rvVisibility() }
            else { initFlag = true }
        }
    }

    private fun rvVisibility(){
        if(vm.requestData.value!!.isEmpty()){
            binding.llEmpty.visibility = View.VISIBLE
            binding.rvRequestSend.visibility = View.GONE
        } else {
            binding.llEmpty.visibility = View.GONE
            binding.rvRequestSend.visibility = View.VISIBLE
        }
    }
}