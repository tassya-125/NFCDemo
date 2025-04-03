package com.example.nfcdemo.util

import org.json.JSONArray
import org.json.JSONObject

object StringUtil {
    fun getOriginValue(valStr: String?): String {
        if (valStr.isNullOrEmpty() || !isValidJSON(valStr)) {
            return valStr ?: ""
        }
        return try {
            val jsonObj = JSONObject(valStr)
            val valuesArray = jsonObj.optJSONArray("values") ?: JSONArray()
            (0 until valuesArray.length()).joinToString("/") { valuesArray.getString(it) }
        } catch (e: Exception) {
            valStr
        }
    }

    fun isValidJSON(str: String): Boolean {
        return try {
            JSONObject(str) // 尝试解析 JSON 对象
            true
        } catch (e: Exception) {
            false
        }
    }

}