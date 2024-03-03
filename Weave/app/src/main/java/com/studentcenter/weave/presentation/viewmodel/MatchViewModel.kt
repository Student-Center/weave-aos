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
    var initFlag = false

    private var _errorEvent = MutableLiveData("")
    val errorEvent: LiveData<String>
        get() = _errorEvent

    fun setErrorEvent(){
        _errorEvent.value = ""
    }

    private val limit = 10
    private var next: String? = null

    private var _teamList = MutableLiveData(listOf<PreparedMeetingItemEntity>())
    val teamList: LiveData<List<PreparedMeetingItemEntity>>
        get() = _teamList

    fun getPreparedMeetings(){
        viewModelScope.launch(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = getPreparedMeetingsUseCase.getPreparedMeetings(accessToken, next, limit)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        next = res.data.next
                        initFlag = true
                        _teamList.postValue(res.data.items)
                    }
                }
                is Resource.Error -> {
                    launch(Dispatchers.Main) {
                        _errorEvent.value = res.message
                    }
                }
                else -> {}
            }
        }
    }
}