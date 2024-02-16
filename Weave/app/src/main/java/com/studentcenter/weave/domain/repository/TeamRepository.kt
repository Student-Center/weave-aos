package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.remote.dto.team.EditTeamReq
import com.studentcenter.weave.data.remote.dto.team.GetLocationsRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamDetailRes
import okhttp3.ResponseBody
import retrofit2.Response

interface TeamRepository {
    suspend fun createTeam(accessToken: String, body: CreateTeamReq): Response<ResponseBody>

    suspend fun getMyTeam(accessToken: String, next: String, limit: Int): Response<GetMyTeamRes>

    suspend fun getTeamDetail(accessToken: String, teamId: String): Response<GetTeamDetailRes>

    suspend fun deleteTeam(accessToken: String, teamId: String): Response<ResponseBody>

    suspend fun editTeam(accessToken: String, teamId: String, body: EditTeamReq): Response<ResponseBody>

    suspend fun getLocations(): Response<GetLocationsRes>
}