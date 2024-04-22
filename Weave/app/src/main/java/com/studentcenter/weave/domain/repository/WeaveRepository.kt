package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.weave.SubmitSuggestionReq
import okhttp3.ResponseBody
import retrofit2.Response

interface WeaveRepository {
    suspend fun submitSuggestion(accessToken: String, body: SubmitSuggestionReq): Response<ResponseBody>
}