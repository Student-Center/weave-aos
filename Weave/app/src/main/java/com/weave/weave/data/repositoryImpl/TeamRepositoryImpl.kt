package com.weave.weave.data.repositoryImpl

import com.weave.weave.data.remote.RetrofitClient
import com.weave.weave.data.remote.api.TeamService
import com.weave.weave.data.remote.dto.team.CreateTeamReq
import com.weave.weave.data.remote.dto.team.GetMyTeamReq
import com.weave.weave.data.remote.dto.team.GetMyTeamRes
import com.weave.weave.domain.repository.TeamRepository
import okhttp3.ResponseBody
import retrofit2.Response

class TeamRepositoryImpl: TeamRepository {
    private val service = RetrofitClient.getInstance().create(TeamService::class.java)

    override suspend fun createTeam(accessToken: String, body: CreateTeamReq): Response<ResponseBody> {
        return service.createTeam(accessToken, body)
    }

    override suspend fun getMyTeam(accessToken: String, body: GetMyTeamReq): Response<GetMyTeamRes> {
        return service.getMyTeam(accessToken, body)
    }
}