package com.studentcenter.weave.presentation.view.team

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.locations
import com.studentcenter.weave.databinding.FragmentMyTeamDetailBinding
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.GetTeamDetailUseCase
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.view.home.DetailRvAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TeamDetailFragment(private val teamId: String): BaseFragment<FragmentMyTeamDetailBinding>(R.layout.fragment_my_team_detail){
    private lateinit var adapter: DetailRvAdapter
    private var data = listOf<TeamDetailMemberEntity>()

    override fun init() {
        (requireContext() as MainActivity).setNaviVisible(false)

        (requireActivity() as MainActivity).showLoadingDialog(requireContext())
        initializeView()

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initializeView(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetTeamDetailUseCase().getTeamDetail(accessToken, teamId)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        data = res.data.members

                        binding.tvTeamTitle.text = res.data.teamIntroduce
                        binding.tvTeamLocation.text = locations?.find { it.name == res.data.location }?.displayName ?: ""

                        adapter = DetailRvAdapter()
                        adapter.dataList = data

                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.rvDetailProfile.adapter = adapter
                            binding.rvDetailProfile.layoutManager = LinearLayoutManager(requireContext())

                            (requireActivity() as MainActivity).dismissLoadingDialog()
                        }, 600)
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "TeamDetailFragment Error: ${res.message}")
                }
                else -> {}
            }
        }
    }
}