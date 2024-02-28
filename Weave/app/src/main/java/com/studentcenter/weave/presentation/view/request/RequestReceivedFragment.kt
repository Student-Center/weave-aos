package com.studentcenter.weave.presentation.view.request

import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentRequestReceivedBinding
import com.studentcenter.weave.domain.entity.meeting.MeetingListItemEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListMemberInfoEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListTeamEntity
import com.studentcenter.weave.domain.enums.TeamType
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.RequestViewModel

class RequestReceivedFragment(private val vm: RequestViewModel): BaseFragment<FragmentRequestReceivedBinding>(R.layout.fragment_request_received) {
    private lateinit var adapter: RequestReceivedRvAdapter
    private var initFlag = false

    override fun init() {
        binding.btnMove.setOnClickListener {
            val mainActivity = (requireContext() as MainActivity)
            mainActivity.binding.bottomNavi.selectedItemId = mainActivity.binding.bottomNavi.menu[2].itemId
        }

        vm.getReceiveData()

        adapter = RequestReceivedRvAdapter()
        binding.rvRequestReceived.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRequestReceived.adapter = adapter

        vm.receiveData.observe(this){
            adapter.changeList(vm.receiveData.value!!)
            if(initFlag) { rvVisibility() }
            else { initFlag = true }

            if(it.isNullOrEmpty()){
                binding.llEmpty.visibility = View.GONE
                binding.rvRequestReceived.visibility = View.VISIBLE

                adapter.changeList(
                    listOf(
                        MeetingListItemEntity(
                            id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                            requestingTeam = MeetingListTeamEntity(
                                id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                teamIntroduce = "테스트",
                                memberCount = 2,
                                gender = "string",
                                memberInfos = listOf(
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "ISFJ",
                                        birthYear = 2000,
                                        animalType = "CAT"
                                    ),
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "ENTJ",
                                        birthYear = 2001,
                                        animalType = "DOG"
                                    )
                                )
                            ),
                            receivingTeam = MeetingListTeamEntity(
                                id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                teamIntroduce = "string",
                                memberCount = 0,
                                gender = "string",
                                memberInfos = listOf(
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "ENTJ",
                                        birthYear = 2001,
                                        animalType = "DOG"
                                    ),
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "INTJ",
                                        birthYear = 2002,
                                        animalType = "CAT"
                                    )
                                )
                            ),
                            teamType = TeamType.RECEIVING.toString(),
                            status = "PENDING",
                            createdAt = "2024-02-28T16:06:16.729Z",
                            pendingEndAt = "2024-02-28T16:06:16.729Z"
                        ),
                        MeetingListItemEntity(
                            id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                            requestingTeam = MeetingListTeamEntity(
                                id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                teamIntroduce = "string",
                                memberCount = 2,
                                gender = "string",
                                memberInfos = listOf(
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "ISTP",
                                        birthYear = 2001,
                                        animalType = "DOG"
                                    ),
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "INTJ",
                                        birthYear = 1998,
                                        animalType = "CAT"
                                    )
                                )
                            ),
                            receivingTeam = MeetingListTeamEntity(
                                id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                teamIntroduce = "string",
                                memberCount = 2,
                                gender = "string",
                                memberInfos = listOf(
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "ISTP",
                                        birthYear = 2001,
                                        animalType = "DOG"
                                    ),
                                    MeetingListMemberInfoEntity(
                                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        userId = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                        universityName = "위브대",
                                        mbti = "INTJ",
                                        birthYear = 1998,
                                        animalType = "CAT"
                                    )
                                )
                            ),
                            teamType = TeamType.RECEIVING.toString(),
                            status = "PENDING",
                            createdAt = "2024-02-28T16:06:16.729Z",
                            pendingEndAt = "2024-02-28T16:06:16.729Z"
                        )
                    )
                )
            }
        }
    }

    private fun rvVisibility(){
        if(vm.receiveData.value!!.isEmpty()){
            binding.llEmpty.visibility = View.VISIBLE
            binding.rvRequestReceived.visibility = View.GONE
        } else {
            binding.llEmpty.visibility = View.GONE
            binding.rvRequestReceived.visibility = View.VISIBLE
        }
    }
}