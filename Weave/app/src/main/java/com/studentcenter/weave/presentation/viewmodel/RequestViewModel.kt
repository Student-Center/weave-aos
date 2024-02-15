package com.studentcenter.weave.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studentcenter.weave.domain.entity.team.RequestTeamTestEntity
import com.studentcenter.weave.domain.entity.team.TeamMember

class RequestViewModel: ViewModel() {
    private var _receiveData = MutableLiveData(mutableListOf<RequestTeamTestEntity>())
    val receiveData: LiveData<MutableList<RequestTeamTestEntity>>
        get() = _receiveData

    private var _sendData = MutableLiveData(mutableListOf<RequestTeamTestEntity>())
    val sendData: LiveData<MutableList<RequestTeamTestEntity>>
        get() = _sendData

    fun getReceiveData(){
        // test data
        val sample = "https://i.namu.wiki/i/ukdzGn7-wELDzW3pwiHKTHwtniRYgksguvHsfD85nVYO51oyK44H-V7kSjTonIaOY6XiIXPCJza8ZVF3EjPUAw.webp"

        with(_receiveData.value!!){
            add(
                RequestTeamTestEntity(title = "Team 1", time = "2",
                listOf(
                    TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                    TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
                ))
            )
            add(
                RequestTeamTestEntity(title = "Team 2", time = "2",
                    listOf(
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
                    ))
            )
            add(
                RequestTeamTestEntity(title = "Team 3", time = "3",
                    listOf(
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
                    ))
            )
            add(
                RequestTeamTestEntity(title = "Team 4", time = "4",
                    listOf(
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ"),
                        TeamMember(url = sample, univ = "위브대•05", mbti = "ISFJ")
                    ))
            )
        }
    }

    fun getSendData(){
        // empty view test
    }
}