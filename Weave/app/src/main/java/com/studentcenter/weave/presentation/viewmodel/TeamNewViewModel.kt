package com.studentcenter.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.locations
import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.CreateTeamUseCase
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
        var result: Boolean

        runBlocking(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken
            val locName = locations?.find { it.displayName == location.value }?.name!!
            val body = CreateTeamReq(location = locName, memberCount = type.value!![0].digitToInt(), teamIntroduce = desc.value!!)

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
}