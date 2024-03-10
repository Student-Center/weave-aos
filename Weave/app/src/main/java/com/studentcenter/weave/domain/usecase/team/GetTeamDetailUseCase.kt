package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.entity.team.TeamDetailEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class GetTeamDetailUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun getTeamDetail(accessToken: String, teamId: String): Resource<TeamDetailEntity> {
        return try {
            val res = teamRepositoryImpl.getTeamDetail("Bearer $accessToken", teamId)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
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