package com.studentcenter.weave.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingItemEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.meeting.GetPreparedMeetingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MatchViewModel: ViewModel() {
    private val getPreparedMeetingsUseCase = GetPreparedMeetingsUseCase()
    private var loadingFlag = true
    var initFlag = false

    private var _errorEvent = MutableLiveData("")
    val errorEvent: LiveData<String>
        get() = _errorEvent

    fun setErrorEvent(){
        _errorEvent.value = ""
    }

    private val limit = 20
    private var next: String? = null

    private var _teamList = MutableLiveData(listOf<PreparedMeetingItemEntity>())
    val teamList: LiveData<List<PreparedMeetingItemEntity>>
        get() = _teamList

    fun getPreparedMeetings(){
        if(loadingFlag) {
            loadingFlag = false

            viewModelScope.launch(Dispatchers.IO) {
                initFlag = true
                val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                when (val res =
                    getPreparedMeetingsUseCase.getPreparedMeetings(accessToken, next, limit)) {
                    is Resource.Success -> {
                        launch(Dispatchers.Main) {
                            next = res.data.next
                            _teamList.postValue(res.data.items)
                            loadingFlag = true
                        }
                    }

                    is Resource.Error -> {
                        launch(Dispatchers.Main) {
                            loadingFlag = true
                            if (res.message == "내 미팅팀이 존재하지 않아요! 미팅팀에 참여해 주세요!") {
                                _teamList.postValue(listOf())
                            } else {
                                _errorEvent.value = res.message
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}