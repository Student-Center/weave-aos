package com.weave.weave.presentation.view.team

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weave.weave.R
import com.weave.weave.databinding.BottomSheetDialogTeamMenuBinding
import com.weave.weave.presentation.util.CustomDialog
import com.weave.weave.presentation.viewmodel.TeamViewModel

class TeamMenuBottomSheetDialog(private val title: String, private val vm: TeamViewModel): BottomSheetDialogFragment(){

    companion object {
        private var instance: TeamMenuBottomSheetDialog? = null

        fun getInstance(title: String, vm: TeamViewModel): TeamMenuBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: TeamMenuBottomSheetDialog(title, vm).also { instance = it }
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
            val dialog = CustomDialog.getInstance(CustomDialog.DialogType.TEAM_DELETE, title)
            dialog.setOnOKClickedListener {
                vm.removeTeam(title)
            }
            dialog.show(requireActivity().supportFragmentManager, "")
            dismiss()
        }

        binding.btnEdit.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, TeamEditFragment())
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