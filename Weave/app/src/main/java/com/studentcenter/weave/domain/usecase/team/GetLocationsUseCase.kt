package com.studentcenter.weave.domain.usecase.team

import com.studentcenter.weave.data.repositoryImpl.TeamRepositoryImpl
import com.studentcenter.weave.domain.entity.team.LocationEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class GetLocationsUseCase {
    private val teamRepositoryImpl = TeamRepositoryImpl()

    suspend fun getLocations(): Resource<List<LocationEntity>> {
        return try {
            val res = teamRepositoryImpl.getLocations()

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    val temp = mutableListOf<LocationEntity>()
                    val locations = data.locations.listIterator()
                    while(locations.hasNext()){
                        temp.add(locations.next().asDomain())
                    }
                    Resource.Success(temp)
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