package com.example.nfcdemo.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun VerificationCodeRow(code:String,valueChange: (String) -> Unit) {

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
            onValueChange = valueChange,
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