package com.studentcenter.weave.presentation.view.team

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.databinding.FragmentTeamBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.CreateInvitationLinkUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
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
                Toast.makeText(requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.initializeList()
        initRecyclerView()

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
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.setErrorEvent()
            }
        }

        binding.btnNew.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, TeamNewFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initRecyclerView() {
        adapter = TeamRvAdapter().apply {
            this.setItemClickListener(object : TeamRvAdapter.OnItemClickListener {
                override fun onClick(teamIntroduce: String, id: String, isLeader: Boolean) {
                    if(isLeader){ // 리더인 경우 메뉴 다이얼로그 보여줌
                        TeamMenuBottomSheetDialog.getInstance(teamIntroduce, id, viewModel).show(requireActivity().supportFragmentManager, "")
                    } else { // 멤버의 경우는 팀 나가기 다이얼로그 보여줌

                    }
                }
            })
            this.setRequestBtnClickListener(object : TeamRvAdapter.OnItemClickListener {
                override fun onClick(teamIntroduce: String, id: String, isLeader: Boolean) {
                    shareUrl(id)
                }
            })
        }

        adapter.dataList = viewModel.teamList.value!!
        binding.rvTeam.adapter = adapter
        binding.rvTeam.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun shareUrl(teamId: String){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = CreateInvitationLinkUseCase().createInvitationLink(accessToken, teamId)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        // Intent 생성
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "${BuildConfig.BASE_URL}invitations?code=${res.data}")
                            type = "text/plain"
                        }

                        startActivity(Intent.createChooser(sendIntent, "공유하기"))
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