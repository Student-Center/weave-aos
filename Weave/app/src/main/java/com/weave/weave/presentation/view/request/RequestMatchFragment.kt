package com.weave.weave.presentation.view.request

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.weave.weave.R
import com.weave.weave.databinding.FragmentRequestMatchBinding
import com.weave.weave.databinding.ItemRequestMatchTeamBinding
import com.weave.weave.domain.entity.team.MemberInfo
import com.weave.weave.domain.entity.team.RequestMatchTeamEntity
import com.weave.weave.domain.entity.team.TeamInfo
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.view.home.DetailFragment

class RequestMatchFragment(private val id: String): BaseFragment<FragmentRequestMatchBinding>(R.layout.fragment_request_match) {
    // id : receive or send 에 대한 team id? (해당 Fragment 데이터 호출할 때 사용할 듯)
    private lateinit var data: RequestMatchTeamEntity

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        getData()
        setMyTeam()
        setOtherTeam()
        moveDetailFragment()
    }

    private fun getData(){
        data = RequestMatchTeamEntity(
            id = "",
            team = listOf(
                TeamInfo(checked = 2, "Team 1", listOf(
                    MemberInfo(id = "", url = "", univ = "위브대•05", mbti = "ISFJ", checked = true),
                    MemberInfo(id = "", url = "", univ = "위브대•05", mbti = "ISFJ", checked = false),
                    MemberInfo(id = "", url = "", univ = "위브대•05", mbti = "ISFJ", checked = true))
                ),
                TeamInfo(checked = 2, "Team 2", listOf(
                    MemberInfo(id = "1", url = "", univ = "위브대•05", mbti = "ISFJ", checked = true),
                    MemberInfo(id = "", url = "", univ = "위브대•05", mbti = "ISFJ", checked = true),
                    MemberInfo(id = "", url = "", univ = "위브대•05", mbti = "ISFJ", checked = false))
                )
            )
        )
    }

    private fun setOtherTeam(){

        with(binding.includeTeamOther){
            val memberSize = data.team[0].members.size
            tvCheckMember.text = getString(R.string.request_team_checked_member, data.team[0].checked.toString())
            tvTeamTitle.text = data.team[0].title

            val itemList = listOf(item1, item2, item3, item4)

            for (i in itemList.indices) {
                itemList[i].visibility = if (i < memberSize) View.VISIBLE else View.GONE
            }

            setMemberInfo(memberSize, this, false)
        }
    }

    private fun setMyTeam(){
        with(binding.includeTeamMy){
            val memberSize = data.team[1].members.size
            tvCheckMember.text = getString(R.string.request_team_checked_member, data.team[1].checked.toString())
            tvTeamTitle.text = data.team[1].title

            val itemList = listOf(item1, item2, item3, item4)

            for (i in itemList.indices) {
                itemList[i].visibility = if (i < memberSize) View.VISIBLE else View.GONE
            }

            setMemberInfo(memberSize, this, true)
        }
    }

    private fun setMemberInfo(memberSize: Int, view: ItemRequestMatchTeamBinding, isMyTeam: Boolean){
        // 팀원 정보 설정
        for (i in 0 until memberSize) {
            val member = if(isMyTeam) data.team[1].members[i] else data.team[0].members[i]
            val imageView = when (i) {
                0 -> view.ivItemProfile1
                1 -> view.ivItemProfile2
                2 -> view.ivItemProfile3
                3 -> view.ivItemProfile4
                else -> null
            }
            // + 이미지 로드 구현 해야 함 (Glide)

            // 내 팀의 경우만 처리
            if(isMyTeam){
                if(member.checked){
                    imageView?.foreground = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_check_member)
                }
                if(member.id != "1"){
                    imageView?.foregroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.green_67))
                }

                val isMeView = when (i) {
                    0 -> view.viewMy1
                    1 -> view.viewMy2
                    2 -> view.viewMy3
                    3 -> view.viewMy4
                    else -> null
                }
                if(member.id == "1"){
                    Log.i(TAG, "isMe")
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
            univTextView?.text = member.univ
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
            (requireActivity() as MainActivity).replaceFragmentWithStack(DetailFragment())
        }

        binding.flTeamOther.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentWithStack(DetailFragment())
        }
    }

    private fun requestParticipate(){}
    private fun requestPass(){}
}