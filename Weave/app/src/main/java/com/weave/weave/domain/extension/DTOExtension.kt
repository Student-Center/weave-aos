package com.weave.weave.domain.extension

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.univ.MajorsRes
import com.weave.weave.data.remote.dto.univ.UniversitiesRes
import com.weave.weave.data.remote.dto.user.GetMyInfoRes
import com.weave.weave.domain.entity.login.MajorEntity
import com.weave.weave.domain.entity.login.TokenEntity
import com.weave.weave.domain.entity.login.UniversityEntity
import com.weave.weave.domain.entity.profile.MyInfoEntity

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
    id, nickname, birthYear, majorName, avatar, mbti, animalType, height, isUniversityEmailVerified
)




