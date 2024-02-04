package com.weave.weave.presentation.util

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.weave.weave.R
import com.weave.weave.databinding.DialogCustomBinding
import com.weave.weave.presentation.view.signIn.SignInActivity

class CustomDialog private constructor(private val dialogType: DialogType, private val msg: String?) : DialogFragment() {

    enum class DialogType {
        SIGN_UP_CANCEL,
        REGISTER,
        EMAIL,
        CERTIFY,
        NO_TEAM,
        LOGOUT,
        UNLINK,
        MEETING_REQUEST,
        TEAM_DELETE,
        TEAM_EXIT
    }

    companion object {
        private var instance: CustomDialog? = null

        fun getInstance(dialogType: DialogType, msg: String?): CustomDialog {
            return instance ?: synchronized(this) {
                instance ?: CustomDialog(dialogType, msg).also { instance = it }
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
            DialogType.SIGN_UP_CANCEL -> {
                setSignUpCancel()
            }
            DialogType.REGISTER -> {
                setRegister()
            }
            DialogType.EMAIL -> {}
            DialogType.CERTIFY -> {
                setCertify()
            }
            DialogType.NO_TEAM -> {
                setNoTeam()
            }
            DialogType.LOGOUT -> {}
            DialogType.UNLINK -> {}
            DialogType.MEETING_REQUEST -> {
                setMeetingRequest()
            }
            DialogType.TEAM_DELETE -> {
                setTeamDelete()
            }
            DialogType.TEAM_EXIT -> {
                setTeamExit()
            }
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

    private fun setMeetingRequest(){
        binding.dialogTitle.text = getString(R.string.detail_dialog_request_title)
        binding.dialogComment.text = getString(R.string.detail_dialog_request_comment, msg)
        binding.dialogBtnYes.text = getString(R.string.detail_dialog_request_yes)
        binding.dialogBtnNo.text = getString(R.string.dialog_register_no)

        binding.dialogBtnNo.setOnClickListener {
            dismiss()
        }
        binding.dialogBtnYes.setOnClickListener {
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
}
