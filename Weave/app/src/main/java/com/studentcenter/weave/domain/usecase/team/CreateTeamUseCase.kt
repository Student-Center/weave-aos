package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

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
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}