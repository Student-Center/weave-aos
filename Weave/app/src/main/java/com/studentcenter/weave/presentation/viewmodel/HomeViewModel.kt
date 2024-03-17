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
    private var loadingFlag = true

    private var _data = MutableLiveData(mutableListOf<GetTeamListItemEntity>())
    val data: LiveData<MutableList<GetTeamListItemEntity>>
        get() = _data

    fun clearData(){
        next = null
        _data.value?.clear()
    }

    private var _isChangedFilter = MutableLiveData(false)
    val isChangedFilter: LiveData<Boolean>
        get() = _isChangedFilter

    var next: String? = null
    private val limit = 10

    fun getTeamList(){
        _isChangedFilter.value = false

        if(loadingFlag) {
            loadingFlag = false
            viewModelScope.launch(Dispatchers.IO) {
                val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                when (val res = getTeamListUseCase.getTeamList(
                    accessToken,
                    memberCount,
                    youngestMemberBirthYear,
                    oldestMemberBirthYear,
                    preferredLocations,
                    next,
                    limit
                )) {
                    is Resource.Success -> {
                        next = res.data.next

                        val newList = data.value?.toMutableList()
                        for (item in res.data.items) {
                            if (!newList?.contains(item)!!) {
                                newList.add(item)
                            }
                        }

                        _data.postValue(newList)
                        loadingFlag = true
                    }

                    is Resource.Error -> {
                        Log.e("HomeViewModel", res.message)
                        loadingFlag = true
                    }

                    else -> {}
                }
            }
        }
    }

    var memberCount: Int? = null
    var youngestMemberBirthYear = 2006
    var oldestMemberBirthYear = 1996
    var preferredLocations = listOf<String>()

    fun setFilter(count: Int?, youngest: Int, oldest: Int, locations: List<String>) {
        memberCount = count
        youngestMemberBirthYear = youngest
        oldestMemberBirthYear = oldest
        preferredLocations = locations

        _isChangedFilter.value = true
    }
}