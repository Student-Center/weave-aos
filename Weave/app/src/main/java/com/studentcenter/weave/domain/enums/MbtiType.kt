package com.studentcenter.weave.domain.enums

import com.studentcenter.weave.domain.extension.emoji

enum class MbtiType(val description: String) {
    INFP("${emoji(0x1F643)} INFP•몽상가형 친구"),
    ISTJ("${emoji(0x1F913)} ISTJ•꼼꼼한 친구"),
    ISFJ("${emoji(0x1F609)} ISFJ•살림 만렙 친구"),
    INFJ("${emoji(0x1F644)} INFJ•생각이 많은 친구"),
    INTJ("${emoji(0x1F9D0)} INTJ•지적인 친구"),
    ISTP("${emoji(0x1F60F)} ISTP•맥가이버형 친구"),
    ISFP("${emoji(0x1F603)} ISFP•평화주의자 친구"),
    INTP("${emoji(0x1F914)} INTP•호기심 많은 친구"),
    ESTP("${emoji(0x1F92D)} ESTP•다재다능한 친구"),
    ESFP("${emoji(0x1F973)} ESFP•분위기 메이커 친구"),
    ENFP("${emoji(0x1F920)} ENFP•자유로운 영혼 친구"),
    ENTP("${emoji(0x1F60E)} ENTP•자아도취형 친구"),
    ESTJ("${emoji(0x1F929)} ESTJ•행동 대장 친구"),
    ESFJ("${emoji(0x1F917)} ESFJ•계모임 총무형 친구"),
    ENFJ("${emoji(0x1F970)} ENFJ•사람 좋아 친구"),
    ENTJ("${emoji(0x1F624)} ENTJ•열정 만수르 친구")
}