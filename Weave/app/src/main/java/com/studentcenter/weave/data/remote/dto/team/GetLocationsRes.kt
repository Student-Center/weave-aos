package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class GetLocationsRes(
    @SerializedName("locations")
    val locations: List<GetLocationRes>
)

data class GetLocationRes(
    @SerializedName("name")
    val name: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("isCapitalArea")
    val isCapitalArea: Boolean
)
