package com.weave.weave.presentation.view.my

import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentMyPageBinding
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.viewmodel.MyViewModel

class MyFragment: BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyViewModel by viewModels()

    private var backPressedTime: Long = 0L

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                requireActivity().finishAffinity()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        initInfo()

        viewModel.profileImg.observe(this){
            setProfile()
        }

        binding.ibSetting.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, SettingFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.tvMbtiBtn.setOnClickListener {
            MbtiEditBottomSheetDialog.getInstance(viewModel).show(requireActivity().supportFragmentManager, "mbti")
        }

        binding.tvAnimalBtn.setOnClickListener {
            AnimalEditBottomSheetDialog.getInstance(viewModel).show(requireActivity().supportFragmentManager, "animal")
        }

        binding.tvHeightBtn.setOnClickListener {
            HeightEditDialog.getInstance(viewModel).show(requireActivity().supportFragmentManager, "height")
        }

        binding.ibEditProfile.setOnClickListener {
            ProfileEditBottomSheetDialog.getInstance(viewModel).show(requireActivity().supportFragmentManager, "profile")
        }
    }

    private fun setProfile() {
        Log.i(TAG, viewModel.profileImg.value!!)
        Glide.with(binding.ivProfile)
            .load(viewModel.profileImg.value)
            .transform(CircleCrop())
            .override(80,80)
            .into(binding.ivProfile)
    }

    // test
    private fun initInfo(){
        viewModel.setLineValue(1, "I")
        viewModel.setLineValue(2, "S")
        viewModel.setLineValue(3, "F")
        viewModel.setLineValue(4, "J")
    }
}