package com.studentcenter.weave.domain.usecase.team

import android.util.Log
import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.entity.team.GetMyTeamEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource

class GetMyTeamUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun getMyTeam(accessToken: String, next: String?, limit: Int): Resource<GetMyTeamEntity> {
        return try {
            val res = teamRepositoryImpl.getMyTeam("Bearer $accessToken", next, limit)

            Log.d("TEST", "0")
            if(res.isSuccessful){
                val data = res.body()
                Log.d("TEST", "1")

                if(data != null){
                    Log.d("TEST", "2")
                    Resource.Success(data.asDomain())
                } else {
                    Log.d("TEST", "3")
                    Resource.Error("Received null data")
                }
            } else {
                Log.d("TEST", "4")
                Resource.Error(res.message())
            }
        } catch (e: Exception){
            Log.d("TEST", "5")
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}