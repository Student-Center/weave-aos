package com.weave.weave.data.remote.dto.univ

import com.google.gson.annotations.SerializedName

data class MajorRes(

    @SerializedName("majors")
    val majors: List<MajorsRes>
)

data class MajorsRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
