package com.studentcenter.weave.presentation.view.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.RangeSlider
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.locations
import com.studentcenter.weave.databinding.BottomSheetDialogFilterBinding
import com.studentcenter.weave.presentation.viewmodel.HomeViewModel


class FilterBottomSheetDialog(private val vm: HomeViewModel): BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var capitalBtnList: List<Button>
    private lateinit var nonCapitalBtnList: List<Button>

    private var selectedMemberCount: Int? = vm.memberCount
    private var selectedYoungest: Int = vm.youngestMemberBirthYear
    private var selectedOldest: Int = vm.oldestMemberBirthYear
    private val selectedLocations = mutableListOf<String>().apply { addAll(vm.preferredLocations) }

    companion object {
        private var instance: FilterBottomSheetDialog? = null

        fun getInstance(vm: HomeViewModel): FilterBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: FilterBottomSheetDialog(vm).also { instance = it }
            }
        }
    }

    private var _binding: BottomSheetDialogFilterBinding? = null
    private val binding get() = _binding!!


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
        _binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_dialog_filter, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        setType()
        setAge()
        loadLocationBtn()
        setLocation()

        binding.ibClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            vm.setFilter(selectedMemberCount, selectedYoungest, selectedOldest, selectedLocations)
            dismiss()
        }

        binding.btnReset.setOnClickListener {
            selectedMemberCount = null
            selectedYoungest = 2006
            selectedOldest = 1996
            selectedLocations.clear()

            vm.memberCount = null
            vm.youngestMemberBirthYear = 2006
            vm.oldestMemberBirthYear = 1996
            vm.preferredLocations = listOf()

            setType()
            setAge()
            loadLocationBtn()
        }

        return binding.root
    }

    private fun resetBtnActivate(){
        if(selectedMemberCount != null || selectedOldest != 1996 || selectedYoungest != 2006 || selectedLocations.isNotEmpty()){
            binding.btnReset.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.basic_white))
            binding.btnReset.setTextColor(requireContext().getColor(R.color.basic_white))
        } else {
            binding.btnReset.backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.grey_80))
            binding.btnReset.setTextColor(requireContext().getColor(R.color.grey_80))
        }
    }

    private fun setType() {
        with(binding){
            when(selectedMemberCount){
                2 -> btnType2.isSelected = true
                3 -> btnType3.isSelected = true
                4 -> btnType4.isSelected = true
                else -> {
                    btnType2.isSelected = false
                    btnType3.isSelected = false
                    btnType4.isSelected = false
                }
            }

            checkSelected() // 타입 버튼 색상
        }

        binding.btnType2.setOnClickListener {
            if(binding.btnType2.isSelected){
                binding.btnType2.isSelected = false
            } else {
                binding.btnType2.isSelected = true
                binding.btnType3.isSelected = false
                binding.btnType4.isSelected = false
            }

            checkSelected()
        }

        binding.btnType3.setOnClickListener {
            if(binding.btnType3.isSelected){
                binding.btnType3.isSelected = false
            } else {
                binding.btnType2.isSelected = false
                binding.btnType3.isSelected = true
                binding.btnType4.isSelected = false
            }

            checkSelected()
        }

        binding.btnType4.setOnClickListener {
            if(binding.btnType4.isSelected){
                binding.btnType4.isSelected = false
            } else {
                binding.btnType2.isSelected = false
                binding.btnType3.isSelected = false
                binding.btnType4.isSelected = true
            }

            checkSelected()
        }
    }

    private fun checkSelected(){
        if(binding.btnType2.isSelected) {
            selectedMemberCount = 2
            binding.btnType2.setTextColor(requireContext().getColor(R.color.basic_blue))
        } else binding.btnType2.setTextColor(requireContext().getColor(R.color.grey_44))
        if(binding.btnType3.isSelected) {
            selectedMemberCount = 3
            binding.btnType3.setTextColor(requireContext().getColor(R.color.basic_blue))
        } else binding.btnType3.setTextColor(requireContext().getColor(R.color.grey_44))
        if(binding.btnType4.isSelected) {
            selectedMemberCount = 4
            binding.btnType4.setTextColor(requireContext().getColor(R.color.basic_blue))
        } else binding.btnType4.setTextColor(requireContext().getColor(R.color.grey_44))


        if(!binding.btnType2.isSelected && !binding.btnType3.isSelected && !binding.btnType4.isSelected) selectedMemberCount = null

        resetBtnActivate()
    }

    private fun setAge(){
        binding.age.text = getString(R.string.filter_birth_year_format, selectedOldest.toString().takeLast(2), selectedYoungest.toString().takeLast(2))
        binding.rangeSlider.values = listOf(selectedOldest.toFloat(), selectedYoungest.toFloat())
        binding.rangeSlider.addOnSliderTouchListener(rangeSliderTouchListener)
    }

    private val rangeSliderTouchListener: RangeSlider.OnSliderTouchListener =
        object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                selectedOldest = slider.values[0].toInt()
                selectedYoungest = slider.values[1].toInt()

                val oldestNumber = slider.values[0].toString().indexOf(".")
                val youngestNumber = slider.values[1].toString().indexOf(".")
                val oldestVal = slider.values[0].toString().substring(0, oldestNumber).takeLast(2)
                val youngestVal = slider.values[1].toString().substring(0, youngestNumber).takeLast(2)
                binding.age.text = getString(R.string.filter_birth_year_format, oldestVal, youngestVal)

                resetBtnActivate()
            }
        }

    private fun loadLocationBtn() {
        with(binding.locationCapital){
            capitalBtnList = listOf(
                btnLocationBucheon,
                btnLocationHi,
                btnLocationKu,
                btnLocationGangnam,
                btnLocationHyehwa,
                btnLocationIncheon,
                btnLocationNowon,
                btnLocationSiheung,
                btnLocationSuwon,
                btnLocationYongin
            )
        }

        with(binding.locationNonCapital){
            nonCapitalBtnList = listOf(
                btnLocationBusan,
                btnLocationChungbuk,
                btnLocationChungnam,
                btnLocationDaegu,
                btnLocationDaejeon,
                btnLocationGangwon,
                btnLocationGwangju
            )
        }

        capitalBtnList.forEach {
            it.isSelected = false
            it.setTextColor(requireContext().getColor(R.color.grey_44))
        }
        nonCapitalBtnList.forEach {
            it.isSelected = false
            it.setTextColor(requireContext().getColor(R.color.grey_44))
        }
    }

    private fun setLocation(){
        val savedLocations = selectedLocations.listIterator()
        while(savedLocations.hasNext()){
            val nextData = savedLocations.next()
            val displayName = locations?.find {it.name == nextData}?.displayName ?: ""

            if(displayName.isEmpty()) continue

            capitalBtnList.find { it.text == displayName }?.apply {
                setTextColor(requireContext().getColor(R.color.basic_blue))
                isSelected = true
            }
            nonCapitalBtnList.find { it.text == displayName }?.apply {
                setTextColor(requireContext().getColor(R.color.basic_blue))
                isSelected = true
            }
        }

        capitalBtnList.forEach { it.setOnClickListener(this) }
        nonCapitalBtnList.forEach { it.setOnClickListener(this) }

        binding.btnLocationCapital.setOnClickListener {
            if(!binding.btnLocationCapital.isSelected){ // 수도권 버튼 비활성화 시 클릭한 경우
                binding.btnLocationCapital.isSelected = true
                binding.btnLocationCapital.setTextColor(requireContext().getColor(R.color.basic_blue))
                binding.btnLocationNonCapital.isSelected = false
                binding.btnLocationNonCapital.setTextColor(requireContext().getColor(R.color.grey_44))
            } else { // 수도권 버튼 활성화 시 클릭한 경우
                binding.btnLocationCapital.isSelected = false
                binding.btnLocationCapital.setTextColor(requireContext().getColor(R.color.grey_44))
            }

            Log.i("Capital Before", binding.flLocationCapital.isVisible.toString())
            if(binding.btnLocationCapital.isSelected){
                binding.flLocationCapital.isVisible = true
                binding.flLocationNonCapital.isVisible = false
            } else {
                binding.flLocationCapital.isVisible = false
            }
            Log.i("Capital After", binding.flLocationCapital.isVisible.toString())

        }

        binding.btnLocationNonCapital.setOnClickListener {
            if(!binding.btnLocationNonCapital.isSelected){ // 비수도권 버튼 비활성화 시 클릭한 경우
                binding.btnLocationNonCapital.isSelected = true
                binding.btnLocationNonCapital.setTextColor(requireContext().getColor(R.color.basic_blue))
                binding.btnLocationCapital.isSelected = false
                binding.btnLocationCapital.setTextColor(requireContext().getColor(R.color.grey_44))
            } else { // 비수도권 버튼 활성화 시 클릭한 경우
                binding.btnLocationNonCapital.isSelected = false
                binding.btnLocationNonCapital.setTextColor(requireContext().getColor(R.color.grey_44))
            }

            Log.i("NonCapital Before", binding.flLocationNonCapital.isVisible.toString())
            if(binding.btnLocationNonCapital.isSelected){
                binding.flLocationNonCapital.isVisible = true
                binding.flLocationCapital.isVisible = false
            } else {
                binding.flLocationNonCapital.isVisible = false
            }
            Log.i("NonCapital After", binding.flLocationNonCapital.isVisible.toString())

        }
    }

    override fun onClick(v: View?) {
        if(binding.btnLocationCapital.isSelected){
            capitalBtnList.forEach { button ->
                if (button.id == v?.id) {
                    button.isSelected = !button.isSelected
                    addLocations(button.text.toString())
                }

                if(button.isSelected){
                    button.setTextColor(requireContext().getColor(R.color.basic_blue))
                } else {
                    button.setTextColor(requireContext().getColor(R.color.grey_44))
                }
            }
        }

        if(binding.btnLocationNonCapital.isSelected){
            nonCapitalBtnList.forEach { button ->
                if (button.id == v?.id) {
                    button.isSelected = !button.isSelected
                    addLocations(button.text.toString())
                }

                if(button.isSelected){
                    button.setTextColor(requireContext().getColor(R.color.basic_blue))
                } else {
                    button.setTextColor(requireContext().getColor(R.color.grey_44))
                }
            }
        }

        resetBtnActivate()
    }

    private fun addLocations(displayName: String){
        val locationName = locations?.find { it.displayName == displayName }?.name
        if(!locationName.isNullOrEmpty()){
            if(selectedLocations.contains(locationName)) { selectedLocations.remove(locationName) }
            else { selectedLocations.add(locationName) }
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