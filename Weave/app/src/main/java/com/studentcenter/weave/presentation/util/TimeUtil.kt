package com.studentcenter.weave.presentation.util

import android.util.Log
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class TimeUtil {
    fun getRemainingTimeMessage(endTimeString: String): String {
        val currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        var parsedDateTime: LocalDateTime? = null

        try {
            parsedDateTime = LocalDateTime.parse(endTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        } catch (e: DateTimeParseException) {
            try {
                parsedDateTime = LocalDateTime.parse(endTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"))
            } catch (e: DateTimeParseException) {
                Log.i("TimeUtil", "날짜 형식이 유효하지 않습니다.")
            }
        }

        Log.i("TEST", "$currentTime / $parsedDateTime")


        val remainingTime = Duration.between(currentTime, parsedDateTime)

        val hours = remainingTime.toHours()
        val minutes = remainingTime.toMinutes() % 60

        return if (hours > 0) {
            "${hours}시간 뒤에 사라져요!"
        } else {
            "${minutes}분 뒤에 사라져요!"
        }
    }
}