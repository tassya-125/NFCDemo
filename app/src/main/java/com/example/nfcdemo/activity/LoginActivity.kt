package com.example.nfcdemo.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.nfcdemo.R
import com.example.nfcdemo.adapter.LoginAdapter
import com.example.nfcdemo.fragment.LoginFragment
import com.example.nfcdemo.fragment.RegisterByEmailFragment
import com.example.nfcdemo.fragment.RegisterByPhoneFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LoginActivity : AppCompatActivity() {

    val tabs = listOf("登录", "手机注册", "邮箱注册")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()

    }

    private fun initView() {
        val viewPager2 = findViewById<ViewPager2>(R.id.viewpager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        val fragments = listOf(LoginFragment(), RegisterByPhoneFragment(), RegisterByEmailFragment())
        val adapter = LoginAdapter(this,fragments)

        viewPager2.adapter = adapter

        // 将 TabLayout 和 ViewPager2 关联
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text =  tabs[position]// 设置 Tab 的标题
        }.attach()
    }


}