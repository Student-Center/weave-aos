package com.studentcenter.weave.presentation.view.home

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
import com.studentcenter.weave.databinding.BottomSheetDialogFilterBinding


class FilterBottomSheetDialog: BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var capitalBtnList: List<Button>
    private lateinit var nonCapitalBtnList: List<Button>


    companion object {
        private var instance: FilterBottomSheetDialog? = null

        fun getInstance(): FilterBottomSheetDialog {
            return instance ?: synchronized(this) {
                instance ?: FilterBottomSheetDialog().also { instance = it }
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
            Log.i("Filter", "saved")
            dismiss()
        }

        return binding.root
    }

    private fun setType() {
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

        if(binding.btnType2.isSelected) binding.btnType2.setTextColor(requireContext().getColor(R.color.basic_blue)) else binding.btnType2.setTextColor(requireContext().getColor(R.color.grey_44))
        if(binding.btnType3.isSelected) binding.btnType3.setTextColor(requireContext().getColor(R.color.basic_blue)) else binding.btnType3.setTextColor(requireContext().getColor(R.color.grey_44))
        if(binding.btnType4.isSelected) binding.btnType4.setTextColor(requireContext().getColor(R.color.basic_blue)) else binding.btnType4.setTextColor(requireContext().getColor(R.color.grey_44))
    }

    private fun setAge(){
        binding.age.text = getString(R.string.filter_age_format, "20", "35")
        binding.rangeSlider.addOnSliderTouchListener(rangeSliderTouchListener)
    }

    private val rangeSliderTouchListener: RangeSlider.OnSliderTouchListener =
        object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val miniNumber = slider.values[0].toString().indexOf(".")
                val maxNumber = slider.values[1].toString().indexOf(".")
                val minVal = slider.values[0].toString().substring(0, miniNumber)
                val maxVal = slider.values[1].toString().substring(0, maxNumber)
                binding.age.text = getString(R.string.filter_age_format, minVal, maxVal)
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

        capitalBtnList.forEach { it.setOnClickListener(this) }
        nonCapitalBtnList.forEach { it.setOnClickListener(this) }
    }

    private fun setLocation(){
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


    // Multi Select 가능성 있음 -> 수정할 수 도
    override fun onClick(v: View?) {
        if(binding.btnLocationCapital.isSelected){
            capitalBtnList.forEach { button ->
                if (button.id == v?.id) {
                    button.isSelected = !button.isSelected
                } else {
                    button.isSelected = false
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
                } else {
                    button.isSelected = false
                }

                if(button.isSelected){
                    button.setTextColor(requireContext().getColor(R.color.basic_blue))
                } else {
                    button.setTextColor(requireContext().getColor(R.color.grey_44))
                }
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