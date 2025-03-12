package com.example.nfcdemo.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NFCCheckScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 使用径向渐变和阴影来打造炫酷大圆
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF8E2DE2), // 渐变起始色：紫色
                                Color(0xFF4A00E0)  // 渐变结束色：深蓝紫色
                            )
                        ),
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "开始 NFC 检验",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNFCCheckScreen() {
    NFCCheckScreen()
}
