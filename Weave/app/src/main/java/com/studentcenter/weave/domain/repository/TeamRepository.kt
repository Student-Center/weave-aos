package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.team.CreateInvitationLinkRes
import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.remote.dto.team.EditTeamReq
import com.studentcenter.weave.data.remote.dto.team.GetLocationsRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamByInvitationCodeRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamDetailRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamListRes
import okhttp3.ResponseBody
import retrofit2.Response

interface TeamRepository {

    suspend fun getTeamList(
        accessToken: String,
        memberCount: Int?,
        youngestMemberBirthYear: Int,
        oldestMemberBirthYear: Int,
        preferredLocations: List<String>?,
        next: String?,
        limit: Int
    ): Response<GetTeamListRes>

    suspend fun createTeam(accessToken: String, body: CreateTeamReq): Response<ResponseBody>

    suspend fun getMyTeam(accessToken: String, next: String?, limit: Int): Response<GetMyTeamRes>

    suspend fun getTeamDetail(accessToken: String, teamId: String): Response<GetTeamDetailRes>

    suspend fun deleteTeam(accessToken: String, teamId: String): Response<ResponseBody>

    suspend fun editTeam(accessToken: String, teamId: String, body: EditTeamReq): Response<ResponseBody>

    suspend fun getLocations(): Response<GetLocationsRes>

    suspend fun createInvitationLink(accessToken: String, teamId: String): Response<CreateInvitationLinkRes>

    suspend fun getTeamByInvitationCode(accessToken: String, invitationCode: String): Response<GetTeamByInvitationCodeRes>

    suspend fun postTeamByInvitationCode(accessToken: String, invitationCode: String): Response<ResponseBody>
}