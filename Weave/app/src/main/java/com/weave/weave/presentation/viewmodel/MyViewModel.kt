package com.weave.weave.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.weave.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.weave.weave.data.remote.dto.user.SetMyHeightReq
import com.weave.weave.domain.enums.AnimalType
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.domain.usecase.profile.GetMyInfoUseCase
import com.weave.weave.domain.usecase.profile.ModifyMyMbtiUseCase
import com.weave.weave.domain.usecase.profile.SetMyAnimalTypeUseCase
import com.weave.weave.domain.usecase.profile.SetMyHeightUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel: ViewModel() {
    private val getMyInfoUseCase = GetMyInfoUseCase()
    private val modifyMyMbtiUseCase = ModifyMyMbtiUseCase()
    private val setMyHeightUseCase = SetMyHeightUseCase()
    private val setMyAnimalTypeUseCase = SetMyAnimalTypeUseCase()

    private fun getSubstring(input: String?): String {
        return if(!input.isNullOrEmpty()){
            val index = input.indexOfFirst { it == ' ' }

            if (index != -1) {
                input.substring(index + 1)
            } else {
                ""
            }
        } else {
            ""
        }
    }

    fun setMyInfo(){
        _refresh.value = false

        viewModelScope.launch(Dispatchers.IO) {
            app.getUserDataStore().getLoginToken().collect {
                when(val res = getMyInfoUseCase.getMyInfo(it.accessToken)){
                    is Resource.Success -> {
                        launch(Dispatchers.Main){
                            _nick.value = res.data.nickname
                            _univ.value = "위브대학교"
                            _major.value = res.data.majorName
                            _birthYear.value = res.data.birthYear.toString()
                            _verified.value = res.data.isUniversityEmailVerified
                            _ssill.value = res.data.sil

                            _profileImg.value = res.data.avatar
                            _mbti.value = res.data.mbti
                            _height.value = if(res.data.height.toString() == "0") "" else res.data.height.toString()
                            _animal.value = getSubstring(AnimalType.values().find { it -> it.name == res.data.animalType }?.description)
                        }
                    }
                    is Resource.Error -> {
                        Log.e("MyViewModel", "setMyInfo: ${res.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    // ---------------------------------------------------------------------

    private var _saveBtn = MutableLiveData(false)
    val saveBtn: LiveData<Boolean>
        get() = _saveBtn

    fun setSaveBtn(p: Boolean){
        _saveBtn.value = p
    }

    private var _refresh = MutableLiveData(false)
    val refresh: LiveData<Boolean>
        get() = _refresh

    // UserInfo ------------------------------------------------------------
    private var _profileImg = MutableLiveData("")
    val profileImg: LiveData<String>
        get() = _profileImg

    fun setProfileImg(p: String){
        _profileImg.value = p
    }

    private var _ssill = MutableLiveData(0)
    val ssill: LiveData<Int>
        get() = _ssill

    private var _nick = MutableLiveData("")
    val nick: LiveData<String>
        get() = _nick

    private var _univ = MutableLiveData("")
    val univ: LiveData<String>
        get() = _univ

    private var _major = MutableLiveData("")
    val major: LiveData<String>
        get() = _major

    private var _birthYear = MutableLiveData("")
    val birthYear: LiveData<String>
        get() = _birthYear

    private var _verified = MutableLiveData(false)
    val verified: LiveData<Boolean>
        get() = _verified


    private var _mbti = MutableLiveData("ISFJ")
    val mbti: LiveData<String>
        get() = _mbti

    fun modifyMyMbti(){
        viewModelScope.launch(Dispatchers.IO){
            app.getUserDataStore().getLoginToken().collect {
                when(val res = modifyMyMbtiUseCase.modifyMyMbti(it.accessToken, ModifyMyMbtiReq("${line1.value}${line2.value}${line3.value}${line4.value}"))){
                    is Resource.Success -> {
                        launch(Dispatchers.Main){
                            _refresh.value = res.data
                        }
                    }
                    is Resource.Error -> {
                        Log.e("MyViewModel", "modifyMyMbit: ${res.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    private var _animal = MutableLiveData("")
    val animal: LiveData<String>
        get() = _animal

    fun setAnimal(){
        viewModelScope.launch(Dispatchers.IO){
            app.getUserDataStore().getLoginToken().collect {
                val animalType = AnimalType.values().find { type -> type.description.contains(animalBtn.value.toString()) }

                when(val res = setMyAnimalTypeUseCase.setMyAnimalType(it.accessToken, SetMyAnimalTypeReq(animalType.toString()))){
                    is Resource.Success -> {
                        launch(Dispatchers.Main){
                            _refresh.value = res.data
                            _animalBtn.value = ""
                        }
                    }
                    is Resource.Error -> {
                        Log.e("MyViewModel", "setMyAnimalType: ${res.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    private var _height = MutableLiveData("")
    val height: LiveData<String>
        get() = _height

    fun setMyHeight(p: String){
        viewModelScope.launch(Dispatchers.IO){
            app.getUserDataStore().getLoginToken().collect {
                when(val res = setMyHeightUseCase.setMyHeight(it.accessToken, SetMyHeightReq(p.toInt()))){
                    is Resource.Success -> {
                        launch(Dispatchers.Main){
                            _refresh.value = res.data
                        }
                    }
                    is Resource.Error -> {
                        Log.e("MyViewModel", "setMyHeight: ${res.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    // --------------------------------------------------------------------------

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

    fun setLineValue(line: Int, p: String) {
        when (line) {
            1 -> _line1.value = p
            2 -> _line2.value = p
            3 -> _line3.value = p
            4 -> _line4.value = p
        }

        if (line1.value != "" && line2.value != "" && line3.value != "" && line4.value != "") {
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