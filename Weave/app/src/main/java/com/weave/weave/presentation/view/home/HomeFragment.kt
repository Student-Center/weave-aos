package com.weave.weave.presentation.view.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentHomeBinding
import com.weave.weave.domain.entity.home.TeamMember
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
        val sample = "https://i.namu.wiki/i/ukdzGn7-wELDzW3pwiHKTHwtniRYgksguvHsfD85nVYO51oyK44H-V7kSjTonIaOY6XiIXPCJza8ZVF3EjPUAw.webp"
        with(data){
            add(TeamTestEntity("4:4", "Team 1", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
            )))
            add(TeamTestEntity("3:3", "Team 2", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
            )))
            add(TeamTestEntity("2:2", "Team 3", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
            )))
            add(TeamTestEntity("3:3", "Team 4", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
            )))
            add(TeamTestEntity("4:4", "Team 5", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
            )))
        }
    }
}