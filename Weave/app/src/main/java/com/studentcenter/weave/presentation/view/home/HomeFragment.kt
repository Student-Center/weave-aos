package com.studentcenter.weave.presentation.view.home

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentHomeBinding
import com.studentcenter.weave.domain.entity.team.GetTeamListItemEntity
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.HomeViewModel

class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: HomeRvAdapter
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

        viewModel.getTeamList()
    }

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        initRecyclerView()

        binding.ibFilter.setOnClickListener {
            FilterBottomSheetDialog.getInstance(viewModel).show(requireActivity().supportFragmentManager, "filter")
        }

        viewModel.isChangedFilter.observe(this){
            if(it){
                viewModel.clearData()
                viewModel.getTeamList()
            }
        }

        viewModel.data.observe(this){
            adapter.changeList(viewModel.data.value!!.toList())
        }

        binding.rvHome.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 리사이클러뷰 아이템 위치 찾기, 아이템 위치가 완전히 보일 때 호출됨
                val rvPos = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                // 리사이클러뷰 아이템 총개수 (index 접근 이기 때문에 -1)
                val totalCount = recyclerView.adapter?.itemCount?.minus(1)

                // 페이징 처리 (+5는 매끄러운 스크롤을 위해 마지막으로 부터 5개 위 아이템이 그려질 때 로드 되도록
                if(rvPos+5 == totalCount) {
                    viewModel.getTeamList()
                }
            }

        })
    }

    private fun initRecyclerView(){
        adapter = HomeRvAdapter()
        adapter.dataList = mutableListOf<GetTeamListItemEntity>()
        binding.rvHome.adapter = adapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
    }
}