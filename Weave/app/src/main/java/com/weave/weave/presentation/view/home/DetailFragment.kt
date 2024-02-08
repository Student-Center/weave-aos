package com.weave.weave.presentation.view.home

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentDetailBinding
import com.weave.weave.domain.entity.home.Member
import com.weave.weave.domain.entity.home.ProflieTestEntity
import com.weave.weave.presentation.util.KakaoShareManager
import com.weave.weave.presentation.view.MainActivity

class DetailFragment: BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail){
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

        binding.btnShare.setOnClickListener {
            KakaoShareManager(requireContext()).sendMsg()
        }

        initTeamInfo()
        initRecyclerView()
    }

    private fun initTeamInfo(){
        binding.tvTeamTitle.text = teamData.teamName
        binding.tvTeamLocation.text = teamData.location

        setRating(teamData.score)
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

    private fun setRating(score: Int){
        var comment = ""
        var rating = 0

        when(score){
            100 -> {
                comment = getString(R.string.detail_score5_comment)
                rating = 5
            }
            80 -> {
                comment = getString(R.string.detail_score4_comment)
                rating = 4
            }
            60 -> {
                comment = getString(R.string.detail_score3_comment)
                rating = 3
            }
            40 -> {
                comment = getString(R.string.detail_score2_comment)
                rating = 2
            }
        }

        binding.tvScoreComment.text = comment
        binding.rbScore.rating = rating.toFloat()
        binding.tvScore.text = getString(R.string.detail_score, score.toString())
    }

    private fun emoji(unicode: Int, text: String): String{
        return "${String(Character.toChars(unicode))} $text"
    }
}