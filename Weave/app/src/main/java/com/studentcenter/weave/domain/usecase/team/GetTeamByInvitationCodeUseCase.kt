package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.entity.team.InvitationEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class GetTeamByInvitationCodeUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun getTeamByInvitationCode(accessToken: String, code: String): Resource<InvitationEntity> {
        return try {
            val res = teamRepositoryImpl.getTeamByInvitationCode("Bearer $accessToken", code)

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