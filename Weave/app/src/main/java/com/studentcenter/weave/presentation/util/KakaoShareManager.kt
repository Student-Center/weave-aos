package com.studentcenter.weave.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.util.Log
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo

class KakaoShareManager(private val context: Context) {
    private val TAG = "KAKAO"
    private lateinit var feedTemplate: FeedTemplate

    fun sendMsg(teamId: String){
        feedTemplate = FeedTemplate(
            content = Content(
                title = "[WEAVE] 친구야 이 팀 어때?",
                imageUrl = BuildConfig.SHARE_IMAGE,
                link = Link(
                    androidExecutionParams = mapOf("type" to "team", "teamId" to teamId),
                    iosExecutionParams = mapOf("type" to "team", "teamId" to teamId),
                    mobileWebUrl = "market://details?id=com.studentcenter.weave"
                ),
                imageHeight = 400,
                imageWidth = 400
            ),
            buttonTitle = "팀 상세보기"
        )

        doShare()
    }

    fun sendInvitation(code: String){
        feedTemplate = FeedTemplate(
            content = Content(
                title = "[WEAVE] 친구야 같이 미팅하자",
                description = "미팅 팀 초대장이 도착했어요!",
                imageUrl = BuildConfig.SHARE_IMAGE,
                link = Link(
                    androidExecutionParams = mapOf("type" to "invitation", "code" to code, "userId" to myInfo!!.id),
                    iosExecutionParams = mapOf("type" to "invitation", "code" to code, "userId" to myInfo!!.id),
                    mobileWebUrl = "market://details?id=com.studentcenter.weave"
                ),
                imageHeight = 400,
                imageWidth = 400
            ),
            buttonTitle = "초대장 확인하기"
        )

        doShare()
    }

    private fun doShare(){
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            ShareClient.instance.shareDefault(context, feedTemplate) { sharingResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡 공유 실패", error)
                }
                else if (sharingResult != null) {
                    Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                    context.startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            Log.e(TAG, "카카오톡 미설치")
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(feedTemplate)

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch(e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                // 2. CustomTabsServiceConnection 미지원 브라우저 열기
                // ex) 다음, 네이버 등
                try {
                    KakaoCustomTabsClient.open(context, sharerUrl)
                } catch (e: ActivityNotFoundException) {
                    // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                }
            }
        }
    }
}