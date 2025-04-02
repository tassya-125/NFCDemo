package com.example.nfcdemo.compose.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfcdemo.util.ConstantUtil
import com.example.nfcdemo.util.UserManager



@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val startDestination= if (UserManager.isLoggedIn()) ConstantUtil.Screen.Home.route else ConstantUtil.Screen.Auth.route
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ConstantUtil.Screen.Auth.route) {
            LoginRegisterScreen(
                onLoginSuccess = {
                    navController.navigate(ConstantUtil.Screen.Home.route) {
                        popUpTo(ConstantUtil.Screen.Auth.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(ConstantUtil.Screen.Home.route) {
            MainScreen()
        }
    }
}
