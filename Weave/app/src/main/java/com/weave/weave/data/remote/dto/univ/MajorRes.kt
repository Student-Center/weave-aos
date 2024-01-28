package com.weave.weave.data.remote.dto.univ

import com.google.gson.annotations.SerializedName

data class MajorRes(
    @SerializedName("majors")
    val majors: List<String>
)
