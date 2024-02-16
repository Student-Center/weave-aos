package com.studentcenter.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.domain.entity.team.GetMyTeamItemEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.DeleteTeamUseCase
import com.studentcenter.weave.domain.usecase.team.GetMyTeamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TeamViewModel: ViewModel() {
    private val getMyTeamUseCase = GetMyTeamUseCase()
    private val deleteTeamUseCase = DeleteTeamUseCase()

    private var _teamList = MutableLiveData<MutableList<GetMyTeamItemEntity>>(mutableListOf())
    val teamList: LiveData<MutableList<GetMyTeamItemEntity>>
        get() = _teamList

    var flag: Boolean = false

    private var _errorEvent = MutableLiveData("")
    val errorEvent: LiveData<String>
        get() = _errorEvent

    fun setErrorEvent(){
        _errorEvent.value = ""
    }

    fun initializeList(){
        _teamList.value!!.clear()

        viewModelScope.launch(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = getMyTeamUseCase.getMyTeam(accessToken, "", 10)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        _teamList.postValue(res.data.item.toMutableList())
                        flag = true
                    }
                }
                is Resource.Error -> {
                    Log.e("TeamViewModel", res.message)
                    launch(Dispatchers.Main){
                        _errorEvent.value = "팀 정보 로드 실패"
                    }
                }
                else -> {}
            }
        }
    }

    fun deleteTeam(teamId: String){
        viewModelScope.launch(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = deleteTeamUseCase.deleteTeam(accessToken, teamId)){
                is Resource.Success -> {
                    initializeList()
                }
                is Resource.Error -> {
                    Log.e("TeamViewModel", "deleteTeam Error: ${res.message}")
                    launch(Dispatchers.Main){
                        _errorEvent.value = "팀 삭제 실패"
                    }
                }
                else -> {}
            }
        }
    }


}