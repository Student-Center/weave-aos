package com.weave.weave.presentation.view.team

import android.app.Activity
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.viewModels
import com.weave.weave.R
import com.weave.weave.databinding.FragmentTeamNewBinding
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.viewmodel.TeamNewViewModel

class TeamNewFragment: BaseFragment<FragmentTeamNewBinding>(R.layout.fragment_team_new), View.OnClickListener  {
    private val viewModel by viewModels<TeamNewViewModel>()
    private lateinit var capitalBtnList: List<Button>
    private lateinit var nonCapitalBtnList: List<Button>


    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        setTypeBtn()
        setCapitalBtn()
        setLocationBtn()
        setEditText()

        // API Post 요청 후 popBackStack으로 돌아가면 새로고침되면서 팀 추가
        binding.btnNext.setOnClickListener {
            if(viewModel.createTeam()){
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "팀 생성 실패: 다시 시도 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setEditText(){
        var newTintColor: Int

        binding.etDesc.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.svTeamNew.postDelayed({
                    binding.svTeamNew.fullScroll(ScrollView.FOCUS_DOWN)
                }, 100)
            }
        }

        binding.etDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDesc(s.toString())

                newTintColor = if(binding.etDesc.text.isNotEmpty()) ContextCompat.getColor(requireContext(), R.color.basic_white) else ContextCompat.getColor(requireContext(), R.color.grey_4c)
                ImageViewCompat.setImageTintList(binding.ibClear , ColorStateList.valueOf(newTintColor))

            }
        })

        binding.ibClear.setOnClickListener {
            binding.etDesc.text = null
        }
    }

    private fun setTypeBtn(){
        binding.btnType2.setOnClickListener { viewModel.setType(binding.btnType2.text.toString()) }
        binding.btnType3.setOnClickListener { viewModel.setType(binding.btnType3.text.toString()) }
        binding.btnType4.setOnClickListener { viewModel.setType(binding.btnType4.text.toString()) }
    }

    private fun setCapitalBtn(){
        binding.btnLocationCapital.setOnClickListener { viewModel.setIsCapital(binding.btnLocationCapital.text.toString()) }
        binding.btnLocationNonCapital.setOnClickListener { viewModel.setIsCapital(binding.btnLocationNonCapital.text.toString()) }
    }

    private fun setLocationBtn() {
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

    override fun onClick(v: View?) {
        if(binding.etDesc.id != v?.id){
            binding.etDesc.clearFocus()
            binding.btnDummy.requestFocus()
            hideKeyboard()
        }

        if(viewModel.isCapital.value == getString(R.string.location_capital)){
            clearLocation(nonCapitalBtnList)

            capitalBtnList.forEach { button ->
                button.isSelected = button.id == v?.id

                if(button.isSelected){
                    button.setTextColor(requireContext().getColor(R.color.basic_blue))
                    viewModel.setLocation(button.text.toString())
                } else {
                    button.setTextColor(requireContext().getColor(R.color.grey_44))
                }
            }
        }

        if(viewModel.isCapital.value == getString(R.string.location_non_capital)){
            clearLocation(capitalBtnList)

            nonCapitalBtnList.forEach { button ->
                button.isSelected = button.id == v?.id

                if(button.isSelected){
                    button.setTextColor(requireContext().getColor(R.color.basic_blue))
                    viewModel.setLocation(button.text.toString())
                } else {
                    button.setTextColor(requireContext().getColor(R.color.grey_44))
                }
            }
        }
    }

    private fun clearLocation(list: List<Button>){
        list.forEach { button ->
            button.isSelected = false
            button.setTextColor(requireContext().getColor(R.color.grey_44))
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}