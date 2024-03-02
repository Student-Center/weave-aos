package com.studentcenter.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.domain.entity.team.GetTeamListItemEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.GetTeamListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val getTeamListUseCase = GetTeamListUseCase()

    private var _data = MutableLiveData(mutableListOf<GetTeamListItemEntity>())
    val data: LiveData<MutableList<GetTeamListItemEntity>>
        get() = _data

    fun clearData(){
        _data.value?.clear()
    }

    private var _isChangedFilter = MutableLiveData(false)
    val isChangedFilter: LiveData<Boolean>
        get() = _isChangedFilter

    var next: String? = null
    private val limit = 10

    fun getTeamList(){
        _isChangedFilter.value = false

        viewModelScope.launch(Dispatchers.IO){
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = getTeamListUseCase.getTeamList(accessToken, _memberCount, _youngestMemberBirthYear, _oldestMemberBirthYear, _preferredLocations, next, limit)){
                is Resource.Success -> {
                    next = res.data.next
                    data.value?.addAll(res.data.items)

                    _data.postValue(data.value)
                }
                is Resource.Error -> {
                    Log.e("HomeViewModel", res.message)
                }
                else -> {}
            }
        }
    }

    private var _memberCount: Int? = null
    private var _youngestMemberBirthYear = 2006
    private var _oldestMemberBirthYear = 1996
    private var _preferredLocations = listOf<String>()

    fun setFilter(count: Int?, youngest: Int, oldest: Int, locations: List<String>){
        _memberCount = count
        _youngestMemberBirthYear = youngest
        _oldestMemberBirthYear = oldest
        _preferredLocations = locations

        _isChangedFilter.value = true
    }
}