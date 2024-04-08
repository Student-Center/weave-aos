package com.studentcenter.weave.presentation.view.team

import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.isRefresh
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.databinding.FragmentTeamBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.CreateInvitationLinkUseCase
import com.studentcenter.weave.domain.usecase.team.LeaveTeamUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.custom.CustomToast
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.util.KakaoShareManager
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.view.my.MyFragment
import com.studentcenter.weave.presentation.viewmodel.TeamViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TeamFragment: BaseFragment<FragmentTeamBinding>(R.layout.fragment_team) {
    private val viewModel by viewModels<TeamViewModel>()
    private lateinit var adapter: TeamRvAdapter

    private var backPressedTime: Long = 0L

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                requireActivity().finishAffinity()
            } else {
                backPressedTime = System.currentTimeMillis()
                CustomToast.createToast(requireContext(), "한 번 더 누르면 종료됩니다.").show()
            }
        }
    }

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.next = null
        viewModel.initializeList()
        initRecyclerView()

        isRefresh.observe(this){
            if(it) {
                (requireActivity() as MainActivity).replaceFragment(TeamFragment())
                isRefresh.value = false
            }
        }

        viewModel.teamList.observe(this){
            adapter.changeList(it)

            if(viewModel.flag){
                if(it.isEmpty()){
                    binding.rvTeam.visibility = View.GONE
                    binding.llEmpty.visibility = View.VISIBLE
                } else {
                    binding.rvTeam.visibility = View.VISIBLE
                    binding.llEmpty.visibility = View.GONE
                }
                viewModel.flag = false
            }
        }

        viewModel.errorEvent.observe(this){
            if(it.isNotEmpty()){
                CustomToast.createToast(requireContext(), it).show()
                viewModel.setErrorEvent()
            }
        }

        binding.btnNew.setOnClickListener {
            if(!myInfo!!.isUniversityEmailVerified){
               CustomDialog.getInstance(CustomDialog.DialogType.CERTIFY, null).apply {
                   setOnOKClickedListener {
                       (requireActivity() as MainActivity).replaceFragment(MyFragment())
                   }
               }.show(requireActivity().supportFragmentManager, "certify_dialog")
            } else if(myInfo!!.kakaoId == null){
                CustomDialog.getInstance(CustomDialog.DialogType.NULL_KAKAO_ID, null).apply {
                    setOnOKClickedListener {
                        (requireActivity() as MainActivity).replaceFragment(MyFragment())
                    }
                }.show(requireActivity().supportFragmentManager, "null_kakao_dialog")
            } else {
                (requireActivity() as MainActivity).replaceFragmentWithStack(TeamNewFragment())
            }
        }
    }

    private fun initRecyclerView() {
        adapter = TeamRvAdapter().apply {
            this.setItemClickListener(object : TeamRvAdapter.OnItemClickListener {
                override fun onClick(teamIntroduce: String, id: String, isLeader: Boolean) {
                    val teamInfo = adapter.dataList.find { it.id == id }

                    if(isLeader){ // 리더인 경우 메뉴 다이얼로그 보여줌
                        TeamMenuBottomSheetDialog.getInstance(teamInfo!!, id, viewModel).show(requireActivity().supportFragmentManager, "")
                    } else { // 멤버의 경우는 팀 나가기 다이얼로그 보여줌
                        val dialogType = if(teamInfo?.memberCount == teamInfo?.memberInfos?.size) CustomDialog.DialogType.TEAM_EXIT_PUBLISHED else CustomDialog.DialogType.TEAM_EXIT

                        CustomDialog.getInstance(dialogType, teamIntroduce).apply {
                            setOnOKClickedListener {
                                CoroutineScope(Dispatchers.IO).launch{
                                    val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                                    when(val res = LeaveTeamUseCase().leaveTeam(accessToken, id)){
                                        is Resource.Success -> {
                                            Log.i(TAG, "Leave Team: $teamIntroduce")
                                            launch(Dispatchers.Main){ isRefresh.value = true }
                                        }
                                        is Resource.Error -> {
                                            launch(Dispatchers.Main){
                                                CustomToast.createToast(this@TeamFragment.requireContext(), res.message).show()
                                            }
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        }.show(this@TeamFragment.requireActivity().supportFragmentManager, "leave_dialog")
                    }
                }
            })
            this.setRequestBtnClickListener(object : TeamRvAdapter.OnItemClickListener {
                override fun onClick(teamIntroduce: String, id: String, isLeader: Boolean) {
                    sendInvitation(id)
                }
            })
        }

        adapter.dataList = viewModel.teamList.value!!
        binding.rvTeam.adapter = adapter
        binding.rvTeam.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun sendInvitation(teamId: String){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = CreateInvitationLinkUseCase().createInvitationLink(accessToken, teamId)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        KakaoShareManager(requireContext()).sendInvitation(res.data)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, res.message)
                }
                else -> {}
            }
        }
    }
}