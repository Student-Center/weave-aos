package com.weave.weave.domain.usecase.team

import com.weave.weave.data.remote.dto.team.CreateTeamReq
import com.weave.weave.data.repositoryImpl.TeamRepositoryImpl
import com.weave.weave.domain.usecase.Resource

class CreateTeamUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun createTeam(accessToken: String, body: CreateTeamReq): Resource<Boolean> {
        return try {
            val res = teamRepositoryImpl.createTeam("Bearer $accessToken", body)

            if(res.isSuccessful){
                if(res.code() == 201){
                    Resource.Success(true)
                } else {
                    Resource.Error(res.message() ?: "isSuccessful but error occurred")
                }
            } else {
                Resource.Error(res.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}