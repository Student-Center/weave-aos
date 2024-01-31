package com.weave.weave.presentation.view.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentHomeBinding
import com.weave.weave.domain.entity.home.TeamTestEntity

class HomeFragment: BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var adapter: HomeRvAdapter
    private val data=mutableListOf<TeamTestEntity>()

    override fun init() {
        initializeList()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter = HomeRvAdapter()
        adapter.dataList = data
        binding.rvHome.adapter = adapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initializeList(){

    }
}