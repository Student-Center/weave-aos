package com.studentcenter.weave.presentation.util

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.density
import com.studentcenter.weave.databinding.DialogCustomBinding
import com.studentcenter.weave.presentation.view.signIn.SignInActivity

class CustomDialog private constructor(private val dialogType: DialogType, private val msg: String?) : DialogFragment() {

    enum class DialogType {
        SIGN_UP_CANCEL, // 이탈 방지
        REGISTER,       // 첫 입장
        EMAIL,          // 이메일 발송 완료
        EMAIL_VERIFY,   // 인증 완료
        EMAIL_TIME_OVER,// 시간 초과
        CERTIFY,        // 대학교 인증 필요
        NO_TEAM,        // 팀 필요
        LOGOUT,         // 로그아웃
        UNLINK,         // 회원탈퇴
        MEETING_REQUEST,// 미팅 요청
        TEAM_DELETE,    // 팀 삭제
        TEAM_DELETE_PUBLISHED, // 팀 삭제 (게시된 팀인 경우)
        TEAM_EXIT,      // 팀 나가기
        TEAM_EXIT_PUBLISHED, // 팀 나가기 (게시된 팀인 경우)
        TEAM_INVITATION,// 초대장
        TEAM_INVITATION_FIRST,
        TEAM_NO_SPACE,  // 초대 받은 팀에 자리 없음
        TEAM_NO_SPACE_FIRST,
        MEETING_PASS,    // 미팅 패스
        MEETING_ATTEND,  // 미팅 참가
        REPORT,          // 신고
        KAKAO_ID,          // 카카오톡 ID 재확인
        NULL_KAKAO_ID,    // 카카오톡 ID 필요
        SUGGESTION        // 개선 제안
    }

    companion object {
        @Volatile
        private var instance: CustomDialog? = null

        fun getInstance(dialogType: DialogType, msg: String?): CustomDialog {
            synchronized(this) {
                instance?.dismiss() // 기존 인스턴스 제거
                instance = CustomDialog(dialogType, msg) // 새로운 인스턴스 생성 및 설정
                return instance!!
            }
        }
    }

    private var _binding: DialogCustomBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()

        val widthInDp = 330

        val widthInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        dialog?.window?.setLayout(widthInPixels, WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_custom, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // 생성된 다이얼로그 종류에 따라 다른 작업 수행
        when (dialogType) {
            DialogType.SIGN_UP_CANCEL -> { setSignUpCancel() }
            DialogType.REGISTER -> { setRegister() }
            DialogType.EMAIL -> { setEmail() }
            DialogType.EMAIL_VERIFY -> { setEmailVerify() }
            DialogType.EMAIL_TIME_OVER -> { setEmailTimeOver() }
            DialogType.CERTIFY -> { setCertify() }
            DialogType.NO_TEAM -> { setNoTeam() }
            DialogType.LOGOUT -> { setLogOut() }
            DialogType.UNLINK -> { setUnlink() }
            DialogType.MEETING_REQUEST -> { setMeetingRequest() }
            DialogType.TEAM_DELETE -> { setTeamDelete() }
            DialogType.TEAM_DELETE_PUBLISHED -> { setTeamDeletePublished() }
            DialogType.TEAM_EXIT -> { setTeamExit() }
            DialogType.TEAM_EXIT_PUBLISHED -> { setTeamExitPublished() }
            DialogType.TEAM_INVITATION -> { setTeamInvitation() }
            DialogType.TEAM_INVITATION_FIRST -> { setTeamInvitationFirst() }
            DialogType.TEAM_NO_SPACE -> { setNoSpace() }
            DialogType.TEAM_NO_SPACE_FIRST -> { setNoSpaceFirst() }
            DialogType.MEETING_PASS -> { setMeetingPass() }
            DialogType.MEETING_ATTEND -> { setMeetingAttend() }
            DialogType.REPORT -> { setReport() }
            DialogType.KAKAO_ID -> { setKakaoId() }
            DialogType.NULL_KAKAO_ID -> { setNullKakaoId() }
            DialogType.SUGGESTION -> { setSuggestion() }
        }

        return binding.root
    }

    private lateinit var listener : DialogOKClickedListener

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: DialogOKClickedListener {
            override fun onOKClicked(value: String) {
                listener(value)
            }
        }
    }

    interface DialogOKClickedListener {
        fun onOKClicked(value : String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.i("DIALOG", "destroy")
    }

    private fun setSignUpCancel(){
        binding.dialogTitle.text = getString(R.string.dialog_sign_up_cancel_title)
        binding.dialogComment.text = getString(R.string.dialog_sign_up_cancel_comment)
        binding.dialogBtnYes.text = getString(R.string.dialog_sign_up_cancel_yes)
        binding.dialogBtnNo.text = getString(R.string.dialog_sign_up_cancel_no)

        binding.dialogBtnNo.setOnClickListener {
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            dismiss()
        }
    }

    private fun setRegister(){
        binding.dialogTitle.text = getString(R.string.dialog_register_title)
        binding.dialogComment.text = getString(R.string.dialog_register_comment)
        binding.dialogBtnYes.text = getString(R.string.dialog_register_yes)
        binding.dialogBtnNo.text = getString(R.string.dialog_register_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("register")
            dismiss()
        }
    }

    private fun setEmail(){
        binding.dialogTitle.text = getString(R.string.email_dialog_send_title)
        binding.dialogTitle.gravity = Gravity.CENTER
        binding.dialogTitle.setLineSpacing(22*resources.displayMetrics.density, 0f)
        binding.dialogComment.text = getString(R.string.email_dialog_send_comment)
        binding.dialogBtnYes.visibility = View.GONE // constraint 이슈로 no <-> yes 사용
        binding.dialogBtnNo.text = getString(R.string.email_dialog_send_btn)
        binding.dialogBtnNo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.basic_blue))

        val params = binding.dialogBtnNo.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = (65*resources.displayMetrics.density).toInt()
        params.rightMargin = (65*resources.displayMetrics.density).toInt()
        binding.dialogBtnNo.layoutParams = params

        binding.dialogBtnNo.setOnClickListener {
            listener.onOKClicked("send")
            dismiss()
        }
    }

    private fun setEmailVerify(){
        binding.dialogTitle.text = getString(R.string.email_verify_dialog_title)
        binding.dialogTitle.gravity = Gravity.CENTER
        binding.dialogTitle.setLineSpacing(22*resources.displayMetrics.density, 0f)
        binding.dialogComment.text = getString(R.string.email_verify_dialog_comment)
        binding.dialogBtnYes.text = getString(R.string.email_verify_dialog_yes)
        binding.dialogBtnNo.text = getString(R.string.email_verify_dialog_no)

        binding.dialogBtnNo.setOnClickListener {
            listener.onOKClicked("no")
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("yes")
            dismiss()
        }
    }

    private fun setEmailTimeOver(){
        binding.dialogTitle.text = getString(R.string.email_over_dialog_title)
        binding.dialogTitle.gravity = Gravity.CENTER
        binding.dialogTitle.setLineSpacing(22*resources.displayMetrics.density, 0f)
        binding.dialogComment.text = getString(R.string.email_over_dialog_comment)
        binding.dialogBtnYes.visibility = View.GONE // constraint 이슈로 no <-> yes 사용
        binding.dialogBtnNo.text = getString(R.string.email_dialog_send_btn)
        binding.dialogBtnNo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.basic_blue))

        val params = binding.dialogBtnNo.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = (65*resources.displayMetrics.density).toInt()
        params.rightMargin = (65*resources.displayMetrics.density).toInt()
        binding.dialogBtnNo.layoutParams = params

        binding.dialogBtnNo.setOnClickListener {
            listener.onOKClicked("time_over")
            dismiss()
        }
    }

    private fun setCertify(){
        binding.dialogTitle.text = getString(R.string.dialog_certify_request_title)
        binding.dialogComment.text = getString(R.string.dialog_certify_request_comment)
        binding.dialogBtnYes.text = getString(R.string.dialog_register_yes)
        binding.dialogBtnNo.text = getString(R.string.dialog_register_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("certify")
            dismiss()
        }
    }

    private fun setNoTeam(){
        binding.dialogTitle.text = getString(R.string.dialog_no_team_title)
        binding.dialogComment.text = getString(R.string.dialog_no_team_comment)
        binding.dialogBtnYes.text = getString(R.string.dialog_register_yes)
        binding.dialogBtnNo.text = getString(R.string.dialog_register_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            dismiss()
        }
    }

    private fun setLogOut(){
        val params = binding.dialogTitle.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = (32 * density!!).toInt()
        binding.dialogTitle.layoutParams = params
        binding.dialogTitle.text = getString(R.string.setting_sign_out_comment)
        binding.dialogComment.visibility = View.GONE
        binding.dialogBtnYes.text = getString(R.string.setting_yes)
        binding.dialogBtnNo.text = getString(R.string.setting_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("logout")
            dismiss()
        }
    }

    private fun setUnlink(){
        binding.dialogTitle.text = getString(R.string.setting_unlink_title)
        binding.dialogComment.text = getString(R.string.setting_unlink_comment)
        binding.dialogBtnYes.text = getString(R.string.setting_yes)
        binding.dialogBtnNo.text = getString(R.string.setting_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("unlink")
            dismiss()
        }
    }

    private fun setMeetingRequest(){
        binding.dialogTitle.text = getString(R.string.detail_dialog_request_title)
        binding.dialogComment.text = getString(R.string.detail_dialog_request_comment, msg)
        binding.dialogBtnYes.text = getString(R.string.detail_dialog_request_yes)
        binding.dialogBtnNo.text = getString(R.string.dialog_register_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("meeting_request")
            dismiss()
        }
    }

    private fun setTeamDelete(){
        binding.dialogTitle.visibility = View.GONE
        val params = binding.dialogComment.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = 24*4
        binding.dialogComment.layoutParams = params
        binding.dialogComment.text = getString(R.string.team_delete_comment, msg)
        binding.dialogBtnYes.text = getString(R.string.team_delete_yes)
        binding.dialogBtnNo.text = getString(R.string.team_delete_no)
        binding.dialogBtnYes.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.red))

        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("remove")
            dismiss()
        }
        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
    }

    private fun setTeamDeletePublished(){
        binding.dialogTitle.text = getString(R.string.team_delete_comment, msg)
        binding.dialogComment.text = getString(R.string.team_delete_comment_published)
        binding.dialogBtnYes.text = getString(R.string.team_delete_yes)
        binding.dialogBtnNo.text = getString(R.string.team_delete_no)
        binding.dialogBtnYes.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.red))

        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("remove")
            dismiss()
        }
        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
    }

    private fun setTeamExit(){
        binding.dialogTitle.visibility = View.GONE
        val params = binding.dialogComment.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = 24*4
        binding.dialogComment.layoutParams = params
        binding.dialogComment.text = getString(R.string.team_exit_comment, msg)
        binding.dialogBtnYes.text = getString(R.string.team_exit_yes)
        binding.dialogBtnNo.text = getString(R.string.team_delete_no)
        binding.dialogBtnYes.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.red))

        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("exit")
            dismiss()
        }
        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
    }

    private fun setTeamExitPublished(){
        binding.dialogTitle.text = getString(R.string.team_exit_comment, msg)
        binding.dialogComment.text = getString(R.string.team_exit_comment_published)
        binding.dialogBtnYes.text = getString(R.string.team_exit_yes)
        binding.dialogBtnNo.text = getString(R.string.team_delete_no)
        binding.dialogBtnYes.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.red))

        binding.dialogBtnYes.setOnClickListener {
            listener.onOKClicked("exit_published")
            dismiss()
        }
        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
    }

    private fun setTeamInvitation(){
        binding.dialogTitle.text = getString(R.string.dialog_invitation_title)
        binding.dialogComment.text = getString(R.string.dialog_invitation_comment, msg)
        binding.dialogBtnNo.text = getString(R.string.dialog_invitation_no)
        binding.dialogBtnYes.text = getString(R.string.dialog_invitation_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("invitation")
            dismiss()
        }
    }

    private fun setTeamInvitationFirst(){
        binding.dialogTitle.text = getString(R.string.dialog_invitation_title)
        binding.dialogComment.text = getString(R.string.dialog_invitation_first_comment, msg)
        binding.dialogBtnNo.text = getString(R.string.dialog_invitation_no)
        binding.dialogBtnYes.text = getString(R.string.dialog_invitation_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("invitation_first")
            dismiss()
        }
    }

    private fun setNoSpace(){
        binding.dialogTitle.text = getString(R.string.dialog_no_space_title)
        binding.dialogComment.text = getString(R.string.dialog_no_space_comment)
        binding.dialogBtnNo.text = getString(R.string.dialog_no_space_no)
        binding.dialogBtnYes.text = getString(R.string.dialog_no_space_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("no_space")
            dismiss()
        }
    }

    private fun setNoSpaceFirst(){
        binding.dialogTitle.text = getString(R.string.dialog_no_space_title)
        binding.dialogComment.text = getString(R.string.dialog_no_space_first_comment)
        binding.dialogBtnNo.text = getString(R.string.dialog_no_space_no)
        binding.dialogBtnYes.text = getString(R.string.dialog_no_space_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("no_space_first")
            dismiss()
        }
    }

    private fun setMeetingPass(){
        binding.dialogTitle.text = getString(R.string.dialog_meeting_pass_title)
        binding.dialogComment.text = getString(R.string.dialog_meeting_pass_comment, msg)
        binding.dialogBtnNo.text = getString(R.string.dialog_meeting_no)
        binding.dialogBtnYes.text = getString(R.string.dialog_meeting_pass_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("pass")
            dismiss()
        }
    }

    private fun setMeetingAttend(){
        binding.dialogTitle.text = getString(R.string.dialog_meeting_attend_title)
        binding.dialogComment.text = getString(R.string.dialog_meeting_attend_comment)
        binding.dialogBtnNo.text = getString(R.string.dialog_meeting_no)
        binding.dialogBtnYes.text = getString(R.string.dialog_meeting_attend_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("attend")
            dismiss()
        }
    }

    private fun setReport(){
        binding.dialogTitle.text = getString(R.string.dialog_report_title)
        binding.dialogComment.visibility = View.GONE
        binding.dialogBtnYes.visibility = View.GONE
        binding.dialogBtnNo.text = getString(R.string.dialog_report_yes)
        binding.dialogBtnNo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.basic_blue))

        val params = binding.dialogBtnNo.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = (65*resources.displayMetrics.density).toInt()
        params.rightMargin = (65*resources.displayMetrics.density).toInt()
        params.topMargin = (24*resources.displayMetrics.density).toInt()
        binding.dialogBtnNo.layoutParams = params

        binding.dialogBtnNo.setOnClickListener {
            listener.onOKClicked("report")
            dismiss()
        }
    }

    private fun setKakaoId(){
        binding.dialogTitle.text = getString(R.string.kakao_dialog_title)
        binding.dialogComment.text = getString(R.string.kakao_dialog_comment, msg)
        binding.dialogBtnNo.text = getString(R.string.kakao_dialog_no)
        binding.dialogBtnYes.text = getString(R.string.kakao_dialog_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("kakaoId")
            dismiss()
        }
    }

    private fun setNullKakaoId(){
        binding.dialogTitle.text = getString(R.string.kakao_null_dialog_title)
        binding.dialogComment.text = getString(R.string.kakao_null_dialog_comment)
        binding.dialogBtnNo.text = getString(R.string.kakao_null_dialog_no)
        binding.dialogBtnYes.text = getString(R.string.kakao_null_dialog_yes)

        binding.dialogBtnNo.setOnClickListener{
            dismiss()
        }

        binding.dialogBtnYes.setOnClickListener{
            listener.onOKClicked("kakaoId")
            dismiss()
        }
    }

    private fun setSuggestion() {
        binding.dialogComment.visibility = View.GONE
        binding.dialogBtnYes.visibility = View.GONE

        binding.dialogTitle.text = getString(R.string.dialog_suggestion_title)
        binding.dialogTitle.gravity = Gravity.CENTER
        binding.dialogTitle.setLineSpacing(22*density!!, 0f)

        binding.dialogBtnNo.text = getString(R.string.dialog_suggestion_btn)
        binding.dialogBtnNo.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.basic_blue))

        val params = binding.dialogBtnNo.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = (65*resources.displayMetrics.density).toInt()
        params.rightMargin = (65*resources.displayMetrics.density).toInt()
        binding.dialogBtnNo.layoutParams = params

        binding.dialogBtnNo.setOnClickListener{
            listener.onOKClicked("suggestion")
            dismiss()
        }
    }
}
