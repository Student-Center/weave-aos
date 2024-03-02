package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class LeaveTeamUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun leaveTeam(accessToken: String, teamId: String): Resource<Boolean> {
        return try {
            val res = teamRepositoryImpl.leaveTeam("Bearer $accessToken", teamId)

            if(res.isSuccessful){
                if(res.code() == 204){
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