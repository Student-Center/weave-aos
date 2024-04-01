package com.studentcenter.weave.presentation.view.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.core.GlobalApplication.Companion.locations
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.databinding.FragmentDetailBinding
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.FindMeetingRequestUseCase
import com.studentcenter.weave.domain.usecase.meeting.RequestMeetingUseCase
import com.studentcenter.weave.domain.usecase.team.GetTeamDetailUseCase
import com.studentcenter.weave.presentation.custom.CustomToast
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
                        isAlreadyRequest.value = false
                    }
                }
                is Resource.Error -> {
                    launch(Dispatchers.Main){

                        // blur 처리 -> 수정 필요
                        isAlreadyRequest.value = true
                        binding.llAffinity.alpha = 0f
                        binding.clAffinity.background = AppCompatResources.getDrawable(requireContext(), R.drawable.image_exception_background)

                        when (res.message) {
                            "MEETING-000" -> { // 이미 요청한 경우 -> 버튼 라이팅 물어봐야함
                                binding.llAffinity.alpha = 1f
                                binding.clAffinity.background = null
                                binding.btnRequest.text = getString(R.string.exception_meeting_000)
                            }
                            "MEETING-004", "MEETING-005" -> { // 팀이 없거나 공개되지 않은 경우
                                binding.tvException.text = getString(R.string.exception_meeting_004)
                            }
                            "MEETING-008" -> { // 상대팀과 인원 수가 맞지 않은 경우
                                binding.tvException.text = getString(R.string.exception_meeting_008)
                            }
                            else -> {
                                // FINISHED_MEETING("MEETING-001"),
                                // MEETING_NOT_JOINED_USER("MEETING-002"),
                                // ALREADY_ATTENDANCE_CREATED("MEETING-003"),
                                // CAN_NOT_MEETING_REQUEST_NOT_UNIV_VERIFIED_USER("MEETING-006"),
                                // CAN_NOT_MEETING_REQUEST_SAME_GENDER("MEETING-007"),
                                binding.tvException.text = getString(R.string.exception_meeting_etc)
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        (requireActivity() as MainActivity).showLoadingDialog(requireContext())
        initializeList()

        isRefresh.observe(this){
            if(it){
                (requireActivity() as MainActivity).replaceFragment(DetailFragment(teamId))
                isRefresh.value = false
            }
        }

        isAlreadyRequest.observe(this){
            binding.btnRequest.alpha = if(it) 0.6f else 1f
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
                                        isAlreadyRequest.value = false
                                        isRefresh.value = true
                                    }
                                }
                                is Resource.Error -> {
                                    launch(Dispatchers.Main) {
                                        CustomToast.createToast(this@DetailFragment.requireContext(), res.message).show()
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

                        Handler(Looper.getMainLooper()).postDelayed({
                            (requireActivity() as MainActivity).dismissLoadingDialog()
                            initRecyclerView()
                            setRating(res.data.affinityScore)
                        }, 600)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "DetailFragment Error: ${res.message}")
                }
                else -> {}
            }
        }
    }

    private fun setRating(rating: Int){
        var comment = getString(R.string.detail_score4_comment)
        var score = 4

        when(rating){
            5 -> {
                comment = getString(R.string.detail_score5_comment)
                score = 100
            }
            4 -> {
                comment = getString(R.string.detail_score4_comment)
                score = 80
            }
            3 -> {
                comment = getString(R.string.detail_score3_comment)
                score = 60
            }
            2 -> {
                comment = getString(R.string.detail_score2_comment)
                score = 40
            }
        }

        binding.tvScoreComment.text = comment
        binding.rbScore.rating = rating.toFloat()
        binding.tvScore.text = getString(R.string.detail_score, score.toString())
    }
}