package com.studentcenter.weave.presentation.view.request

import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentRequestReceivedBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.RequestViewModel

class RequestReceivedFragment(private val vm: RequestViewModel): BaseFragment<FragmentRequestReceivedBinding>(R.layout.fragment_request_received) {
    private lateinit var adapter: RequestReceivedRvAdapter
    private var initFlag = false

    override fun init() {
        binding.btnMove.setOnClickListener {
            val mainActivity = (requireContext() as MainActivity)
            mainActivity.binding.bottomNavi.selectedItemId = mainActivity.binding.bottomNavi.menu[2].itemId
        }

        vm.getReceiveData()

        adapter = RequestReceivedRvAdapter()
        binding.rvRequestReceived.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRequestReceived.adapter = adapter

        vm.receiveData.observe(this) {
            adapter.changeList(vm.receiveData.value!!)
            if (initFlag) {
                rvVisibility()
            } else {
                initFlag = true
            }
        }

        binding.rvRequestReceived.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 리사이클러뷰 아이템 위치 찾기, 아이템 위치가 완전히 보일 때 호출됨
                val rvPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                // 리사이클러뷰 아이템 총개수 (index 접근 이기 때문에 -1)
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                // 페이징 처리
                if(rvPos+5 == totalCount) {
                    vm.getReceiveData()
                }
            }
        })
    }

    private fun rvVisibility(){
        if(vm.receiveData.value!!.isEmpty()){
            binding.llEmpty.visibility = View.VISIBLE
            binding.rvRequestReceived.visibility = View.GONE
        } else {
            binding.llEmpty.visibility = View.GONE
            binding.rvRequestReceived.visibility = View.VISIBLE
        }
    }
}