package com.studentcenter.weave.presentation.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel: ViewModel() {
    private var _isFinish = MutableLiveData(false)
    val isFinish: LiveData<Boolean>
        get() = _isFinish

    private val timer = object : CountDownTimer(1 * 60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val minutes = millisUntilFinished / 1000 / 60
            val seconds = (millisUntilFinished / 1000) % 60
            _timeText.value = String.format("%02d:%02d", minutes, seconds)
            Log.i("Timer", timeText.value.toString())
        }

        override fun onFinish() {
            _isFinish.value = true
        }
    }

    fun startTimer(){
        timer.start()
    }

    fun cancelTimer(){
        timer.cancel()
    }

    fun resetTimer(){
        timer.cancel()
        _isFinish.value = false
        _timeText.value = "05:00"
        timer.start()
    }

    private var _timeText = MutableLiveData("05:00")
    val timeText: LiveData<String>
        get() = _timeText
}