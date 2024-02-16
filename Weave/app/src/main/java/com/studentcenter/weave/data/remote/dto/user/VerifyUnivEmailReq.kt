package com.studentcenter.weave.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class VerifyUnivEmailReq(
    @SerializedName("universityEmail")
    val universityEmail: String,
    @SerializedName("verificationNumber")
    val verificationNumber: String
)
