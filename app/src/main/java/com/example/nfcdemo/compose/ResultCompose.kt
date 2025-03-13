package com.example.nfcdemo.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun ProductStatus(isAuthentic: Boolean = true) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 根据isAuthentic值来决定显示什么
        if (isAuthentic) {
            // 正品
            Text(
                text = "正品",
                color = Color.Green,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "点击查看相关信息",
                color = Color(0xFFE3F2FD),
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* 点击逻辑 */ }
            )
        } else {
            // 假货
            Text(
                text = "假的",
                color = Color.Red,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
