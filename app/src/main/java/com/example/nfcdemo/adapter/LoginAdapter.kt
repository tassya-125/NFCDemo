package com.example.nfcdemo.adapter



import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LoginAdapter(fragmentActivity: FragmentActivity,fragments :List<Fragment> ) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = fragments

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}