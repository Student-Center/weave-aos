package com.studentcenter.weave.presentation.view.team

import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentTeamBinding
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.TeamViewModel

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
        }

        adapter.dataList = viewModel.teamList.value!!
        binding.rvTeam.adapter = adapter
        binding.rvTeam.layoutManager = LinearLayoutManager(requireContext())
    }
}