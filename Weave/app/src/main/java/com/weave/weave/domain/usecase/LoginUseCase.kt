package com.weave.weave.domain.usecase

import com.google.gson.Gson
import com.weave.weave.data.remote.dto.auth.LoginTokenReq
import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import com.weave.weave.data.repositoryImpl.AuthRepositoryImpl
import com.weave.weave.data.repositoryImpl.UnivRepositoryImpl
import com.weave.weave.data.repositoryImpl.UserRepositoryImpl
import com.weave.weave.domain.entity.login.MajorEntity
import com.weave.weave.domain.entity.login.RegisterTokenEntity
import com.weave.weave.domain.entity.login.TokenEntity
import com.weave.weave.domain.entity.login.UniversityEntity
import com.weave.weave.domain.extension.asDomain

class LoginUseCase {
    private val authRepositoryImpl = AuthRepositoryImpl()
    private val userRepositoryImpl = UserRepositoryImpl()
    private val univRepositoryImpl = UnivRepositoryImpl()

    suspend fun refreshLoginToken(refreshTokenReq: RefreshTokenReq): Resource<TokenEntity> {
        return try {
            val res = authRepositoryImpl.refreshLoginToken(refreshTokenReq)

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

    suspend fun login(provider: String, idToken: LoginTokenReq): Resource<TokenEntity> {
        return try {
            val res = authRepositoryImpl.login(provider, idToken)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                if(res.code() == 401){
                    val errorBody = res.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterTokenEntity::class.java)
                    Resource.Error("registerToken $errorResponse")
                } else {
                    Resource.Error(res.message())
                }
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Resource<TokenEntity>{
        return try {
            val res = userRepositoryImpl.registerUser(registerToken, userInfo)

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

    suspend fun getUnivList(): Resource<UniversityEntity>{
        return try {
            val res = univRepositoryImpl.findAllUniv()

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

    suspend fun getMajorList(univName: String): Resource<MajorEntity>{
        return try {
            val res = univRepositoryImpl.getAllMajor(univName)

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