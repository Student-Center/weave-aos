package com.studentcenter.weave.presentation.view.request

import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        binding.rvRequestSend.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 리사이클러뷰 아이템 위치 찾기, 아이템 위치가 완전히 보일 때 호출됨
                val rvPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                // 리사이클러뷰 아이템 총개수 (index 접근 이기 때문에 -1)
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                // 페이징 처리
                if(rvPos+5 == totalCount) {
                    vm.getRequestData()
                }
            }
        })
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