package com.example.nfcdemo.compose.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfcdemo.MainActivity
import com.example.nfcdemo.model.PotteryEntity
import com.example.nfcdemo.util.ConstantUtil
import com.example.nfcdemo.util.NFCUtil
import com.example.nfcdemo.util.ToastUtil

@Composable
fun NFCApp(activity: MainActivity) {
    val navController = rememberNavController()
    var pottery: PotteryEntity?=null
    var isAuthentic = false

    DisposableEffect(Unit) {
        NFCUtil.enableNfcForegroundDispatch(activity)
        onDispose {
            NFCUtil.disableNfcForegroundDispatch(activity) // 退出页面时执行
        }
    }

    NavHost(navController = navController, startDestination = ConstantUtil.Screen.NFC_CHECK.route) {
        composable(ConstantUtil.Screen.NFC_CHECK.route) {
            NFCCheckScreen(activity){
                 pottery=it
                 pottery?.let {
                     isAuthentic=true
                 }
                 navController.navigate(ConstantUtil.Screen.Result.route) {
                    popUpTo(ConstantUtil.Screen.NFC_CHECK.route) {
                        inclusive = true
                    }
                 }

            }
        }
        composable(ConstantUtil.Screen.Result.route) {
            ProductStatus(isAuthentic){
                if(!isAuthentic){
                    ToastUtil.show(activity,"假的,无法查看详细信息",ToastUtil.ERROR)
                    return@ProductStatus
                }
                navController.navigate(ConstantUtil.Screen.Detail.route) {
                    popUpTo(ConstantUtil.Screen.Result.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(ConstantUtil.Screen.Detail.route) {
            pottery?.let { PotteryDetailScreen(it) }
        }
    }
}
