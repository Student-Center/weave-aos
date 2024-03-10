package com.studentcenter.weave.domain.extension

import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import com.studentcenter.weave.data.remote.dto.meeting.GetMeetingListRes
import com.studentcenter.weave.data.remote.dto.meeting.GetPreparedMeetingsRes
import com.studentcenter.weave.data.remote.dto.meeting.MeetingListItemRes
import com.studentcenter.weave.data.remote.dto.meeting.MeetingListMemberInfoRes
import com.studentcenter.weave.data.remote.dto.meeting.MeetingListTeamRes
import com.studentcenter.weave.data.remote.dto.meeting.PreparedMeetingsItemRes
import com.studentcenter.weave.data.remote.dto.meeting.PreparedMeetingsMemberRes
import com.studentcenter.weave.data.remote.dto.meeting.PreparedMeetingsOtherTeamRes
import com.studentcenter.weave.data.remote.dto.team.GetLocationRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamItem
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamMemberInfos
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamByInvitationCodeRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamDetailMemberRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamDetailRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamListItem
import com.studentcenter.weave.data.remote.dto.team.GetTeamListMemberInfo
import com.studentcenter.weave.data.remote.dto.team.GetTeamListRes
import com.studentcenter.weave.data.remote.dto.univ.MajorsRes
import com.studentcenter.weave.data.remote.dto.univ.UnivInfoRes
import com.studentcenter.weave.data.remote.dto.user.GetMyInfoRes
import com.studentcenter.weave.domain.entity.login.MajorEntity
import com.studentcenter.weave.domain.entity.login.TokenEntity
import com.studentcenter.weave.domain.entity.login.UniversityEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListItemEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListMemberInfoEntity
import com.studentcenter.weave.domain.entity.meeting.MeetingListTeamEntity
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingEntity
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingItemEntity
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingMemberEntity
import com.studentcenter.weave.domain.entity.meeting.PreparedMeetingOtherTeamEntity
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.entity.team.GetMyTeamEntity
import com.studentcenter.weave.domain.entity.team.GetMyTeamItemEntity
import com.studentcenter.weave.domain.entity.team.GetMyTeamMemberInfoEntity
import com.studentcenter.weave.domain.entity.team.GetTeamListEntity
import com.studentcenter.weave.domain.entity.team.GetTeamListItemEntity
import com.studentcenter.weave.domain.entity.team.GetTeamListMemberEntity
import com.studentcenter.weave.domain.entity.team.InvitationEntity
import com.studentcenter.weave.domain.entity.team.LocationEntity
import com.studentcenter.weave.domain.entity.team.TeamDetailEntity
import com.studentcenter.weave.domain.entity.team.TeamDetailMemberEntity

// Login
fun TokenRes.asDomain() = TokenEntity(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
)

fun UnivInfoRes.asDomain() = UniversityEntity(
    id = this.id,
    name = this.name,
    displayName = this.displayName,
    domainAddress = this.domainAddress,
    logoAddress = this.logoAddress
)

fun MajorsRes.asDomain() = MajorEntity(
    id = this.id,
    name = this.name
)

// User
fun GetMyInfoRes.asDomain() = MyInfoEntity(
    id, nickname, birthYear, universityName, majorName, avatar, mbti, animalType, height, kakaoId, isUniversityEmailVerified, sil
)

// Team
fun GetMyTeamRes.asDomain() = GetMyTeamEntity(
    item = this.items.map { it.asDomain() },
    next = this.next,
    total = this.total
)

fun GetMyTeamItem.asDomain() = GetMyTeamItemEntity(
    id, teamIntroduce, memberCount, location, memberInfos = memberInfos.map { it.asDomain() }
)

fun GetMyTeamMemberInfos.asDomain() = GetMyTeamMemberInfoEntity(
    id, universityName, mbti, birthYear, role, isMe
)

fun GetLocationRes.asDomain() = LocationEntity(
    name, displayName, isCapitalArea
)

fun GetTeamDetailMemberRes.asDomain() = TeamDetailMemberEntity(
    userId, universityName, majorName, mbti, birthYear, role, animalType, height, isUnivVerified
)

fun GetTeamDetailRes.asDomain() = TeamDetailEntity(
    id, teamIntroduce, memberCount, location, gender, members = members.map { it.asDomain() }, status, affinityScore
)

fun GetTeamListRes.asDomain() = GetTeamListEntity(
    items = items.map { it.asDomain() }, next, total
)

fun GetTeamListItem.asDomain() = GetTeamListItemEntity(
    id, teamIntroduce, memberCount, location, memberInfos = memberInfos.map { it.asDomain() }
)

fun GetTeamListMemberInfo.asDomain() = GetTeamListMemberEntity(
    id, universityName, mbti, birthYear, role
)

fun GetMeetingListRes.asDomain() = MeetingListEntity(
    items = items.map { it.asDomain() }, next, total
)

fun MeetingListItemRes.asDomain() = MeetingListItemEntity(
    id, requestingTeam = requestingTeam.asDomain(), receivingTeam = receivingTeam.asDomain(), teamType, status, createdAt, pendingEndAt
)

fun MeetingListTeamRes.asDomain() = MeetingListTeamEntity(
    id, teamIntroduce, memberCount, gender, memberInfos = memberInfos.map { it.asDomain() }
)

fun MeetingListMemberInfoRes.asDomain() = MeetingListMemberInfoEntity(
    id, userId, universityName, mbti, birthYear, animalType, checked = false
)

fun GetTeamByInvitationCodeRes.asDomain() = InvitationEntity(
    teamId, teamIntroduce, status
)

fun GetPreparedMeetingsRes.asDomain() = PreparedMeetingEntity(
    items = items.map { it.asDomain() }, next, total
)

fun PreparedMeetingsItemRes.asDomain() = PreparedMeetingItemEntity(
    id, memberCount, otherTeam = otherTeam.asDomain(), status, createdAt
)

fun PreparedMeetingsOtherTeamRes.asDomain() = PreparedMeetingOtherTeamEntity(
    id, teamIntroduce, memberCount, gender, location, memberInfos = memberInfos.map { it.asDomain() }
)

fun PreparedMeetingsMemberRes.asDomain() = PreparedMeetingMemberEntity(
    id, userId, universityName, majorName, mbti, birthYear, animalType, height, isUnivVerified, avatar, kakaoId = ""
)





