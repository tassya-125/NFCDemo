package com.example.nfcdemo.compose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nfcdemo.compose.components.AuthInputField
import com.example.nfcdemo.compose.components.PasswordInputField
import com.example.nfcdemo.compose.components.VerificationCodeRow
import com.example.nfcdemo.viewmodel.AuthViewModel


@Composable
fun LoginRegisterScreen(onLoginSuccess: () -> Unit) {
    var authViewModel : AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    var identifier  by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var isUsingPhone by remember { mutableStateOf(true) }
    var code by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.LoginSuccess -> {
                onLoginSuccess()
            }
            else -> {
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isLogin) "登录" else "注册",
                fontSize = 32.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if(!isLogin) ((if (isUsingPhone) "使用邮箱" else "使用手机号") + "注册") else "",
                color = Color(0xFF2196F3),
                modifier = Modifier.clickable { isUsingPhone = !isUsingPhone }
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuthInputField(
                label = if(isLogin)  "用户名/手机号/邮箱" else if (isUsingPhone) "手机号" else "邮箱",
                icon = if (isUsingPhone) Icons.Default.Phone else Icons.Default.Email,
                value =   identifier   ,
                onValueChange= {newValue ->
                    identifier = newValue
                }
            )

            if (!isLogin ) {
                Spacer(modifier = Modifier.height(8.dp))
                VerificationCodeRow(code=code){
                        newValue->code= newValue
                }
            }

            PasswordInputField(password = password) { newValue -> password = newValue }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if(isLogin){
                        authViewModel.login(identifier,password)
                    }else{
                        authViewModel.register(identifier,password,code,isUsingPhone)
                    }
                    Log.e("auth","status : ${authViewModel.authState.value}")

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text(text = if (isLogin) "登录" else "注册", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isLogin) "没有账号？立即注册" else "已有账号？立即登录",
                color = Color(0xFF2196F3),
                modifier = Modifier.clickable { isLogin = !isLogin }
            )
        }
    }
}

