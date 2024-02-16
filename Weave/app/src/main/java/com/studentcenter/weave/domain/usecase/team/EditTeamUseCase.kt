package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.remote.dto.team.EditTeamReq
import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource

class EditTeamUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun editTeam(accessToken: String, teamId: String, body: EditTeamReq): Resource<Boolean> {
        return try {
            val res = teamRepositoryImpl.editTeam("Bearer $accessToken", teamId, body)

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