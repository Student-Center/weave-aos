package com.weave.weave.domain.repository

import com.weave.weave.data.remote.dto.team.CreateTeamReq
import com.weave.weave.data.remote.dto.team.GetMyTeamReq
import com.weave.weave.data.remote.dto.team.GetMyTeamRes
import okhttp3.ResponseBody
import retrofit2.Response

interface TeamRepository {
    suspend fun createTeam(accessToken: String, body: CreateTeamReq): Response<ResponseBody>

    suspend fun getMyTeam(accessToken: String, body: GetMyTeamReq): Response<GetMyTeamRes>
}