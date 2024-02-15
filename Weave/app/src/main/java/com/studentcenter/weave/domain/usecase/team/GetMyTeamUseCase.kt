package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.entity.team.GetMyTeamEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource

class GetMyTeamUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun getMyTeam(accessToken: String, next: String, limit: Int): Resource<GetMyTeamEntity> {
        return try {
            val res = teamRepositoryImpl.getMyTeam("Bearer $accessToken", next, limit)

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