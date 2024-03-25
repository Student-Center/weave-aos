package com.studentcenter.weave.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UploadCallbackReq(
    @SerializedName("imageId")
    val imageId: String,
    @SerializedName("extension")
    val extension: String
)
