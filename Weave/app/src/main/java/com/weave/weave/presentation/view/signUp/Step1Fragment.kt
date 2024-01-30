package com.weave.weave.presentation.view.signUp

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.databinding.FragmentSignUpStep1Binding
import com.weave.weave.presentation.util.CustomDialog
import com.weave.weave.presentation.viewmodel.SignUpViewModel

class Step1Fragment: BaseFragment<FragmentSignUpStep1Binding>(R.layout.fragment_sign_up_step_1) {
    private val viewModel: SignUpViewModel by activityViewModels()
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
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.appBar.ibAppBarSignUpBack.isVisible = false
        binding.appBar.ibAppBarSignUpCancel.setOnClickListener {
            CustomDialog.getInstance(CustomDialog.DialogType.SIGN_UP_CANCEL).show(requireActivity().supportFragmentManager, "cancelDialog")
        }

        binding.ivBoyChecked.setOnClickListener{
            viewModel.setBoyChecked(!viewModel.boyChecked.value!!)
        }

        binding.ivGirlChecked.setOnClickListener {
            viewModel.setGirlChecked(!viewModel.girlChecked.value!!)
        }

        binding.ibNext.setOnClickListener {
            if(viewModel.nextBtn.value!!){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_sign_up, Step2Fragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        // test
//        CoroutineScope(Dispatchers.IO).launch {
//            app.getUserDataStore().getLoginToken().collect { value ->
//                Log.i(TAG, "${value.accessToken} / ${value.refreshToken}" )
//            }
//        }


//        val dataObserver = Observer { resource: Resource<UserEntity> ->
//            when (resource) {
//                is Resource.Success -> {
//                    // 성공적으로 데이터를 받아온 경우
//                    val yourDataModel = resource.data
//                    // UI에 데이터를 표시하거나 필요한 작업을 수행
//                    // 예: textView.text = yourDataModel.someProperty
//                    Log.i(TAG, "SUCCESS -> ${yourDataModel.title} / ${yourDataModel.price}")
//                }
//                is Resource.Error -> {
//                    // 오류가 발생한 경우
//                    val errorMessage = resource.message
//                    // UI에 오류 메시지를 표시하거나 필요한 작업을 수행
//                    // 예: showErrorDialog(errorMessage)
//                    Log.i(TAG, "ERROR -> $errorMessage")
//                }
//                is Resource.Loading -> {
//                    // 데이터를 불러오는 중인 경우
//                    // UI에 로딩 스피너를 표시하거나 필요한 작업을 수행
//                    // 예: showLoadingIndicator()
//                    Log.i(TAG, "LOADING")
//                }
//            }
//        }
//
//        // Observer를 ViewModel의 LiveData에 연결
//        viewModel.data.observe(this, dataObserver)
//
//        viewModel.getUsers()
    }
}