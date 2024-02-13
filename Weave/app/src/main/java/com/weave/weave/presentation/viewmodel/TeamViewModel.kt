package com.weave.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.domain.entity.team.GetMyTeamItemEntity
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.domain.usecase.team.GetMyTeamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TeamViewModel: ViewModel() {
    private val getMyTeamUseCase = GetMyTeamUseCase()

    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty


    private var _teamList = MutableLiveData<MutableList<GetMyTeamItemEntity>>(mutableListOf())
    val teamList: LiveData<MutableList<GetMyTeamItemEntity>>
        get() = _teamList

    fun initializeList(){
        _teamList.value!!.clear()

        viewModelScope.launch(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = getMyTeamUseCase.getMyTeam(accessToken, "", 0)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        _teamList.postValue(res.data.item.toMutableList())
                        _isEmpty.value = false
                    }
                }
                is Resource.Error -> {
                    Log.e("TeamViewModel", res.message)
                    _isEmpty.value = true
                }
                else -> {}
            }
        }
    }
}