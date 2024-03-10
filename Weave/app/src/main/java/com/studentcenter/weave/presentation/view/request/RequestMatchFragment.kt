package com.studentcenter.weave.presentation.view.request

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.databinding.FragmentRequestMatchBinding
import com.studentcenter.weave.databinding.ItemRequestMatchTeamBinding
import com.studentcenter.weave.domain.entity.meeting.MeetingListItemEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListTeamEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.AttendMeetingUseCase
import com.studentcenter.weave.domain.usecase.meeting.GetAttendanceUseCase
import com.studentcenter.weave.domain.usecase.meeting.PassMeetingUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.view.team.TeamDetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class RequestMatchFragment(private val data: MeetingListItemEntity): BaseFragment<FragmentRequestMatchBinding>(R.layout.fragment_request_match) {
    private var teamType: String? = null
    private var alreadyChecked = false
    private lateinit var timerTask: CountDownTimer

    @SuppressLint("SetTextI18n")
    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        binding.btnBack.setOnClickListener {
            runBlocking {
                timerTask.cancel()
            }
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnPass.setOnClickListener {
            passMeeting()
        }

        binding.btnAttend.setOnClickListener {
            if(!alreadyChecked) attendMeeting()
        }

        isRefresh.observe(this){
            if(it){
                (requireActivity() as MainActivity).replaceFragment(RequestMatchFragment(data))
                isRefresh.value = false
            }
        }

        teamType = data.teamType

        (requireActivity() as MainActivity).showLoadingDialog(requireContext())
        getIsChecked()

        val seoul = TimeZone.getTimeZone("Asia/Seoul")
        val currentTime = Calendar.getInstance(seoul).timeInMillis
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        dateFormat.timeZone = seoul
        val endTime = dateFormat.parse(data.pendingEndAt)!!.time
        val difference = endTime - currentTime

        if(difference > 0){
            timerTask = object : CountDownTimer(difference, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60

                    binding.tvTimer.text = String.format(getString(R.string.match_timer_format), hours, minutes % 60, seconds % 60)
                }

                override fun onFinish() {
                    binding.tvTimer.text = String.format(getString(R.string.match_timer_format), 0, 0, 0)
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
            timerTask.start()
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun getIsChecked(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetAttendanceUseCase().getAttendances(accessToken, data.id)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){

                        val requestTeam = data.requestingTeam.memberInfos.listIterator()
                        while(requestTeam.hasNext()){
                            val member = requestTeam.next()
                            res.data.find { it.memberId == member.id }.let { member.checked = it?.attendance ?: false }
                        }

                        val receiveTeam = data.receivingTeam.memberInfos.listIterator()
                        while(receiveTeam.hasNext()){
                            val member = receiveTeam.next()
                            res.data.find { it.memberId == member.id }.let { member.checked = it?.attendance ?: false }
                        }

                        setMyTeam()
                        setOtherTeam()
                        moveDetailFragment()

                        Handler(Looper.getMainLooper()).postDelayed({
                            (requireActivity() as MainActivity).dismissLoadingDialog()
                        }, 1000)
                    }
                }
                is Resource.Error -> {
                    (requireActivity() as MainActivity).dismissLoadingDialog()
                    Log.i(TAG, res.message)
                }
                else -> {}
            }
        }
    }

    private fun setOtherTeam(){

        with(binding.includeTeamOther){
            val subData = if(teamType == "REQUESTING") data.receivingTeam else data.requestingTeam
            val memberSize = subData.memberCount
            val checkedMember = subData.memberInfos.count { it.checked }

            tvCheckMember.text = getString(R.string.request_team_checked_member, checkedMember.toString())
            tvTeamTitle.text = subData.teamIntroduce

            val itemList = listOf(item1, item2, item3, item4)

            for (i in itemList.indices) {
                itemList[i].visibility = if (i < memberSize) View.VISIBLE else View.GONE
            }

            setMemberInfo(memberSize, this, false, subData)
        }
    }

    private fun setMyTeam(){
        with(binding.includeTeamMy){
            val subData = if(teamType == "REQUESTING") data.requestingTeam else data.receivingTeam
            val memberSize = subData.memberCount
            val checkedMember = subData.memberInfos.count { it.checked }

            tvCheckMember.text = getString(R.string.request_team_checked_member, checkedMember.toString())
            tvTeamTitle.text = subData.teamIntroduce

            val itemList = listOf(item1, item2, item3, item4)

            for (i in itemList.indices) {
                itemList[i].visibility = if (i < memberSize) View.VISIBLE else View.GONE
            }

            setMemberInfo(memberSize, this, true, subData)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMemberInfo(memberSize: Int, view: ItemRequestMatchTeamBinding, isMyTeam: Boolean, data: MeetingListTeamEntity){
        // 팀원 정보 설정
        for (i in 0 until memberSize) {
            val member = data.memberInfos[i]
            val imageView = when (i) {
                0 -> view.ivItemProfile1
                1 -> view.ivItemProfile2
                2 -> view.ivItemProfile3
                3 -> view.ivItemProfile4
                else -> null
            }

            if (imageView != null) {
                Glide.with(imageView)
                    .load("${BuildConfig.MBTI_LIST}${member.mbti.uppercase()}.png")
                    .into(imageView)
            }

            // 내 팀의 경우만 처리
            if(isMyTeam){
                val isMeView = when (i) {
                    0 -> view.viewMy1
                    1 -> view.viewMy2
                    2 -> view.viewMy3
                    3 -> view.viewMy4
                    else -> null
                }
                if(member.userId != myInfo?.id){ // 본인 아님
                    if(member.checked){
                        isMeView?.visibility = View.VISIBLE
                        isMeView?.foreground = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_check_member)
                        isMeView?.foregroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.green_67))
                        isMeView?.background = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_match_profile_stroke)
                    }
                } else { // 본인
                    isMeView?.visibility = View.VISIBLE

                    if(member.checked) {
                        alreadyChecked = true
                        binding.btnAttend.alpha = 0.6f
                        binding.btnAttend.text = "미팅 참가 완료"
                        isMeView?.foreground = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_check_member)
                        isMeView?.background = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_match_profile_stroke_me)
                    }
                }
            }

            val univTextView = when (i) {
                0 -> view.tvItemUniv1
                1 -> view.tvItemUniv2
                2 -> view.tvItemUniv3
                3 -> view.tvItemUniv4
                else -> null
            }
            univTextView?.text = "${member.universityName}•${member.birthYear.toString().takeLast(2)}"
            val mbtiTextView = when (i) {
                0 -> view.tvItemMbti1
                1 -> view.tvItemMbti2
                2 -> view.tvItemMbti3
                3 -> view.tvItemMbti4
                else -> null
            }
            mbtiTextView?.text = member.mbti
        }
    }

    private fun moveDetailFragment(){
        binding.flTeamMy.setOnClickListener {
            timerTask.cancel()
            (requireActivity() as MainActivity).replaceFragmentWithStack(TeamDetailFragment(
                if(teamType == "REQUESTING") data.requestingTeam.id else data.receivingTeam.id
            ))
        }

        binding.flTeamOther.setOnClickListener {
            timerTask.cancel()
            (requireActivity() as MainActivity).replaceFragmentWithStack(TeamDetailFragment(
                if(teamType == "REQUESTING") data.receivingTeam.id else data.requestingTeam.id
            ))
        }
    }

    private fun attendMeeting(){
        CustomDialog.getInstance(CustomDialog.DialogType.MEETING_ATTEND, null).apply {
            setOnOKClickedListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                    when(val res = AttendMeetingUseCase().attendMeeting(accessToken, data.id)){
                        is Resource.Success -> {
                            launch(Dispatchers.Main){
                                timerTask.cancel()
                                isRefresh.value = true
                            }
                        }
                        is Resource.Error -> {
                            launch(Dispatchers.Main){
                                Toast.makeText(this@RequestMatchFragment.requireContext(), res.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }.show(this.requireActivity().supportFragmentManager, "attend_dialog")
    }

    private fun passMeeting(){
        CustomDialog.getInstance(CustomDialog.DialogType.MEETING_PASS, if(teamType == "REQUESTING") data.receivingTeam.teamIntroduce else data.requestingTeam.teamIntroduce).apply {
            setOnOKClickedListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                    when(val res = PassMeetingUseCase().passMeeting(accessToken, data.id)){
                        is Resource.Success -> {
                            launch(Dispatchers.Main){
                                timerTask.cancel()
                                this@RequestMatchFragment.requireActivity().supportFragmentManager.popBackStack()
                            }
                        }
                        is Resource.Error -> {
                            launch(Dispatchers.Main){
                                Toast.makeText(this@RequestMatchFragment.requireContext(), res.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }.show(this.requireActivity().supportFragmentManager, "pass_dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()

        timerTask.cancel()
    }
}