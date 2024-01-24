package com.weave.weave.data.repositoryImpl

import com.weave.weave.core.Resource
import com.weave.weave.core.RetrofitClient
import com.weave.weave.data.remote.api.TestService
import com.weave.weave.domain.entity.UserEntity
import com.weave.weave.domain.extension.asDomain
import com.weave.weave.domain.repository.TestRepository

class TestRepositoryImpl() : TestRepository {
    private val service = RetrofitClient.getInstance().create(TestService::class.java)

    override suspend fun getUsers(): Resource<UserEntity> {
        return try {
            val response = service.getUsers()


            if(response.isSuccessful){
                val data = response.body()
                if(data != null){
                    val entity = data.asDomain()
                    Resource.Success(entity)
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                // API 호출은 성공했지만 서버에서 오류 응답을 받은 경우 Error 상태로 반환
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}