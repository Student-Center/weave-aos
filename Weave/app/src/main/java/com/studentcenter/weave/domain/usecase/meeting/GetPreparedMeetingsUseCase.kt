package com.studentcenter.weave.domain.usecase.meeting

import com.studentcenter.weave.data.repositoryImpl.MeetingRepositoryImpl
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class GetPreparedMeetingsUseCase {
    private val meetingRepositoryImpl = MeetingRepositoryImpl()

    suspend fun getPreparedMeetings(accessToken: String, next: String?, limit: Int): Resource<PreparedMeetingEntity> {
        return try {
            val res = meetingRepositoryImpl.getPreparedMeetings("Bearer $accessToken", next, limit)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
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