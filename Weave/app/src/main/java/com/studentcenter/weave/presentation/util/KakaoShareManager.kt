package com.studentcenter.weave.presentation.util

import android.content.Context
import android.util.Log
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate

class KakaoShareManager(private val context: Context) {
    private val TAG = "KAKAO"


    val defaultText = TextTemplate(
        text = """
                카카오톡 공유는 카카오톡을 실행하여
                사용자가 선택한 채팅방으로 메시지를 전송합니다.
            """.trimIndent(),
        link = Link(
            webUrl = "https://developers.kakao.com",
            mobileWebUrl = "https://developers.kakao.com",
        ),
        buttons = listOf(
            Button(
                "자세히 보기",
                Link(
                    androidExecutionParams = mapOf("type" to "6", "route" to "main", "data" to "data")
                )
            )
        )
    )

    fun sendMsg(){
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(context, defaultText) { sharingResult, error ->
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
//            // 카카오톡 미설치: 웹 공유 사용 권장
//            // 웹 공유 예시 코드
//            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)
//
//            // CustomTabs으로 웹 브라우저 열기
//
//            // 1. CustomTabsServiceConnection 지원 브라우저 열기
//            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
//            try {
//                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
//            } catch(e: UnsupportedOperationException) {
//                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
//            }
//
//            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
//            // ex) 다음, 네이버 등
//            try {
//                KakaoCustomTabsClient.open(context, sharerUrl)
//            } catch (e: ActivityNotFoundException) {
//                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
//            }
        }
    }
}