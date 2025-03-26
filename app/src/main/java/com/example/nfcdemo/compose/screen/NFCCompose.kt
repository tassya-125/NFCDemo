package com.example.nfcdemo.compose.screen

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.nfcdemo.MainActivity
import com.example.nfcdemo.R
import com.example.nfcdemo.compose.components.LoadingIndicator
import com.example.nfcdemo.util.NFCUtil
import kotlin.math.log

@Composable
fun NFCCheckScreen(activity: MainActivity) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    activity.setNfcListener { data -> Log.d("NFC_DATA",data)}
    NFCUtil.enableNfcForegroundDispatch(activity)
//    DisposableEffect(Unit) {
//        onDispose {
//            NFCUtil.disableNfcForegroundDispatch(activity) // 退出页面时执行
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

            // 背景光环
            Canvas(
                modifier = Modifier
                    .size(280.dp)
                    .graphicsLayer { alpha = pulseAlpha }
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4A00E0).copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    radius = size.width / 1.8f
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                        spotColor = Color(0xFFB39DDB).copy(alpha = 0.3f) // 浅紫色阴影
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFA45DEB),  // 浅紫色
                                Color(0xFF6D5DEB)   // 浅蓝紫色
                            )
                        ),
                        shape = CircleShape
                    )
                    .drawWithCache {
                        onDrawBehind {
                            rotate(rotation) {
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.2f), // 调高透明度
                                    center = Offset(size.width / 2, size.height / 2),
                                    radius = size.width / 2,
                                    style = Stroke(width = 4.dp.toPx())
                                )
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.nfc_scan),
                    contentDescription = "NFC Icon",
                    tint = Color.White.copy(alpha = 0.95f), // 微调图标透明度
                    modifier = Modifier.size(80.dp)
                )
            }


            // 文字内容
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "NFC 检验",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "请将设备靠近 NFC 标签",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

               LoadingIndicator()
            }
        }
    }
}



