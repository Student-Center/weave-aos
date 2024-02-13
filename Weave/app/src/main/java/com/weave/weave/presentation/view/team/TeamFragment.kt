package com.weave.weave.presentation.view.team

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.weave.weave.R
import com.weave.weave.databinding.FragmentTeamBinding
import com.weave.weave.domain.entity.team.GetMyTeamItemEntity
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.viewmodel.TeamViewModel

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

        binding.btnNew.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, TeamNewFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.teamList.observe(this){
            Log.i(TAG, it.size.toString())
            adapter.changeList(it)
        }

        viewModel.isEmpty.observe(this){
            if(it){
                binding.rvTeam.visibility = View.GONE
                binding.llEmpty.visibility = View.VISIBLE
            } else {
                binding.rvTeam.visibility = View.VISIBLE
                binding.llEmpty.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        adapter = TeamRvAdapter().apply {
            this.setItemClickListener(object : TeamRvAdapter.OnItemClickListener {
                override fun onClick(title: String) {
                    TeamMenuBottomSheetDialog.getInstance(title, viewModel).show(requireActivity().supportFragmentManager, "")
                }
            })
        }

        val temp = viewModel.teamList.value!!.listIterator()
        val tempList = arrayListOf<GetMyTeamItemEntity>()
        while (temp.hasNext()) {
            tempList.add(temp.next())
        }

        adapter.dataList = tempList
        binding.rvTeam.adapter = adapter
        binding.rvTeam.layoutManager = LinearLayoutManager(requireContext())
    }
}