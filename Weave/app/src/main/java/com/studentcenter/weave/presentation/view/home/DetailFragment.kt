package com.studentcenter.weave.presentation.view.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.locations
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.databinding.FragmentDetailBinding
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.FindMeetingRequestUseCase
import com.studentcenter.weave.domain.usecase.meeting.RequestMeetingUseCase
import com.studentcenter.weave.domain.usecase.team.GetTeamDetailUseCase
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.util.KakaoShareManager
import com.studentcenter.weave.presentation.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailFragment(private val teamId: String): BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail){
    private lateinit var adapter: DetailRvAdapter
    private var data = listOf<TeamDetailMemberEntity>()
    private var isAlreadyRequest = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = FindMeetingRequestUseCase().findMeetingRequest(accessToken, teamId)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        isAlreadyRequest.value = true
                    }
                }
                is Resource.Error -> {
                    launch(Dispatchers.Main){
                        isAlreadyRequest.value = false

                        if(res.message == "내가 속한 공개된 미팅 팀이 없어요!"){
                            // 케미 부분에 해당 블러 처리
                            isAlreadyRequest.value = true
                        } else if(res.message == "????"){
                            // 케미 부분에 인원 수 안 맞음 블러 처리
                            isAlreadyRequest.value = true
                        }
                    }
                }
                else -> {}
            }
        }
    }

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        Log.i("KAKAOLINK", teamId)
        initializeList()

        isAlreadyRequest.observe(this){
            if(it){
                binding.btnRequest.alpha = 0.6f
            } else {
                binding.btnRequest.alpha = 1f
            }
        }

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnShare.setOnClickListener {
            KakaoShareManager(requireContext()).sendMsg(teamId)
        }

        binding.btnRequest.setOnClickListener{
            if(!isAlreadyRequest.value!!){
                CustomDialog.getInstance(CustomDialog.DialogType.MEETING_REQUEST, binding.tvTeamTitle.text.toString()).apply {
                    setOnOKClickedListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                            when(val res = RequestMeetingUseCase().findMeetingRequest(accessToken, RequestMeetingReq(teamId))){
                                is Resource.Success -> {
                                    launch(Dispatchers.Main){
                                        Toast.makeText(this@DetailFragment.requireContext(), "요청 성공", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                is Resource.Error -> {
                                    launch(Dispatchers.Main) {
                                        Toast.makeText(this@DetailFragment.requireContext(), res.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }.show(requireActivity().supportFragmentManager, "meeting_request")
            }
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
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetTeamDetailUseCase().getTeamDetail(accessToken, teamId)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        data = res.data.members

                        binding.tvTeamTitle.text = res.data.teamIntroduce
                        binding.tvTeamLocation.text = locations?.find { it.name == res.data.location }?.displayName

                        initRecyclerView()
                        setRating(res.data.affinityScore)
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
            0 -> {
                comment = getString(R.string.detail_score4_comment)
                rating = 4
            }
        }

        binding.tvScoreComment.text = comment
        binding.rbScore.rating = rating.toFloat()
        binding.tvScore.text = getString(R.string.detail_score, score.toString())
    }
}