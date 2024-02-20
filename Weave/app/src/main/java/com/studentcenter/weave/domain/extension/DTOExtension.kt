package com.studentcenter.weave.domain.extension

import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import com.studentcenter.weave.data.remote.dto.team.GetLocationRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamItem
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamMemberInfos
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
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
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.entity.team.GetMyTeamEntity
import com.studentcenter.weave.domain.entity.team.GetMyTeamItemEntity
import com.studentcenter.weave.domain.entity.team.GetMyTeamMemberInfoEntity
import com.studentcenter.weave.domain.entity.team.GetTeamListEntity
import com.studentcenter.weave.domain.entity.team.GetTeamListItemEntity
import com.studentcenter.weave.domain.entity.team.GetTeamListMemberEntity
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
    domainAddress = this.domainAddress,
    logoAddress = this.logoAddress
)

fun MajorsRes.asDomain() = MajorEntity(
    id = this.id,
    name = this.name
)

// User
fun GetMyInfoRes.asDomain() = MyInfoEntity(
    id, nickname, birthYear, universityName, majorName, avatar, mbti, animalType, height, isUniversityEmailVerified, sil
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
    userId, universityName, majorName, mbti, birthYear, role, animalType, height
)

fun GetTeamDetailRes.asDomain() = TeamDetailEntity(
    id, teamIntroduce, memberCount, location, gender, members = members.map { it.asDomain() }, status
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






