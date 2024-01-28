package com.weave.weave.data.remote.dto.univ

import com.google.gson.annotations.SerializedName

data class UniversityRes(

    @SerializedName("universities")
    val universities: List<String>
)
