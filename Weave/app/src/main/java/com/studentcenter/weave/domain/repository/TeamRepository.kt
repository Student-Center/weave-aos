package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import okhttp3.ResponseBody
import retrofit2.Response

interface TeamRepository {
    suspend fun createTeam(accessToken: String, body: CreateTeamReq): Response<ResponseBody>

    suspend fun getMyTeam(accessToken: String, next: String, limit: Int): Response<GetMyTeamRes>
}