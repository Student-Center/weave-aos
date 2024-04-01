package com.studentcenter.weave.presentation.view.chat

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentMatchBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.MatchViewModel

class MatchFragment: BaseFragment<FragmentMatchBinding>(R.layout.fragment_match) {
    private val viewModel: MatchViewModel by viewModels()
    private lateinit var adapter: MatchRvAdapter
    private var backPressedTime: Long = 0L

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                requireActivity().finishAffinity()
            } else {
                backPressedTime = System.currentTimeMillis()
                CustomToast.createToast(requireContext(), "한 번 더 누르면 종료됩니다.").show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getPreparedMeetings()
    }

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        initRv()

        viewModel.teamList.observe(this){
            adapter.changeList(viewModel.teamList.value!!)

            if(viewModel.initFlag && adapter.dataList.isEmpty()){
                binding.rvMatch.visibility = View.GONE
                binding.llEmpty.visibility = View.VISIBLE
            } else {
                binding.rvMatch.visibility = View.VISIBLE
                binding.llEmpty.visibility = View.GONE
            }
        }

        viewModel.errorEvent.observe(this){
            if(it.isNotEmpty()){
                CustomToast.createToast(requireContext(), it).show()
                viewModel.setErrorEvent()
            }
        }

        binding.btnChatToHome.setOnClickListener {
            (requireActivity() as MainActivity).naviItemChange(2)
        }

        binding.rvMatch.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 리사이클러뷰 아이템 위치 찾기, 아이템 위치가 완전히 보일 때 호출됨
                val rvPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                // 리사이클러뷰 아이템 총개수 (index 접근 이기 때문에 -1)
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                // 페이징 처리
                if(rvPos+5 == totalCount) {
                    viewModel.getPreparedMeetings()
                }
            }
        })
    }

    private fun initRv(){
        adapter = MatchRvAdapter()
        adapter.dataList = mutableListOf()
        binding.rvMatch.adapter = adapter
        binding.rvMatch.layoutManager = LinearLayoutManager(requireContext())
    }
}