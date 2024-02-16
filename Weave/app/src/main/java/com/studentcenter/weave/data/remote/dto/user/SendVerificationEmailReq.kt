package com.studentcenter.weave.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class SendVerificationEmailReq(
    @SerializedName("universityEmail")
    val universityEmail: String
)
