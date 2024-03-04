package com.studentcenter.weave.domain.usecase.meeting

import com.studentcenter.weave.data.repositoryImpl.MeetingRepositoryImpl
import com.studentcenter.weave.domain.entity.meeting.OtherTeamKakaoIdEntity
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class GetOtherTeamKakaoIdUseCase {
    private val meetingRepositoryImpl = MeetingRepositoryImpl()

    suspend fun getOtherTeamKakaoId(accessToken: String, meetingId: String): Resource<List<OtherTeamKakaoIdEntity>> {
        return try {
            val res = meetingRepositoryImpl.getOtherTeamKakaoId("Bearer $accessToken", meetingId)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    val lst = data.members.listIterator()
                    val temp = mutableListOf<OtherTeamKakaoIdEntity>()
                    while(lst.hasNext()){
                        val item = lst.next()
                        temp.add(OtherTeamKakaoIdEntity(item.memberId, item.kakaoId))
                    }

                    Resource.Success(temp.toList())
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