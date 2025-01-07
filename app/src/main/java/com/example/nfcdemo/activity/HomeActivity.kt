package com.example.nfcdemo.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.nfcdemo.R
import com.example.nfcdemo.adapter.LoginAdapter
import com.example.nfcdemo.fragment.HomeFragment
import com.example.nfcdemo.fragment.LoginFragment
import com.example.nfcdemo.fragment.MainFragment
import com.example.nfcdemo.fragment.RegisterByEmailFragment
import com.example.nfcdemo.fragment.RegisterByPhoneFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {
    val tabs =  listOf("主页","我的")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val viewPager2 = findViewById<ViewPager2>(R.id.viewpager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        val fragments = listOf(MainFragment(), HomeFragment())
        val adapter = LoginAdapter(this,fragments)

        viewPager2.adapter = adapter

        // 将 TabLayout 和 ViewPager2 关联
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text =  tabs[position]// 设置 Tab 的标题
        }.attach()
    }
}