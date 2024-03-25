package com.studentcenter.weave.presentation.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationBarView
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.invitationCode
import com.studentcenter.weave.core.GlobalApplication.Companion.isFinish
import com.studentcenter.weave.core.GlobalApplication.Companion.loginState
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.core.GlobalApplication.Companion.networkState
import com.studentcenter.weave.core.GlobalApplication.Companion.registerToken
import com.studentcenter.weave.databinding.ActivityMainBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.team.EnterTeamUseCase
import com.studentcenter.weave.domain.usecase.team.GetTeamByInvitationCodeUseCase
import com.studentcenter.weave.presentation.base.BaseActivity
import com.studentcenter.weave.presentation.util.CustomDialog
import com.studentcenter.weave.presentation.util.CustomDialog.DialogType
import com.studentcenter.weave.presentation.util.NetworkDialog
import com.studentcenter.weave.presentation.view.chat.MatchFragment
import com.studentcenter.weave.presentation.view.home.DetailFragment
import com.studentcenter.weave.presentation.view.home.HomeFragment
import com.studentcenter.weave.presentation.view.my.MyFragment
import com.studentcenter.weave.presentation.view.request.RequestFragment
import com.studentcenter.weave.presentation.view.team.TeamFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var alertDialog: AlertDialog.Builder? = null
    private var networkDialog: NetworkDialog? = null

    override fun init() {
        loginState = true

        if(invitationCode == null){ // 초대장 없음
            if(registerToken != null) { // 첫 입장
                CustomDialog.getInstance(DialogType.REGISTER, null).apply {
                    setOnOKClickedListener {
                        naviItemChange(4)
                    }
                }.show(supportFragmentManager, "register_dialog")
                registerToken = null
            }
        } else { // 초대장 있음
            if(registerToken == null) showInvitation(false) else showInvitation(true)
        }

        // 네트워크 연결 상태 다이얼로그
        networkState.observe(this){
            if(!it){
                networkDialog = null // 여러 개

                networkDialog = NetworkDialog()
                networkDialog?.show(supportFragmentManager, "network_dialog")
            }
        }

        // View Setting
        binding.bottomNavi.itemIconTintList = null
        binding.bottomNavi.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        changeIconOfNaviMy()

        binding.bottomNavi.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navi_match->{
                    Log.i(TAG, "chat")
                    replaceFragment(MatchFragment())
                }
                R.id.navi_request->{
                    Log.i(TAG, "request")
                    replaceFragment(RequestFragment())
                }
                R.id.navi_home->{
                    Log.i(TAG, "home")
                    replaceFragment(HomeFragment())
                }
                R.id.navi_team->{
                    Log.i(TAG, "team")
                    replaceFragment(TeamFragment())
                }
                R.id.navi_my->{
                    Log.i(TAG, "my")
                    replaceFragment(MyFragment())
                }
            }
            true
        }

        binding.bottomNavi.setOnItemReselectedListener {
            when(it.itemId){
                R.id.navi_match->{}
                R.id.navi_request->{}
                R.id.navi_home->{}
                R.id.navi_team->{}
                R.id.navi_my->{}
            }
        }

        naviItemChange(2) // 첫 화면

        if(intent.getStringExtra("teamId") != null){
            comeFromKakaoLink(intent.getStringExtra("teamId") ?: "")
        }

        isFinish.observe(this){
            Log.i("FINISH", it.toString())
            if(it){
                isFinish.value = false
                loginState = false

                val intent = Intent(this, StartActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finishAffinity()
            }
        }
    }

    private fun comeFromKakaoLink(teamId: String){
        if(teamId.isNotEmpty()){
            replaceFragmentWithStack(DetailFragment(teamId))
        }
    }

    private fun showInvitation(isFirst: Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetTeamByInvitationCodeUseCase().getTeamByInvitationCode(accessToken, invitationCode!!)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        val dialogType = if(isFirst) DialogType.TEAM_INVITATION_FIRST else DialogType.TEAM_INVITATION

                        CustomDialog.getInstance(dialogType, res.data.teamIntroduce).apply {
                            setOnOKClickedListener {
                                enterTeam(isFirst)
                            }
                        }.show(supportFragmentManager, "invitation_dialog")
                    }
                }
                is Resource.Error -> {
                    Log.e(TAG, "초대장 에러: ${res.message}")
                }
                else -> {}
            }
        }
    }

    private fun enterTeam(isFirst: Boolean){
        CoroutineScope(Dispatchers.IO).launch{
            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = EnterTeamUseCase().enterTeamByInvitationCode(accessToken, invitationCode!!)){
                is Resource.Success -> {
                    launch(Dispatchers.Main){
                        naviItemChange(3)
                    }
                }
                is Resource.Error -> {
                    launch(Dispatchers.Main){
                        if(res.message == "팀원의 수가 초과되었습니다"){
                            val dialogType = if(isFirst) DialogType.TEAM_NO_SPACE_FIRST else DialogType.TEAM_NO_SPACE

                            CustomDialog.getInstance(dialogType, null).apply {
                                setOnOKClickedListener {
                                    if(it == "no_space") naviItemChange(3) else naviItemChange(4)
                                }
                            }.show(supportFragmentManager, "no_space_team")
                        } else {
                            Toast.makeText(this@MainActivity, res.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> {}
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fl_main, fragment).commit()
    }

    fun replaceFragmentWithStack(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun naviItemChange(index: Int){
        binding.bottomNavi.selectedItemId = binding.bottomNavi.menu[index].itemId
    }

    fun changeIconOfNaviMy() {
        val menu = binding.bottomNavi.menu
        val menuItem = menu.findItem(R.id.navi_my)

        Glide.with(this)
            .load(myInfo?.avatar ?: "")
            .circleCrop()
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    menuItem.icon = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    // 바텀 네비 숨김/보기
    fun setNaviVisible(p: Boolean){
        binding.bottomNavi.visibility = if(p) View.VISIBLE else View.GONE
    }

    fun checkGalleryPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (shouldShowRequestPermissionRationale(permission[0])) {
            showPermissionRationale("설정에서 저장소 권한을 추가해야 합니다.")
        } else {
            ActivityCompat.requestPermissions(this, permission, 100)
        }
    }

    fun checkCameraPermission(): Boolean{
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCameraPermission() {
        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
            showPermissionRationale("설정에서 카메라 권한을 추가해야 합니다.")
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        }
    }


    private fun showPermissionRationale(msg: String) {
        alertDialog = AlertDialog.Builder(this)
        alertDialog?.setMessage(msg)
        alertDialog?.setPositiveButton("확인") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        alertDialog?.setNegativeButton("취소") { _, _ ->
        }

        alertDialog?.show()

    }

}