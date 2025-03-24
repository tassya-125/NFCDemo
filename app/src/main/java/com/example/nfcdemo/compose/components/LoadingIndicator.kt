package com.example.nfcdemo.compose.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val dotSize by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                8f at 0 with LinearEasing
                12f at 500 with LinearEasing
                8f at 1000 with LinearEasing
            }
        ), label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { index ->
            val delayedSize by infiniteTransition.animateFloat(
                initialValue = 8f,
                targetValue = 12f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        8f at (index * 200) with LinearEasing
                        12f at (500 + index * 200) with LinearEasing
                        8f at (1000 + index * 200) with LinearEasing
                    }
                ), label = ""
            )

            Box(
                modifier = Modifier
                    .size(delayedSize.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8E2DE2))
            )
        }
    }
}