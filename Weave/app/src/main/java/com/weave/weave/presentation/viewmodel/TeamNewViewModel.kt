package com.weave.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TeamNewViewModel: ViewModel() {
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

    fun getResult() {
        Log.d("VM", "미팅 유형: ${type.value} / 지역: ${location.value} / 한 줄 소개: ${desc.value}")
    }

    private var _chipsVisible = MutableLiveData(false)
    val chipsVisible: LiveData<Boolean>
        get() = _chipsVisible

    fun setChipsVisible(p: Boolean){
        _chipsVisible.value = p
    }
}