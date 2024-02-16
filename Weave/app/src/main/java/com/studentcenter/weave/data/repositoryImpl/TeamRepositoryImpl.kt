package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.TeamService
import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import com.studentcenter.weave.domain.repository.TeamRepository
import okhttp3.ResponseBody
import retrofit2.Response

class TeamRepositoryImpl: TeamRepository {
    private val service = RetrofitClient.getInstance().create(TeamService::class.java)

    override suspend fun createTeam(accessToken: String, body: CreateTeamReq): Response<ResponseBody> {
        return service.createTeam(accessToken, body)
    }

    override suspend fun getMyTeam(accessToken: String, next: String, limit: Int): Response<GetMyTeamRes> {
        return service.getMyTeam(accessToken, next, limit)
    }
}