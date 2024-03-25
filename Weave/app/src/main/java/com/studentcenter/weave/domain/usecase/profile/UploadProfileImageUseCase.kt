package com.studentcenter.weave.domain.usecase.profile

import com.studentcenter.weave.data.remote.dto.user.UploadCallbackReq
import com.studentcenter.weave.data.repositoryImpl.UserRepositoryImpl
import com.studentcenter.weave.domain.entity.profile.GetProfileUploadUrlEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import okhttp3.RequestBody
import org.json.JSONObject

class UploadProfileImageUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun getUploadUrl(accessToken: String, extension: String): Resource<GetProfileUploadUrlEntity> {
        return try {
            val res = userRepositoryImpl.getUploadUrl("Bearer $accessToken", extension)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
                } else {
                    Resource.Error("null data")
                }
            } else {
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun uploadImage(uploadUrl: String, file: RequestBody): Resource<Boolean> {
        return try {
            val res = userRepositoryImpl.uploadProfileImage(uploadUrl, file)

            if(res.isSuccessful){
                if(res.code() == 200){
                    Resource.Success(true)
                } else {
                    Resource.Error(res.code().toString())
                }
            } else {
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun uploadCallback(accessToken: String, body: UploadCallbackReq): Resource<Boolean> {
        return try {
            val res = userRepositoryImpl.uploadCallback("Bearer $accessToken", body)

            if(res.isSuccessful){
                if(res.code() == 204){
                    Resource.Success(true)
                } else {
                    Resource.Error(res.code().toString())
                }
            } else {
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}