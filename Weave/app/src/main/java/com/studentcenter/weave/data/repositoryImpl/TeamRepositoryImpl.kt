package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.TeamService
import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.remote.dto.team.EditTeamReq
import com.studentcenter.weave.data.remote.dto.team.GetLocationsRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamDetailRes
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

    override suspend fun getTeamDetail(accessToken: String, teamId: String): Response<GetTeamDetailRes> {
        return service.getTeamDetail(accessToken, teamId)
    }

    override suspend fun deleteTeam(accessToken: String, teamId: String): Response<ResponseBody> {
        return service.deleteTeam(accessToken, teamId)
    }

    override suspend fun editTeam(accessToken: String, teamId: String, body: EditTeamReq): Response<ResponseBody> {
        return service.editTeam(accessToken, teamId, body)
    }

    override suspend fun getLocations(): Response<GetLocationsRes> {
        return service.getLocations()
    }
}