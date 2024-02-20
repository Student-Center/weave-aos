package com.studentcenter.weave.presentation.view.home

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication
import com.studentcenter.weave.databinding.FragmentDetailBinding
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.GetTeamDetailUseCase
import com.studentcenter.weave.presentation.util.KakaoShareManager
import com.studentcenter.weave.presentation.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailFragment(private val teamId: String): BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail){
    private lateinit var adapter: DetailRvAdapter
    private var data = listOf<TeamDetailMemberEntity>()

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        Log.i("KAKAOLINK", teamId)
        initializeList()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnShare.setOnClickListener {
            KakaoShareManager(requireContext()).sendMsg(teamId)
        }
    }

    private fun initRecyclerView(){
        adapter = DetailRvAdapter()
        adapter.dataList = data
        binding.rvDetailProfile.adapter = adapter
        binding.rvDetailProfile.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initializeList(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = GlobalApplication.app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetTeamDetailUseCase().getTeamDetail(accessToken, teamId)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        data = res.data.members

                        binding.tvTeamTitle.text = res.data.teamIntroduce
                        binding.tvTeamLocation.text = GlobalApplication.locations?.find { it.name == res.data.location }?.displayName

                        initRecyclerView()
//                        setRating() 아직 점수 API 추가 안됨
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "TeamDetailFragment Error: ${res.message}")
                }
                else -> {}
            }
        }
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
}