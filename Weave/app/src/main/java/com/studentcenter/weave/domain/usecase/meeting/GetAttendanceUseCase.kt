package com.studentcenter.weave.domain.usecase.meeting

import com.studentcenter.weave.data.repositoryImpl.MeetingRepositoryImpl
import com.studentcenter.weave.domain.entity.meeting.IsCheckedEntity
import com.studentcenter.weave.domain.usecase.Resource

class GetAttendanceUseCase {
    private val meetingRepositoryImpl = MeetingRepositoryImpl()

    suspend fun getAttendances(accessToken: String, meetingId: String): Resource<List<IsCheckedEntity>> {
        return try {
            val res = meetingRepositoryImpl.getAttendances("Bearer $accessToken", meetingId)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    val lst = mutableListOf<IsCheckedEntity>()

                    val temp = data.meetingAttendances.listIterator()
                    while(temp.hasNext()){
                        val next = temp.next()
                        lst.add(IsCheckedEntity(next.memberId, next.attendance))
                    }
                    Resource.Success(lst.toList())
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