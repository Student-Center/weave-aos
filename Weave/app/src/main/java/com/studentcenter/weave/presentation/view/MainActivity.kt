package com.studentcenter.weave.presentation.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.registerToken
import com.studentcenter.weave.databinding.ActivityMainBinding
import com.studentcenter.weave.presentation.base.BaseActivity
import com.studentcenter.weave.presentation.util.CustomDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.navigation.NavigationBarView
import com.studentcenter.weave.core.GlobalApplication.Companion.isFinish
import com.studentcenter.weave.core.GlobalApplication.Companion.loginState
import com.studentcenter.weave.presentation.view.chat.ChatFragment
import com.studentcenter.weave.presentation.view.home.HomeFragment
import com.studentcenter.weave.presentation.view.my.MyFragment
import com.studentcenter.weave.presentation.view.request.RequestFragment
import com.studentcenter.weave.presentation.view.team.TeamFragment

class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var alertDialog: AlertDialog.Builder? = null

    override fun init() {
        if(registerToken != null){
            CustomDialog.getInstance(CustomDialog.DialogType.REGISTER, null).show(supportFragmentManager, "registerDialog")
            registerToken = null
        }

        loginState = true

        binding.bottomNavi.itemIconTintList = null
        binding.bottomNavi.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        changeIconOfNaviMy()
        binding.bottomNavi.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navi_chat->{
                    Log.i(TAG, "chat")
                    replaceFragment(ChatFragment())
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
                R.id.navi_chat->{}
                R.id.navi_request->{}
                R.id.navi_home->{}
                R.id.navi_team->{}
                R.id.navi_my->{}
            }
        }

        isFinish.observe(this){
            Log.i("FINISH", it.toString())
            if(it){
                isFinish.value = false

                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
                finishAffinity()
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


    private fun changeIconOfNaviMy() {
        val menu = binding.bottomNavi.menu
        val menuItem = menu.findItem(R.id.navi_my)

        Glide.with(this)
            // 이후 유저 프로필 이미지 url로 설정해야함
            .load("")
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
                this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
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