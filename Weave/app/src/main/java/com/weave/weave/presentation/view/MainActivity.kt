package com.weave.weave.presentation.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.registerToken
import com.weave.weave.databinding.ActivityMainBinding
import com.weave.weave.presentation.base.BaseActivity
import com.weave.weave.presentation.util.CustomDialog
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
import com.weave.weave.presentation.view.chat.ChatFragment
import com.weave.weave.presentation.view.home.HomeFragment
import com.weave.weave.presentation.view.my.MyFragment
import com.weave.weave.presentation.view.request.RequestFragment
import com.weave.weave.presentation.view.team.TeamFragment

class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var alertDialog: AlertDialog.Builder? = null

    override fun init() {
        if(registerToken != null){
            CustomDialog.getInstance(CustomDialog.DialogType.REGISTER).show(supportFragmentManager, "registerDialog")
            registerToken = null
        }
    
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
    }

    private fun replaceFragment(fragment: Fragment) {
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

    fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (shouldShowRequestPermissionRationale(permission[0])) {
            showPermissionRationale("설정에서 권한을 추가해야 합니다.")
        } else {
            ActivityCompat.requestPermissions(this, permission, 100)
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