package com.weave.weave.presentation.view.my

import android.content.Intent
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.weave.weave.presentation.base.BaseFragment
import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.databinding.FragmentSettingBinding
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.domain.usecase.auth.LogOutUseCase
import com.weave.weave.domain.usecase.auth.UnRegisterUserUseCase
import com.weave.weave.presentation.util.CustomDialog
import com.weave.weave.presentation.view.MainActivity
import com.weave.weave.presentation.view.StartActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingFragment: BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        logout()
        unlink()
    }

    private fun unlink(){
        binding.ibUnlink.setOnClickListener {
            val dialog = CustomDialog.getInstance(CustomDialog.DialogType.UNLINK, null)
            dialog.setOnOKClickedListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                    when(val res = UnRegisterUserUseCase().unregisterUser(accessToken)){
                        is Resource.Success -> {
                            UserApiClient.instance.unlink { error ->
                                if(error != null){
                                    Log.e("Auth", "Kakao Unlink Error: ${error.message}")
                                } else {
                                    Log.i("Auth", "Kakao Unlink 성공")
                                    CoroutineScope(Dispatchers.Main).launch {
                                        moveStart()
                                    }
                                }
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "Unlink Error: ${res.message}")
                        }
                        else -> {}
                    }
                }
            }
            dialog.show(requireActivity().supportFragmentManager, "unlink")
        }
    }

    private fun logout(){
        binding.ibSignOut.setOnClickListener {
            val dialog = CustomDialog.getInstance(CustomDialog.DialogType.LOGOUT, null)
            dialog.setOnOKClickedListener {
                CoroutineScope(Dispatchers.IO).launch{
                    val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                    when(val res = LogOutUseCase().logOut(accessToken)){
                        is Resource.Success -> {
                            UserApiClient.instance.logout { error ->
                                if(error != null){
                                    Log.e("Auth", "Kakao Logout Error: ${error.message}")
                                } else {
                                    Log.i("Auth", "Kakao Logout 성공")
                                    CoroutineScope(Dispatchers.Main).launch {
                                        moveStart()
                                    }
                                }
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, "SignOut Error: ${res.message}")
                        }
                        else -> {}
                    }
                }
            }
            dialog.show(requireActivity().supportFragmentManager, "sign_out")
        }
    }

    private fun moveStart(){
        runBlocking {
            app.getUserDataStore().clearData()
        }

        if(context != null){
            val intent = Intent(requireContext(), StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}