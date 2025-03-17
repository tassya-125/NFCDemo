package com.example.nfcdemo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
    fun LoginRegisterScreen() {
        var isLogin by remember { mutableStateOf(false) }
        var isUsingPhone by remember { mutableStateOf(true) }
        var countdown by remember { mutableStateOf(0) }
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
                    (if (isUsingPhone) "使用邮箱" else "使用手机号") + (if (isLogin) "登录" else "注册"),
                    color = Color(0xFF2196F3),
                    modifier = Modifier.clickable { isUsingPhone = !isUsingPhone }
                )

                Spacer(modifier = Modifier.height(8.dp))

                InputField(
                    label = if (isUsingPhone) "请输入手机号" else "请输入邮箱",
                    icon = if (isUsingPhone) Icons.Default.Phone else Icons.Default.Email
                )

                if (!isLogin ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    VerificationCodeRow()
                }

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

@Composable
fun VerificationCodeRow() {
    var code by remember { mutableStateOf("") }
    var isCountingDown by remember { mutableStateOf(false) }
    var countdownTime by remember { mutableStateOf(60) }

    // 倒计时逻辑
    LaunchedEffect(isCountingDown) {
        if (isCountingDown) {
            while (countdownTime > 0) {
                delay(1000L)
                countdownTime--
            }
            isCountingDown = false
            countdownTime = 60
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 验证码输入框
        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("输入验证码") },
            modifier = Modifier.weight(1f)
        )

        // 发送验证码按钮
        Button(
            onClick = {
                // 处理发送验证码的逻辑
                isCountingDown = true
            },
            enabled = !isCountingDown,
            shape = RectangleShape, // 设置为无圆角矩形
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier = Modifier.height(56.dp).absolutePadding(top = 5.dp) // 确保按钮与输入框高度一致

        ) {
            Text(
                text = if (isCountingDown) "$countdownTime 秒后重试" else "发送验证码"
            )
        }
    }
}

    @Preview
    @Composable
    fun PreviewLoginRegisterScreen() {
        LoginRegisterScreen()
    }