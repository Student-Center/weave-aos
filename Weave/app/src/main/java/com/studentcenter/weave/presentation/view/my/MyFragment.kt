package com.studentcenter.weave.presentation.view.my

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.databinding.FragmentMyPageBinding
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.viewmodel.MyViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initMyInfo()
    }

    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        viewModel.setMyInfo()
        setObserver()
        ssillGradient()

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

        binding.tvEmailBtn.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentWithStack(EmailFragment())
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

    private fun ssillGradient(){
        val colors = intArrayOf(
            Color.parseColor("#CC2EAE"),
            Color.parseColor("#CC596E"),
            Color.parseColor("#D0923C"),
            Color.parseColor("#B0B10F"),
            Color.parseColor("#73CF14")
        )

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,  // 그라디언트 방향 (좌측에서 우측)
            colors  // 색상 배열
        )

        gradientDrawable.cornerRadius = 90f
        binding.llSsill.background = gradientDrawable

    }

    private fun setObserver(){
        with(viewModel){
            refresh.observe(this@MyFragment){
                if(it){
                    setMyInfo()
                }
            }

            ssill.observe(this@MyFragment){
                binding.tvSsill.text = getString(R.string.my_ssill, it.toString())
            }

            profileImg.observe(this@MyFragment){
                if(!it.isNullOrEmpty()){
                    setProfile()
                }
            }

            mbti.observe(this@MyFragment){
                binding.tvMbtiBtn.text = it
            }

            animal.observe(this@MyFragment){
                if(!it.isNullOrEmpty()){
                    binding.tvAnimalBtn.text = it
                    binding.tvAnimalBtn.setTextColor(requireContext().getColor(R.color.grey_8E))
                }
            }

            height.observe(this@MyFragment){
                if(!it.isNullOrEmpty()){
                    binding.tvHeightBtn.text = it
                    binding.tvHeightBtn.setTextColor(requireContext().getColor(R.color.grey_8E))
                }
            }

            univ.observe(this@MyFragment){
                binding.tvUniv.text = it
            }

            verified.observe(this@MyFragment){
                if(it){
                    binding.ivUnivCertified.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_certified))
                } else {
                    binding.ivUnivCertified.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_non_certified))
                }
            }

            major.observe(this@MyFragment){
                binding.tvMajor.text = it
            }

            birthYear.observe(this@MyFragment){
                binding.tvYear.text = getString(R.string.my_birth_year, it.takeLast(2))
            }
        }

    }

}