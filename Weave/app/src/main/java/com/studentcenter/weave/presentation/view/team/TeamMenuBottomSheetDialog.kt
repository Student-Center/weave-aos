package com.studentcenter.weave.presentation.view.team

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.BottomSheetDialogTeamMenuBinding
import com.studentcenter.weave.domain.entity.team.GetMyTeamItemEntity
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.viewmodel.TeamViewModel

class TeamMenuBottomSheetDialog(private val teamInfo: GetMyTeamItemEntity, private val id: String, private val vm: TeamViewModel): BottomSheetDialogFragment(){

    companion object {
        private var instance: TeamMenuBottomSheetDialog? = null

        fun getInstance(teamInfo: GetMyTeamItemEntity, id: String, vm: TeamViewModel): TeamMenuBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: TeamMenuBottomSheetDialog(teamInfo, id, vm).also { instance = it }
            }
        }
    }

    private var _binding: BottomSheetDialogTeamMenuBinding? = null
    val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog_team_menu, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnDelete.setOnClickListener {
            val dialogType = if(teamInfo.memberCount == teamInfo.memberInfos.size) CustomDialog.DialogType.TEAM_DELETE_PUBLISHED else CustomDialog.DialogType.TEAM_DELETE

            CustomDialog.getInstance(dialogType, teamInfo.teamIntroduce).apply {
                setOnOKClickedListener {
                    vm.deleteTeam(this@TeamMenuBottomSheetDialog.id)
                }
            }.show(requireActivity().supportFragmentManager, "delete_dialog")
            dismiss()
        }

        binding.btnEdit.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, TeamEditFragment(id, teamInfo.memberCount == teamInfo.memberInfos.size))
                .addToBackStack(null)
                .commit()
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }


}