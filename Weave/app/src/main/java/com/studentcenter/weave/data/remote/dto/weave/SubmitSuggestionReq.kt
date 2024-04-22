package com.studentcenter.weave.data.remote.dto.weave

import com.google.gson.annotations.SerializedName

data class SubmitSuggestionReq(
    @SerializedName("contents")
    val contents: String
)
