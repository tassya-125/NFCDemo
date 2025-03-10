package com.example.nfcdemo.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


    @Composable
    fun LoginRegisterScreen() {
        var isLogin by remember { mutableStateOf(true) }
        var isUsingPhone by remember { mutableStateOf(true) }

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
                    text = if (isUsingPhone) "使用邮箱登录" else "使用手机号登录",
                    color = Color(0xFF2196F3),
                    modifier = Modifier.clickable { isUsingPhone = !isUsingPhone }
                )

                Spacer(modifier = Modifier.height(8.dp))

                InputField(
                    label = if (isUsingPhone) "请输入手机号" else "请输入邮箱",
                    icon = if (isUsingPhone) Icons.Default.Phone else Icons.Default.Email
                )

                PasswordField()

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle authentication */ },
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

    @Composable
    fun InputField(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(label, color = Color.Gray) },
            leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = Color.Black) },
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun PasswordField() {
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("请输入密码", color = Color.Gray) },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.Black) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Preview
    @Composable
    fun PreviewLoginRegisterScreen() {
        LoginRegisterScreen()
    }