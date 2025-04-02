package com.example.nfcdemo.compose.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfcdemo.MainActivity
import com.example.nfcdemo.compose.PotteryDetailScreen
import com.example.nfcdemo.compose.ProductStatus
import com.example.nfcdemo.model.PotteryEntity
import com.example.nfcdemo.util.ConstantUtil

@Composable
fun NFCApp(activity: MainActivity) {
    val navController = rememberNavController()
    var pottery:PotteryEntity?=null
    var isAuthentic :Boolean= false

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
