package com.weave.weave.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weave.weave.domain.entity.home.TeamMember
import com.weave.weave.domain.entity.home.TeamTestEntity

class TeamViewModel: ViewModel() {
    private var _currentTeam = ""

    private var _teamList = MutableLiveData<MutableList<TeamTestEntity>>(mutableListOf())
    val teamList: LiveData<MutableList<TeamTestEntity>>
        get() = _teamList

    fun removeTeam(p: String){
        val removeItem = teamList.value!!.find { it.title == p }
        val temp = teamList.value!!
        temp.remove(removeItem)
        _teamList.postValue(temp)
    }

    fun addTeam(p: TeamTestEntity){
        val temp = teamList.value!!
        temp.add(p)
        _teamList.postValue(temp)
    }

    fun initializeList(){
        val sample = "https://i.namu.wiki/i/ukdzGn7-wELDzW3pwiHKTHwtniRYgksguvHsfD85nVYO51oyK44H-V7kSjTonIaOY6XiIXPCJza8ZVF3EjPUAw.webp"
        val temp = teamList.value!!
        _teamList.value!!.clear()
        with(temp){
            add(
                TeamTestEntity("4:4", "Team 1", "서울", listOf(
                    TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                    TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                )
                )
            )
            add(
                TeamTestEntity("3:3", "Team 2", "서울", listOf(
                    TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                )
                )
            )
            add(
                TeamTestEntity("2:2", "Team 3", "서울", listOf(
                    TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                )
                )
            )
        }

        _teamList.postValue(temp)
    }
}