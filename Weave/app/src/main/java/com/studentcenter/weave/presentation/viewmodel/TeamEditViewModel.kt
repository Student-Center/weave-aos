package com.studentcenter.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studentcenter.weave.core.GlobalApplication
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.team.EditTeamReq
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.EditTeamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TeamEditViewModel: ViewModel() {
    private val editTeamUseCase = EditTeamUseCase()

    private var _nextBtn = MutableLiveData(false)
    val nextBtn: LiveData<Boolean>
        get() = _nextBtn

    var teamId: String = ""

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

    fun editTeam(): Boolean{
        var result: Boolean

        runBlocking(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken
            Log.i("TEST", location.value.toString())
            val locName = GlobalApplication.locations?.find { it.displayName == location.value }?.name!!
            val body = EditTeamReq(location = locName, memberCount = type.value!![0].digitToInt(), teamIntroduce = desc.value!!)

            result = when(val res = editTeamUseCase.editTeam(accessToken, teamId, body)){
                is Resource.Success -> {
                    true
                }

                is Resource.Error -> {
                    Log.e("TeamEditViewModel", res.message)
                    false
                }

                else -> { false }
            }
        }

        return result
    }

    // 팀원 모두 채워진 경우 수정 불가 코멘트
    private var _chipsVisible = MutableLiveData(false)
    val chipsVisible: LiveData<Boolean>
        get() = _chipsVisible

    fun setChipsVisible(p: Boolean){
        _chipsVisible.value = p
    }
}