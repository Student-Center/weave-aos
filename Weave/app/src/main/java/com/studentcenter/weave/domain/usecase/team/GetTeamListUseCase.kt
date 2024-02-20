package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.remote.dto.team.GetTeamListReq
import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.entity.team.GetTeamListEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource

class GetTeamListUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun getTeamList(accessToken: String, body: GetTeamListReq): Resource<GetTeamListEntity> {
        return try {
            val res = teamRepositoryImpl.getTeamList("Bearer $accessToken", body)

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