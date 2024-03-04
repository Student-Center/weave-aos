package com.studentcenter.weave.presentation.view.chat

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.databinding.FragmentMatchDetailBinding
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingItemEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.GetOtherTeamKakaoIdUseCase
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MatchDetailFragment(private val data: PreparedMeetingItemEntity): BaseFragment<FragmentMatchDetailBinding>(R.layout.fragment_match_detail) {
    private lateinit var adapter: MatchDetailRvAdapter

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)

        (requireActivity() as MainActivity).showLoadingDialog(requireContext())
        getKakaoId()
        binding.tvTeamTitle.text = data.otherTeam.teamIntroduce
        binding.tvTeamLocation.text = data.otherTeam.location

        binding.ibBack.setOnClickListener{
            (requireActivity() as MainActivity).supportFragmentManager.popBackStack()
        }

        binding.btnMove.setOnClickListener {
            (requireActivity() as MainActivity).supportFragmentManager.popBackStack()
        }
    }

    private fun getKakaoId(){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetOtherTeamKakaoIdUseCase().getOtherTeamKakaoId(accessToken, data.id)){
                is Resource.Success -> {
                    val members = data.otherTeam.memberInfos.listIterator()
                    while(members.hasNext()){
                        val member = members.next()
                        res.data.find { it.memberId == member.id }.let { member.kakaoId = it?.kakaoId ?: ""}
                    }

                    launch(Dispatchers.Main){
                        Handler(Looper.getMainLooper()).postDelayed({
                            (requireActivity() as MainActivity).dismissLoadingDialog()
                            setRv()
                        }, 1000)
                    }
                }
                is Resource.Error -> {
                    launch(Dispatchers.Main){
                        Toast.makeText(this@MatchDetailFragment.requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {}
            }
        }
    }

    private fun setRv(){
        adapter = MatchDetailRvAdapter().apply {
            setItemClickListener(object : MatchDetailRvAdapter.OnItemClickListener {
                override fun onClick() {
                    ReportBottomSheetDialog.getInstance().show(requireActivity().supportFragmentManager, "")
                }
            })
        }
        adapter.dataList = data.otherTeam.memberInfos
        binding.rvDetailProfile.adapter = adapter
        binding.rvDetailProfile.layoutManager = LinearLayoutManager(requireContext())
    }
}