package com.weave.weave.domain.extension

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.team.GetMyTeamItem
import com.weave.weave.data.remote.dto.team.GetMyTeamMemberInfos
import com.weave.weave.data.remote.dto.team.GetMyTeamRes
import com.weave.weave.data.remote.dto.univ.MajorsRes
import com.weave.weave.data.remote.dto.univ.UniversitiesRes
import com.weave.weave.data.remote.dto.user.GetMyInfoRes
import com.weave.weave.domain.entity.login.MajorEntity
import com.weave.weave.domain.entity.login.TokenEntity
import com.weave.weave.domain.entity.login.UniversityEntity
import com.weave.weave.domain.entity.profile.MyInfoEntity
import com.weave.weave.domain.entity.team.GetMyTeamEntity
import com.weave.weave.domain.entity.team.GetMyTeamItemEntity
import com.weave.weave.domain.entity.team.GetMyTeamMemberInfoEntity

// Login
fun TokenRes.asDomain() = TokenEntity(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
)

fun UniversitiesRes.asDomain() = UniversityEntity(
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
    limit = this.limit
)

fun GetMyTeamItem.asDomain() = GetMyTeamItemEntity(
    id, teamIntroduce, memberCount, location, memberInfos = memberInfos.map { it.asDomain() }
)

fun GetMyTeamMemberInfos.asDomain() = GetMyTeamMemberInfoEntity(
    id, universityName, mbti, birthYear, isLeader, isMe
)




