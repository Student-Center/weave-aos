package com.studentcenter.weave.presentation.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.studentcenter.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.studentcenter.weave.data.remote.dto.user.SetMyHeightReq
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.enums.AnimalType
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.profile.GetMyInfoUseCase
import com.studentcenter.weave.domain.usecase.profile.ModifyMyMbtiUseCase
import com.studentcenter.weave.domain.usecase.profile.SetMyAnimalTypeUseCase
import com.studentcenter.weave.domain.usecase.profile.SetMyHeightUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    fun initMyInfo(){
        _univ.value = myInfo?.universityName ?: ""
        _major.value = myInfo?.majorName ?: ""
        _birthYear.value = myInfo?.birthYear.toString()
        _verified.value = myInfo?.isUniversityEmailVerified ?: false
        _ssill.value = myInfo?.sil ?: 0

        _profileImg.value = myInfo?.avatar ?: ""
        _mbti.value = myInfo?.mbti ?: ""
        _height.value = if(myInfo?.height == null || myInfo?.height == 0) "" else myInfo?.height.toString()
        _animal.value = getSubstring(AnimalType.values().find { it -> it.name == myInfo?.animalType }?.description)
    }



    fun setMyInfo(){
        _refresh.value = false

         viewModelScope.launch(Dispatchers.IO) {
            app.getUserDataStore().getLoginToken().collect {
                when(val res = getMyInfoUseCase.getMyInfo(it.accessToken)){
                    is Resource.Success -> {
                        launch(Dispatchers.Main){
                            runBlocking {
                                myInfo = MyInfoEntity(
                                    id = res.data.id,
                                    nickname = res.data.nickname,
                                    birthYear = res.data.birthYear,
                                    universityName = res.data.universityName,
                                    majorName = res.data.majorName,
                                    avatar = res.data.avatar,
                                    mbti = res.data.mbti,
                                    animalType = res.data.animalType,
                                    height = res.data.height,
                                    isUniversityEmailVerified = res.data.isUniversityEmailVerified,
                                    sil = res.data.sil
                                )
                            }

                            initMyInfo()
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
                        showErrorToastMsg(res.message)
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
                        showErrorToastMsg(res.message)
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
                        showErrorToastMsg(res.message)
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

    override fun onCleared() {
        super.onCleared()
        Log.i("MyViewModel", "cleared")
    }

    private fun showErrorToastMsg(msg: String){
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(app.applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }
}