package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource

class CreateInvitationLinkUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun createInvitationLink(accessToken: String, teamId: String): Resource<String> {
        return try {
            val res = teamRepositoryImpl.createInvitationLink("Bearer $accessToken", teamId)

            if(res.isSuccessful){
                val data = res.body()

                if(data != null){
                    Resource.Success(data.meetingTeamInvitationCode)
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