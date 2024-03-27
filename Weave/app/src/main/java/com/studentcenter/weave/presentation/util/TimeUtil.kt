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
        val parsedDateTime = parseDateTime(endTimeString)

        if (parsedDateTime == null) {
            Log.i("TimeUtil", "날짜 형식이 유효하지 않습니다.")
            return ""
        }

        val remainingTime = Duration.between(currentTime, parsedDateTime)
        val hours = remainingTime.toHours()
        val minutes = remainingTime.toMinutes() % 60

        return if (hours > 0) {
            "${hours}시간 뒤에 사라져요!"
        } else {
            "${minutes}분 뒤에 사라져요!"
        }
    }

    private fun parseDateTime(endTimeString: String): LocalDateTime? {
        val patterns = arrayOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SS",
            "yyyy-MM-dd'T'HH:mm:ss.S",
            "yyyy-MM-dd'T'HH:mm:ss"
        )

        for (pattern in patterns) {
            try {
                return if (pattern.endsWith("Z")) {
                    LocalDateTime.parse(endTimeString, DateTimeFormatter.ofPattern(pattern))
                } else {
                    LocalDateTime.parse(endTimeString.replace("Z", ""), DateTimeFormatter.ofPattern(pattern))
                }
            } catch (e: DateTimeParseException) {
                Log.i("TIME_UTIL", "$pattern -> 실패")
            }
        }
        return null
    }
}
