package com.weave.weave.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    private var _saveBtn = MutableLiveData(false)
    val nextBtn: LiveData<Boolean>
        get() = _saveBtn

    fun setSaveBtn(p: Boolean){
        _saveBtn.value = p
    }

    // UserInfo
    private var _mbti = MutableLiveData("ISFJ")
    val mbti: LiveData<String>
        get() = _mbti

    fun setMbti(){
        _mbti.value = "${line1.value}${line2.value}${line3.value}${line4.value}"
    }

    private var _animal = MutableLiveData("ISFJ")
    val animal: LiveData<String>
        get() = _animal

    fun setAnimal(){
        _animal.value = animalBtn.value
    }


    // Edit-MBTI
    private var _line1 = MutableLiveData("")
    val line1: LiveData<String>
        get() = _line1

    private var _line2 = MutableLiveData("")
    val line2: LiveData<String>
        get() = _line2

    private var _line3 = MutableLiveData("")
    val line3: LiveData<String>
        get() = _line3

    private var _line4 = MutableLiveData("")
    val line4: LiveData<String>
        get() = _line4

    fun setLineValue(line: Int, p: String){
        when(line){
            1 -> _line1.value = p
            2 -> _line2.value = p
            3 -> _line3.value = p
            4 -> _line4.value = p
        }

        if(line1.value != "" && line2.value != "" && line3.value != "" && line4.value != ""){
            _saveBtn.value = true
        }
    }

    // Edit-Animal
    private var _animalBtn = MutableLiveData("")
    val animalBtn: LiveData<String>
        get() = _animalBtn

    fun setAnimalBtn(p: String){
        _animalBtn.value = p
    }
}