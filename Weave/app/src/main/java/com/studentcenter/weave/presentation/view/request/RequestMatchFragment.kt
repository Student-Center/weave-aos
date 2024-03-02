package com.studentcenter.weave.presentation.view.request

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.databinding.FragmentRequestMatchBinding
import com.studentcenter.weave.databinding.ItemRequestMatchTeamBinding
import com.studentcenter.weave.domain.entity.meeting.MeetingListItemEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListTeamEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.GetAttendanceUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.view.home.DetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class RequestMatchFragment(private val data: MeetingListItemEntity): BaseFragment<FragmentRequestMatchBinding>(R.layout.fragment_request_match) {
    private var teamType: String? = null
    private lateinit var timerTask: CountDownTimer

    @SuppressLint("SetTextI18n")
    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        binding.btnBack.setOnClickListener {
            timerTask.cancel()
            requireActivity().supportFragmentManager.popBackStack()
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
                        while(requestTeam.hasNext()){
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
                if(member.checked){
                    imageView?.foreground = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_check_member)
                }
                if(member.id != myInfo?.id){
                    imageView?.foregroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.green_67))
                }

                val isMeView = when (i) {
                    0 -> view.viewMy1
                    1 -> view.viewMy2
                    2 -> view.viewMy3
                    3 -> view.viewMy4
                    else -> null
                }
                if(member.id == myInfo?.id){
                    isMeView?.visibility = View.VISIBLE
                }
            }

            val univTextView = when (i) {
                0 -> view.tvItemUniv1
                1 -> view.tvItemUniv2
                2 -> view.tvItemUniv3
                3 -> view.tvItemUniv4
                else -> null
            }
            univTextView?.text = member.universityName
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
        // DetailFragment 진입 시 TeamId 필요

        binding.flTeamMy.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentWithStack(DetailFragment(
                if(teamType == "REQUESTING") data.requestingTeam.id else data.receivingTeam.id
            ))
        }

        binding.flTeamOther.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentWithStack(DetailFragment(
                if(teamType == "REQUESTING") data.receivingTeam.id else data.requestingTeam.id
            ))
        }
    }

    private fun requestParticipate(){}
    private fun requestPass(){}
}