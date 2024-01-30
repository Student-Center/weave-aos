package com.weave.weave.presentation.view


import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.registerToken
import com.weave.weave.databinding.ActivityMainBinding
import com.weave.weave.presentation.base.BaseActivity
import com.weave.weave.presentation.util.CustomDialog
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
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


    private fun changeIconOfNaviMy() {
        val menu = binding.bottomNavi.menu
        val menuItem = menu.findItem(R.id.navi_my)

        Glide.with(this)
            // 이후 유저 프로필 이미지 url로 설정해야함
            .load("https://i.namu.wiki/i/Y1OafcZddMvQImboc5DViDY9zWetYFznQWP-r0qSPvQCqPmPKnR1rCYVcL_uRzQOIXZTSNQpOK2Qt_1sjszyMg.webp")
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

}