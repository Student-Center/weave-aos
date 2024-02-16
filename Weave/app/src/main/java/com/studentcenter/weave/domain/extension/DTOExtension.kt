package com.studentcenter.weave.domain.extension

import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import com.studentcenter.weave.data.remote.dto.team.GetLocationRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamItem
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamMemberInfos
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
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
import com.studentcenter.weave.domain.entity.team.LocationEntity

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
    item = this.item.map { it.asDomain() },
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




