package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.TeamService
import com.studentcenter.weave.data.remote.dto.team.CreateInvitationLinkRes
import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.remote.dto.team.EditTeamReq
import com.studentcenter.weave.data.remote.dto.team.GetLocationsRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamByInvitationCodeRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamDetailRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamListRes
import com.studentcenter.weave.domain.repository.TeamRepository
import okhttp3.ResponseBody
import retrofit2.Response

class TeamRepositoryImpl: TeamRepository {
    private val service = RetrofitClient.getInstance().create(TeamService::class.java)

    override suspend fun getTeamList(
        accessToken: String,
        memberCount: Int?,
        youngestMemberBirthYear: Int,
        oldestMemberBirthYear: Int,
        preferredLocations: List<String>?,
        next: String?,
        limit: Int
    ): Response<GetTeamListRes> {
        return service.getTeamList(accessToken, memberCount, youngestMemberBirthYear, oldestMemberBirthYear, preferredLocations, next, limit)
    }

    override suspend fun createTeam(accessToken: String, body: CreateTeamReq): Response<ResponseBody> {
        return service.createTeam(accessToken, body)
    }

    override suspend fun getMyTeam(accessToken: String, next: String?, limit: Int): Response<GetMyTeamRes> {
        return service.getMyTeam(accessToken, next, limit)
    }

    override suspend fun getTeamDetail(accessToken: String, teamId: String): Response<GetTeamDetailRes> {
        return service.getTeamDetail(accessToken, teamId)
    }

    override suspend fun deleteTeam(accessToken: String, teamId: String): Response<ResponseBody> {
        return service.deleteTeam(accessToken, teamId)
    }

    override suspend fun leaveTeam(accessToken: String, teamId: String): Response<ResponseBody> {
        return service.leaveTeam(accessToken, teamId)
    }

    override suspend fun editTeam(accessToken: String, teamId: String, body: EditTeamReq): Response<ResponseBody> {
        return service.editTeam(accessToken, teamId, body)
    }

    override suspend fun getLocations(): Response<GetLocationsRes> {
        return service.getLocations()
    }

    override suspend fun createInvitationLink(
        accessToken: String,
        teamId: String
    ): Response<CreateInvitationLinkRes> {
        return service.createInvitationLink(accessToken, teamId)
    }

    override suspend fun getTeamByInvitationCode(
        accessToken: String,
        invitationCode: String
    ): Response<GetTeamByInvitationCodeRes> {
        return service.getTeamByInvitationCode(accessToken, invitationCode)
    }

    override suspend fun enterTeamByInvitationCode(
        accessToken: String,
        invitationCode: String
    ): Response<ResponseBody> {
        return service.enterTeamByInvitationCode(accessToken, invitationCode)
    }
}