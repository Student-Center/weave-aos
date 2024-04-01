package com.studentcenter.weave.presentation.view.request

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.core.GlobalApplication.Companion.locations
import com.studentcenter.weave.databinding.FragmentRequestDetailBinding
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.AttendMeetingUseCase
import com.studentcenter.weave.domain.usecase.meeting.PassMeetingUseCase
import com.studentcenter.weave.domain.usecase.team.GetTeamDetailUseCase
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.view.home.DetailRvAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestDetailFragment(
    private val teamId: String,
    private val meetingId: String,
    private var alreadyChecked: Boolean,
    private val teamIntroduce: String
): BaseFragment<FragmentRequestDetailBinding>(R.layout.fragment_request_detail){
    private lateinit var adapter: DetailRvAdapter
    private var data = listOf<TeamDetailMemberEntity>()

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        (requireActivity() as MainActivity).showLoadingDialog(requireContext())
        initializeList()

        setBtnActivate()

        isRefresh.observe(this){
            if(it){
                (requireActivity() as MainActivity).replaceFragment(RequestDetailFragment(teamId, meetingId, alreadyChecked, teamIntroduce))
                isRefresh.value = false
            }
        }

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnPass.setOnClickListener {
            passMeeting()
        }

        binding.btnAttend.setOnClickListener {
            if(!alreadyChecked) attendMeeting()
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

    private fun setBtnActivate(){
        if(alreadyChecked){
            binding.btnAttend.alpha = 0.6f
            binding.btnAttend.text = "미팅 참가 완료"
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

    private fun attendMeeting(){
        CustomDialog.getInstance(CustomDialog.DialogType.MEETING_ATTEND, null).apply {
            setOnOKClickedListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                    when(val res = AttendMeetingUseCase().attendMeeting(accessToken, meetingId)){
                        is Resource.Success -> {
                            launch(Dispatchers.Main){
                                alreadyChecked = true
                                setBtnActivate()
                            }
                        }
                        is Resource.Error -> {
                            launch(Dispatchers.Main){
                                CustomToast.createToast(this@RequestDetailFragment.requireContext(), res.message).show()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }.show(this.requireActivity().supportFragmentManager, "attend_dialog")
    }

    private fun passMeeting(){
        CustomDialog.getInstance(CustomDialog.DialogType.MEETING_PASS, teamIntroduce).apply {
            setOnOKClickedListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                    when(val res = PassMeetingUseCase().passMeeting(accessToken, meetingId)){
                        is Resource.Success -> {
                            launch(Dispatchers.Main){
                                this@RequestDetailFragment.requireActivity().supportFragmentManager.popBackStack()
                            }
                        }
                        is Resource.Error -> {
                            launch(Dispatchers.Main){
                                CustomToast.createToast(this@RequestDetailFragment.requireContext(), res.message).show()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }.show(this.requireActivity().supportFragmentManager, "pass_dialog")
    }
}