package com.example.nfcdemo.model

import androidx.annotation.Nullable

// 定义一个普通类 User
class User(private val id: Long, private val userName: String,private val email :String,private val phoneNumber :String , private var password: String) {

    // 访问器方法
    fun getId(): Long {
        return id
    }

    fun getUserName(): String {
        return userName
    }

    fun getPassword(): String {
        return password
    }


}
