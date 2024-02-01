package com.weave.weave.presentation.view.my

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.weave.weave.R
import com.weave.weave.databinding.BottomSheetDialogAnimalBinding
import com.weave.weave.presentation.viewmodel.MyViewModel

class AnimalEditBottomSheetDialog(private val vm: MyViewModel): BottomSheetDialogFragment(){

    companion object {
        private var instance: AnimalEditBottomSheetDialog? = null

        fun getInstance(vm: MyViewModel): AnimalEditBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: AnimalEditBottomSheetDialog(vm).also { instance = it }
            }
        }
    }

    private var _binding: BottomSheetDialogAnimalBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonIdList: List<Button>

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog_animal, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm
        vm.setSaveBtn(false)

        setButtonList()
        setClickListener()

        binding.ibClose.setOnClickListener {
            vm.setAnimalBtn("")
            dismiss()
        }

        binding.ibSave.setOnClickListener {
            if(vm.saveBtn.value!!){
                vm.setAnimal()
                vm.setAnimalBtn("")
                dismiss()
            }
        }

        vm.animalBtn.observe(this){
            if(it != ""){
                vm.setSaveBtn(true)
            } else {
                vm.setSaveBtn(false)
            }
        }


        return binding.root
    }

    private fun setButtonList(){
        buttonIdList = listOf(
            binding.btnDog,
            binding.btnCat,
            binding.btnFox,
            binding.btnRabbit,
            binding.btnTiger,
            binding.btnMonkey,
            binding.btnTurtle,
            binding.btnDeer,
            binding.btnHamster,
            binding.btnWolf,
            binding.btnBear,
            binding.btnPanda,
            binding.btnSnake,
            binding.btnOtter,
            binding.btnFish,
            binding.btnChick,
            binding.btnDinosaur,
            binding.btnHorse,
            binding.btnSloth,
            binding.btnLion,
            binding.btnCamel
        )
    }

    private fun setClickListener(){
        buttonIdList.forEach { button ->
            button.setOnClickListener {
                val animal = button.text.substring(button.text.indexOf(" ") + 1)
                vm.setAnimalBtn(animal)
                Log.i("TEST", vm.animalBtn.value.toString())
            }
        }
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