package com.studentcenter.weave.domain.usecase.weave

import com.studentcenter.weave.data.remote.dto.weave.SubmitSuggestionReq
import com.studentcenter.weave.data.repositoryImpl.WeaveRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class SubmitSuggestionUseCase {
    private val weaveRepositoryImpl = WeaveRepositoryImpl()

    suspend fun submitSuggestion(accessToken: String, body: SubmitSuggestionReq): Resource<Boolean> {

        return try {
            val res = weaveRepositoryImpl.submitSuggestion("Bearer $accessToken", body)

            if(res.isSuccessful){
                if(res.code() == 201){
                    Resource.Success(true)
                } else {
                    Resource.Error(res.message() ?: "isSuccessful but error occurred")
                }
            } else {
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}