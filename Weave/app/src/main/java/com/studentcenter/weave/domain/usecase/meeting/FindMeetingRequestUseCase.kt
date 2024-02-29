package com.studentcenter.weave.domain.usecase.meeting

import com.studentcenter.weave.data.repositoryImpl.MeetingRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class FindMeetingRequestUseCase {
    private val meetingRepositoryImpl = MeetingRepositoryImpl()

    suspend fun findMeetingRequest(accessToken: String, receivingTeamId: String): Resource<Boolean> {
        return try {
            val res = meetingRepositoryImpl.findMeetingRequest("Bearer $accessToken", receivingTeamId)

            if(res.isSuccessful){
                val data = res.body()
                if(data?.meeting != null){
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