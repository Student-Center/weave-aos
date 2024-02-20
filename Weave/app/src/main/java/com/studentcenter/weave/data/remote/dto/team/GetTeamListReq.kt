package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class GetTeamListReq(
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("minBirthYear") // ex. 00년생
    val minBirthYear: Int,
    @SerializedName("maxBirthYear") // ex. 00년생
    val maxBirthYear: Int,
    @SerializedName("preferredLocations")
    val preferredLocations: List<String>,
    @SerializedName("next") // 이전에 조회한 목록의 마지막 ID
    val next: String,
    @SerializedName("limit") // 조회할 아이템 수
    val limit: Int
)
