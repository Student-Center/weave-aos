package com.studentcenter.weave.domain.usecase.meeting

import com.studentcenter.weave.data.repositoryImpl.MeetingRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class AttendMeetingUseCase {
    private val meetingRepositoryImpl = MeetingRepositoryImpl()

    suspend fun attendMeeting(accessToken: String, meetingId: String): Resource<Boolean> {
        return try {
            val res = meetingRepositoryImpl.attendMeeting("Bearer $accessToken", meetingId)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(true)
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}