package com.example.nfcdemo.util

import android.util.Log
import com.example.nfcdemo.model.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

object UserManager {
    private val mmkv by lazy { MMKV.defaultMMKV() }

    private val TAG = "USER"

    // 保存用户数据
    fun saveUser(user: User, token: String) {
        mmkv.encode("user_json", Gson().toJson(user))
        mmkv.encode("auth_token", token)
    }

    // 获取当前用户
    fun getCurrentUser(): User? {
       val user=  mmkv.decodeString("user_json")?.let {
            Gson().fromJson(it, User::class.java)
        }
        Log.d(TAG,user.toString())
        return user;
    }

    // 获取认证 token
    fun getAuthToken(): String? = mmkv.decodeString("auth_token")

    // 检查登录状态
    fun isLoggedIn(): Boolean = !getAuthToken().isNullOrEmpty()

    // 退出登录
    fun logout() {
        mmkv.remove("user_json")
        mmkv.remove("auth_token")
    }
}
