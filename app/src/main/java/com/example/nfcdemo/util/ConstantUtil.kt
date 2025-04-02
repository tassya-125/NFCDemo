package com.example.nfcdemo.util

object ConstantUtil {
        /*
          当前主页
         */
        const val PAGE_USER = 0
        const val PAGE_HOME = 1
        const val PAGE_SEARCH = 2

        sealed class Screen(val route: String) {
                object Auth : Screen("auth")
                object Home : Screen("home")
                object NFC_CHECK:Screen("nfc_check")
                object Result:Screen("result")
                object Detail:Screen("detail")
        }



}