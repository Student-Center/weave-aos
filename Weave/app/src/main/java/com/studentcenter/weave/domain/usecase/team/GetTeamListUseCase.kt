package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.entity.team.GetTeamListEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class GetTeamListUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun getTeamList(accessToken: String, memberCount: Int?, youngestMemberBirthYear: Int, oldestMemberBirthYear: Int, preferredLocations: List<String>?, next: String?, limit: Int): Resource<GetTeamListEntity> {
        return try {
            val res = teamRepositoryImpl.getTeamList("Bearer $accessToken", memberCount, youngestMemberBirthYear, oldestMemberBirthYear, preferredLocations, next, limit)

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