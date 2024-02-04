package com.weave.weave.presentation.view.team

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.weave.weave.R
import com.weave.weave.databinding.FragmentTeamBinding
import com.weave.weave.domain.entity.home.TeamMember
import com.weave.weave.domain.entity.home.TeamTestEntity
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.presentation.view.MainActivity

class TeamFragment: BaseFragment<FragmentTeamBinding>(R.layout.fragment_team) {
    var data = mutableListOf<TeamTestEntity>()
    private lateinit var adapter: TeamRvAdapter

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

        initializeList()
    }

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        initRecyclerView()

        binding.btnNew.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, TeamNewFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initRecyclerView(){
        if(data.isEmpty()){
            binding.rvTeam.visibility = View.GONE
            binding.llEmpty.visibility = View.VISIBLE
        } else {
            binding.rvTeam.visibility = View.VISIBLE
            binding.llEmpty.visibility = View.GONE
        }

        adapter = TeamRvAdapter()
        adapter.dataList = data
        binding.rvTeam.adapter = adapter
        binding.rvTeam.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initializeList(){
        val sample = "https://i.namu.wiki/i/ukdzGn7-wELDzW3pwiHKTHwtniRYgksguvHsfD85nVYO51oyK44H-V7kSjTonIaOY6XiIXPCJza8ZVF3EjPUAw.webp"
        with(data){
            add(
                TeamTestEntity("4:4", "Team 1", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
            )
                )
            )
            add(
                TeamTestEntity("3:3", "Team 2", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
            )
                )
            )
            add(
                TeamTestEntity("2:2", "Team 3", "서울", listOf(
                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
            )
                )
//            )
//            add(
//                TeamTestEntity("3:3", "Team 4", "서울", listOf(
//                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
//                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
//                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
//            )
//                )
//            )
//            add(
//                TeamTestEntity("4:4", "Team 5", "서울", listOf(
//                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
//                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
//                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
//                TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
//            )
//                )
            )
        }
    }
}