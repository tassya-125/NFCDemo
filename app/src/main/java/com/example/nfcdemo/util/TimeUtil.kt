package com.example.nfcdemo.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {

    private val TAG = "Time"

    fun formatDate(time: Any?): String {
        if (time == null) return "暂无时间"

        val date = when (time) {
            is Long -> Date(time)  // 处理时间戳
            is String -> try {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(time)
            } catch (e: Exception) {
                Log.e(TAG,"日期格式错误")
                null
            }
            is Date -> time
            else -> null
        } ?: return "日期格式错误"

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

}