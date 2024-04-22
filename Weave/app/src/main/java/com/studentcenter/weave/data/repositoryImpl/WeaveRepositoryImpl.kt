package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.WeaveService
import com.studentcenter.weave.data.remote.dto.weave.SubmitSuggestionReq
import com.studentcenter.weave.domain.repository.WeaveRepository
import okhttp3.ResponseBody
import retrofit2.Response

class WeaveRepositoryImpl: WeaveRepository {
    private val service = RetrofitClient.getInstance().create(WeaveService::class.java)
    override suspend fun submitSuggestion(
        accessToken: String,
        body: SubmitSuggestionReq
    ): Response<ResponseBody> {
        return service.submitSuggestion(accessToken, body)
    }

}