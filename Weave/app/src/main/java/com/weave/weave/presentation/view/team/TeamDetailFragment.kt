package com.weave.weave.presentation.view.team

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentMyTeamDetailBinding
import com.weave.weave.domain.entity.home.Member
import com.weave.weave.domain.entity.home.ProflieTestEntity
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.view.home.DetailRvAdapter

class TeamDetailFragment: BaseFragment<FragmentMyTeamDetailBinding>(R.layout.fragment_my_team_detail){
    private lateinit var adapter: DetailRvAdapter
    private lateinit var teamData: ProflieTestEntity
    private var data = listOf<Member>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeList()
    }

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        initTeamInfo()
        initRecyclerView()
    }

    private fun initTeamInfo(){
        binding.tvTeamTitle.text = teamData.teamName
        binding.tvTeamLocation.text = teamData.location
    }

    private fun initRecyclerView(){
        adapter = DetailRvAdapter()
        adapter.dataList = data
        binding.rvDetailProfile.adapter = adapter
        binding.rvDetailProfile.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initializeList(){
        val sample = "https://i.namu.wiki/i/ukdzGn7-wELDzW3pwiHKTHwtniRYgksguvHsfD85nVYO51oyK44H-V7kSjTonIaOY6XiIXPCJza8ZVF3EjPUAw.webp"
        val testData = ProflieTestEntity(
            teamName = "TEAM: WEAVE",
            location = "서울",
            score = 80,
            members = listOf(
                Member(
                    mbti = emoji(0x1F9D0, "ISJF"),
                    animal = emoji(0x1F9A5, "나무늘보상"),
                    height = emoji( 0x1F4CF, "176cm"),
                    univ = "위브대학교",
                    major = "위브만세학과",
                    age = "05년생",
                    url = sample
                ),
                Member(
                    mbti = emoji(0x1F9D0, "ISJF"),
                    animal = emoji(0x1F9A5, "나무늘보상"),
                    height = emoji( 0x1F4CF, "176cm"),
                    univ = "위브대학교",
                    major = "위브만세학과",
                    age = "05년생",
                    url = sample
                ),
                Member(
                    mbti = emoji(0x1F9D0, "ISJF"),
                    animal = emoji(0x1F9A5, "나무늘보상"),
                    height = emoji( 0x1F4CF, "176cm"),
                    univ = "위브대학교",
                    major = "위브만세학과",
                    age = "05년생",
                    url = sample
                ),
                Member(
                    mbti = emoji(0x1F9D0, "ISJF"),
                    animal = emoji(0x1F9A5, "나무늘보상"),
                    height = emoji( 0x1F4CF, "176cm"),
                    univ = "위브대학교",
                    major = "위브만세학과",
                    age = "05년생",
                    url = sample
                )
            )
        )

        teamData = testData
        data = testData.members
    }

    private fun emoji(unicode: Int, text: String): String{
        return "${String(Character.toChars(unicode))} $text"
    }
}