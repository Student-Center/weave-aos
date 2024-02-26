package com.studentcenter.weave.domain.usecase.meeting

import com.studentcenter.weave.data.repositoryImpl.MeetingRepositoryImpl
import com.studentcenter.weave.domain.entity.meeting.MeetingListEntity
import com.studentcenter.weave.domain.enums.TeamType
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource

class GetMeetingListUseCase {
    private val meetingRepositoryImpl = MeetingRepositoryImpl()

    suspend fun getMeetingList(accessToken: String, teamType: TeamType, next: String?, limit: Int): Resource<MeetingListEntity> {
        return try {
            val res = meetingRepositoryImpl.getMeetingList("Bearer $accessToken", teamType, next, limit)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                Resource.Error(res.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}