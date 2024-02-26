package com.studentcenter.weave.presentation.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class TimeUtil {
    fun getRemainingTimeMessage(endTimeString: String): String {
        val currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val endTime = ZonedDateTime.parse(endTimeString)

        val remainingTime = Duration.between(currentTime, endTime.toLocalDateTime())

        val hours = remainingTime.toHours()
        val minutes = remainingTime.toMinutes() % 60

        return if (hours > 0) {
            "${hours}시간 뒤에 사라져요!"
        } else {
            "${minutes}분 뒤에 사라져요!"
        }
    }
}