package com.studentcenter.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.domain.entity.meeting.MeetingListItemEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListMemberInfoEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListTeamEntity
import com.studentcenter.weave.domain.enums.AnimalType
import com.studentcenter.weave.domain.enums.TeamType
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.GetMeetingListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestViewModel: ViewModel() {
    private val getMeetingListUseCase = GetMeetingListUseCase()

    private val limit = 10
    private var receivingNext: String? = null
    private var requestingNext: String? = null

    private var _receiveData = MutableLiveData(listOf<MeetingListItemEntity>())
    val receiveData: LiveData<List<MeetingListItemEntity>>
        get() = _receiveData

    private var _requestData = MutableLiveData(listOf<MeetingListItemEntity>())
    val requestData: LiveData<List<MeetingListItemEntity>>
        get() = _requestData

    fun getReceiveData(){
        viewModelScope.launch(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = getMeetingListUseCase.getMeetingList(accessToken, TeamType.RECEIVING, receivingNext, limit)){
                is Resource.Success -> {
                    receivingNext = res.data.next
                    _receiveData.postValue(res.data.items)
                }
                is Resource.Error -> {
                    Log.e("GetReceiveData", res.message)
                }
                else -> {}
            }
        }
    }

    fun getRequestData(){
        viewModelScope.launch(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = getMeetingListUseCase.getMeetingList(accessToken, TeamType.REQUESTING, requestingNext, limit)){
                is Resource.Success -> {
                    requestingNext = res.data.next
                    _requestData.postValue(res.data.items)

                }
                is Resource.Error -> {
                    Log.e("GetRequestData", res.message)
                }
                else -> {}
            }
        }
    }

    private val testData = listOf(
        MeetingListItemEntity(
            id = "",
            teamType = TeamType.RECEIVING.toString(),
            status = "PENDING",
            createdAt = "",
            pendingEndAt = "2024-02-27T00:05:16.655Z",
            receivingTeam = MeetingListTeamEntity(
                id = "",
                teamIntroduce = "test",
                memberCount = 2,
                gender = "",
                memberInfos = listOf(
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "명지대학교", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    ),
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "인하대대학교", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "INFJ"
                    )
                )
            ),
            requestingTeam = MeetingListTeamEntity(
                id = "",
                teamIntroduce = "test",
                memberCount = 2,
                gender = "",
                memberInfos = listOf(
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "명지대", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    ),
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "인하대", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    )
                )
            ),
        ),
        MeetingListItemEntity(
            id = "",
            teamType = TeamType.RECEIVING.toString(),
            status = "PENDING",
            createdAt = "",
            pendingEndAt = "2024-02-27T00:05:16.655Z",
            receivingTeam = MeetingListTeamEntity(
                id = "",
                teamIntroduce = "test",
                memberCount = 3,
                gender = "",
                memberInfos = listOf(
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "명지대학교", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    ),
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "인하대대학교", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "INFJ"
                    ),
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "명지대학교", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    ),
                )
            ),
            requestingTeam = MeetingListTeamEntity(
                id = "",
                teamIntroduce = "test",
                memberCount = 3,
                gender = "",
                memberInfos = listOf(
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "명지대", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    ),
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "인하대", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    ),
                    MeetingListMemberInfoEntity(
                        id = "", userId = "", universityName = "명지대학교", birthYear = 2000, animalType = AnimalType.CAMEL.description, mbti = "ISFJ"
                    ),
                )
            ),
        )
    )
}