package com.weave.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.data.remote.dto.team.CreateTeamReq
import com.weave.weave.domain.enums.Location
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.domain.usecase.team.CreateTeamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TeamNewViewModel: ViewModel() {
    private val createTeamUseCase = CreateTeamUseCase()

    private var _nextBtn = MutableLiveData(false)
    val nextBtn: LiveData<Boolean>
        get() = _nextBtn

    private var _type = MutableLiveData("")
    val type: LiveData<String>
        get() = _type

    fun setType(p: String){
        _type.value = p
    }

    private var _isCapital = MutableLiveData("")
    val isCapital: LiveData<String>
        get() = _isCapital

    fun setIsCapital(p: String){
        _isCapital.value = p
    }

    private var _location = MutableLiveData("")
    val location: LiveData<String>
        get() = _location

    fun setLocation(p: String){
        _location.value = p
    }

    private var _desc = MutableLiveData("")
    val desc: LiveData<String>
        get() = _desc

    fun setDesc(p: String){
        _desc.value = p
        _nextBtn.value = desc.value?.length in 1..10
    }

    fun createTeam(): Boolean{
        Log.d("VM", "미팅 유형: ${type.value} / 지역: ${location.value} / 한 줄 소개: ${desc.value}")
        var result: Boolean

        runBlocking(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken
            val location = Location.values().find { it -> it.value == location.value }.toString()
            val body = CreateTeamReq(location = location, memberCount = type.value!![0].digitToInt(), teamIntroduce = desc.value!!)

            result = when(val res = createTeamUseCase.createTeam(accessToken, body)){
                is Resource.Success -> {
                    true
                }

                is Resource.Error -> {
                    Log.e("TeamNewViewModel", res.message)
                    false
                }

                else -> { false }
            }
        }

        return result
    }

    private var _chipsVisible = MutableLiveData(false)
    val chipsVisible: LiveData<Boolean>
        get() = _chipsVisible

    fun setChipsVisible(p: Boolean){
        _chipsVisible.value = p
    }
}