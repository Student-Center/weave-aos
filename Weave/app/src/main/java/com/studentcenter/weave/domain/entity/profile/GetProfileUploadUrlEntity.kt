package com.studentcenter.weave.domain.entity.profile

data class GetProfileUploadUrlEntity(
    val uploadUrl: String,
    val imageId: String,
    val extension: String
)
