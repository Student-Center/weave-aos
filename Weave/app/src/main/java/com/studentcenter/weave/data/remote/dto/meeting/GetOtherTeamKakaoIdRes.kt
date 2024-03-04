package com.studentcenter.weave.data.remote.dto.meeting

import com.google.gson.annotations.SerializedName

data class GetOtherTeamKakaoIdRes(
    @SerializedName("members")
    val members: List<GetOtherTeamKakaoIdMemberRes>
)

data class GetOtherTeamKakaoIdMemberRes(
    @SerializedName("memberId")
    val memberId: String,
    @SerializedName("kakaoId")
    val kakaoId: String
)
