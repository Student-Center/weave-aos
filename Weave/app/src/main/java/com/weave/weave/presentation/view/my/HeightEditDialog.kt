package com.weave.weave.presentation.view.my

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.weave.weave.R
import com.weave.weave.databinding.DialogEditHeightBinding
import com.weave.weave.presentation.viewmodel.MyViewModel

class HeightEditDialog(private val vm: MyViewModel) : DialogFragment() {
    companion object {
        private var instance: HeightEditDialog? = null

        fun getInstance(vm: MyViewModel): HeightEditDialog {
            return instance ?: synchronized(this) {
                instance ?: HeightEditDialog(vm).also { instance = it }
            }
        }
    }

    private var _binding: DialogEditHeightBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_height, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm
        vm.setSaveBtn(false)

        binding.tvHeight.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        binding.tvHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length
                if (textLength == 3) {
                    val number = s.toString().toIntOrNull()
                    if (number in 100..300) {
                        vm.setSaveBtn(true)
                    }
                } else {
                    vm.setSaveBtn(false)
                }
            }
        })

        binding.ibSave.setOnClickListener {
            if(vm.saveBtn.value!!){
                vm.setMyHeight(binding.tvHeight.text.toString())
                vm.setSaveBtn(false)
                dismiss()
            }
        }

        binding.root.setOnClickListener {
            dismiss()
        }

        binding.tvHeight.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(vm.saveBtn.value!!){
                    binding.ibSave.performClick()
                }
                handled = true
            }
            handled
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
        Log.i("DIALOG", "destroy")
    }
}
