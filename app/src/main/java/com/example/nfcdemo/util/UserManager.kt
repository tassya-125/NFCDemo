package com.example.nfcdemo.util

import android.media.session.MediaSession.Token
import android.util.Log
import com.example.nfcdemo.model.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

object UserManager {
    private val mmkv by lazy { MMKV.defaultMMKV() }

    private const val TAG = "USER"

    private var USER_CACHE:User?= null

    // 保存用户数据
    fun saveUser(user: User, token: String) {
        USER_CACHE=user
        mmkv.encode("user_json", Gson().toJson(user))
        mmkv.encode("auth_token", token)
    }

    // 获取当前用户
    fun getCurrentUser(): User? {
        USER_CACHE?.let{
            return it
        }
        val user=  mmkv.decodeString("user_json")?.let {
            Gson().fromJson(it, User::class.java)
        }
        Log.d(TAG,user.toString())
        return user;
    }

    fun getAuthToken(): String? = mmkv.decodeString("auth_token")

    // 检查登录状态
    fun isLoggedIn(): Boolean = !getAuthToken().isNullOrEmpty()

    fun getUserId():Long?{
        return getCurrentUser()?.id
    }

    // 退出登录
    fun logout() {
        USER_CACHE=null
        mmkv.remove("user_json")
        mmkv.remove("auth_token")
    }
}
