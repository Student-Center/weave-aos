package com.studentcenter.weave.presentation.view.my

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.presentation.base.BaseFragment
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.databinding.FragmentSettingBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.auth.LogOutUseCase
import com.studentcenter.weave.domain.usecase.auth.UnRegisterUserUseCase
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.view.MainActivity
import com.studentcenter.weave.presentation.view.StartActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingFragment: BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {
    override fun init() {
        (requireActivity() as MainActivity).setNaviVisible(false)

        binding.tvVersion.text = getString(R.string.version_num, BuildConfig.VERSION_NAME)
        binding.tvUserId.text = myInfo?.id ?: "error"

        binding.ibBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.ibPrivacy.setOnClickListener { openInternet(BuildConfig.PRIVACY_POLICY_URL) }
        binding.ibTos.setOnClickListener { openInternet(BuildConfig.TOS_URL) }

        logout()
        unlink()

        binding.ibUserId.setOnClickListener {
            copyUserId()
        }
    }

    private fun copyUserId(){
        val clipboardManager = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", myInfo?.id ?: "error"))

//        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
//            Toast.makeText(requireContext(), "ID가 복사되었어요.", Toast.LENGTH_SHORT).show()
//        }
        Toast.makeText(requireContext(), "ID가 복사되었어요.", Toast.LENGTH_SHORT).show()
    }

    private fun openInternet(url: String){
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if(intent.resolveActivity(requireActivity().packageManager) != null){
            startActivity(intent)
        }
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
                                    myInfo = null
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