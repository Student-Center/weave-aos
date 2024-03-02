package com.studentcenter.weave.presentation.view.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentHomeBinding
import com.studentcenter.weave.domain.entity.team.GetTeamListItemEntity
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
                Toast.makeText(requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
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
                viewModel.next = null
            }
        }

        viewModel.data.observe(this){
            adapter.changeList(viewModel.data.value!!.toList())
        }
    }

    private fun initRecyclerView(){
        adapter = HomeRvAdapter()
        adapter.dataList = mutableListOf<GetTeamListItemEntity>()
        binding.rvHome.adapter = adapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
    }
}