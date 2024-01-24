package com.weave.weave.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weave.weave.core.Resource
import com.weave.weave.domain.entity.UserEntity
import com.weave.weave.domain.usecase.TestUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SignUpViewModel(): ViewModel(){
    // Test
    private val useCase = TestUseCase()

    private val _data = MutableLiveData<Resource<UserEntity>>()
    val data: LiveData<Resource<UserEntity>>
        get() = _data

    fun getUsers(){
        viewModelScope.launch {
            _data.value = useCase.getUsers()
        }
    }

    // 공통
    private var _nextBtn = MutableLiveData(false)
    val nextBtn: LiveData<Boolean>
        get() = _nextBtn

    fun setNextBtn(p: Boolean){
        _nextBtn.value = p
    }


    // STEP 1 - 성별
    private var _boyChecked = MutableLiveData(false)
    private var _girlChecked = MutableLiveData(false)

    val boyChecked: LiveData<Boolean>
        get() = _boyChecked

    val girlChecked: LiveData<Boolean>
        get() = _girlChecked

    fun setBoyChecked(p: Boolean){
        runBlocking {
            _boyChecked.value = p
        }

        if(boyChecked.value!! && girlChecked.value!!){
            _girlChecked.value = false
        }

        _nextBtn.value = boyChecked.value!! || girlChecked.value!!
    }

    fun setGirlChecked(p: Boolean){
        runBlocking {
            _girlChecked.value = p
        }

        if(boyChecked.value!! && girlChecked.value!!){
            _boyChecked.value = false
        }

        _nextBtn.value = boyChecked.value!! || girlChecked.value!!
    }

    // STEP2 - 생년
    private var _focusFlag = MutableLiveData(false)
    val focusFlag: LiveData<Boolean>
        get() = _focusFlag

    fun setFocusFlag(p: Boolean){
        _focusFlag.value = p
    }

    private var _year = MutableLiveData("")
    val year: LiveData<String>
        get() = _year

    fun setYear(p: Int){
        _year.value = p.toString()
    }

    // STEP3 - MBTI
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
            _nextBtn.value = true
        }
    }

    // STEP4 - 대학교
    private var _inputIsEmpty = MutableLiveData(true)
    val inputIsEmpty: LiveData<Boolean>
        get() = _inputIsEmpty

    fun setIsEmpty(p: Boolean){
        _inputIsEmpty.value = p
    }

    private var _currentUniv = MutableLiveData("")
    val currentUniv: LiveData<String>
        get() = _currentUniv

    fun setUniv(p: String){
        _currentUniv.value = p

        _nextBtn.value = !_currentUniv.value.isNullOrEmpty()
    }

    // STEP5 - 학과
    private var _currentMajor = MutableLiveData("")
    val currentMajor: LiveData<String>
        get() = _currentMajor

    fun setMajor(p: String){
        _currentMajor.value = p

        _nextBtn.value = !_currentMajor.value.isNullOrEmpty()
    }

    // 최종 결과
    fun getResult(): Boolean {
        println("Boy Checked: ${boyChecked.value}")
        println("Girl Checked: ${girlChecked.value}")
        println("Age: ${year.value}")
        println("MBTI: ${line1.value}${line2.value}${line3.value}${line4.value}")
        println("University: ${currentUniv.value}")
        println("Major: ${currentMajor.value}")

        if(boyChecked.value!! || girlChecked.value!!){
            if(!year.value.isNullOrEmpty()){
                if(line1.value != "" && line2.value != "" && line3.value != "" && line4.value != ""){
                    if(!currentUniv.value.isNullOrEmpty() && !currentMajor.value.isNullOrEmpty()){
                        return true
                    }
                }
            }
        }

        return false
    }

}