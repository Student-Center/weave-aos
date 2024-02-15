package com.studentcenter.weave.data.remote.dto.univ

import com.google.gson.annotations.SerializedName

data class UniversityRes(

    @SerializedName("universities")
    val universities: List<UniversitiesRes>
)

data class UniversitiesRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("domainAddress")
    val domainAddress: String,
    @SerializedName("logoAddress")
    val logoAddress: String
)
