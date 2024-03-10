package com.studentcenter.weave.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.domain.entity.meeting.MeetingListItemEntity
import com.studentcenter.weave.domain.enums.TeamType
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.GetMeetingListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RequestViewModel: ViewModel() {
    private val getMeetingListUseCase = GetMeetingListUseCase()
    private var loadingFlag = true

    private val limit = 20
    private var receivingNext: String? = null
    private var requestingNext: String? = null

    private var _receiveData = MutableLiveData(listOf<MeetingListItemEntity>())
    val receiveData: LiveData<List<MeetingListItemEntity>>
        get() = _receiveData

    private var _requestData = MutableLiveData(listOf<MeetingListItemEntity>())
    val requestData: LiveData<List<MeetingListItemEntity>>
        get() = _requestData

    fun getReceiveData(){
        if(loadingFlag) {
            loadingFlag = false
            viewModelScope.launch(Dispatchers.IO) {
                val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                when (val res = getMeetingListUseCase.getMeetingList(
                    accessToken,
                    TeamType.RECEIVING,
                    receivingNext,
                    limit
                )) {
                    is Resource.Success -> {
                        receivingNext = res.data.next

                        val newList = receiveData.value?.toMutableList()
                        for (item in res.data.items) {
                            if (!newList?.contains(item)!!) {
                                newList.add(item)
                            }
                        }

                        _receiveData.postValue(newList)
                        loadingFlag = true
                    }

                    is Resource.Error -> {
                        _receiveData.postValue(listOf())
                        loadingFlag = true
                    }

                    else -> {}
                }
            }
        }
    }

    fun getRequestData(){
        if(loadingFlag) {
            loadingFlag = false
            viewModelScope.launch(Dispatchers.IO) {
                val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                when (val res = getMeetingListUseCase.getMeetingList(
                    accessToken,
                    TeamType.REQUESTING,
                    requestingNext,
                    limit
                )) {
                    is Resource.Success -> {
                        requestingNext = res.data.next

                        val newList = requestData.value?.toMutableList()
                        for (item in res.data.items) {
                            if (!newList?.contains(item)!!) {
                                newList.add(item)
                            }
                        }

                        _requestData.postValue(newList)
                        loadingFlag = true
                    }

                    is Resource.Error -> {
                        _requestData.postValue(listOf())
                        loadingFlag = true
                    }

                    else -> {}
                }
            }
        }
    }
}