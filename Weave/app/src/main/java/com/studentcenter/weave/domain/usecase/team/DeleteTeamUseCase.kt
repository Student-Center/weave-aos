package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource

class DeleteTeamUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun deleteTeam(accessToken: String, teamId: String): Resource<Boolean> {
        return try {
            val res = teamRepositoryImpl.deleteTeam("Bearer $accessToken", teamId)

            if(res.isSuccessful){
                if(res.code() == 204){
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