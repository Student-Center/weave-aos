package com.studentcenter.weave.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class GetUploadUrlRes(
    @SerializedName("uploadUrl")
    val uploadUrl: String
)
