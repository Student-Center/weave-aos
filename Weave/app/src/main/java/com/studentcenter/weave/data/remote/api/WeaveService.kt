package com.studentcenter.weave.data.remote.api

import com.studentcenter.weave.data.remote.dto.weave.SubmitSuggestionReq
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WeaveService {
    @POST("/api/suggestions")
    suspend fun submitSuggestion(
        @Header("Authorization") accessToken: String,
        @Body body: SubmitSuggestionReq
    ): Response<ResponseBody>
}