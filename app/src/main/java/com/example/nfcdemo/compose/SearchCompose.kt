package com.example.nfcdemo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 定义颜色常量
private val PrimaryBlue = Color(0xFF2196F3)
private val LightBlue = Color(0xFFE3F2FD)
private val DarkRed = Color(0xFFD32F2F)
private val CardBackground = Color(0xFFF5F5F5)
private val GradientStart = Color(0xFF42A5F5)
private val GradientEnd = Color(0xFF1976D2)

@Preview(showBackground = true)
@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    val items = listOf(
        Item("Premium Watch", null, true),
        Item("Designer Bag", null, false),
        Item("Smartphone X", null, true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue)
            .padding(horizontal = 16.dp)
    ) {
        // 搜索栏
        SearchBarSection(query, onQueryChange = { query = it })

        Spacer(modifier = Modifier.height(24.dp))

        // 物品列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items) { item ->
                ProductCard(item)
            }
        }
    }
}


@Composable
private fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .shadow(2.dp, RoundedCornerShape(16.dp)),
            placeholder = { Text("Search products...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = { /* 执行搜索 */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .shadow(4.dp, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun ProductCard(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图片区域
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (item.imageRes != null) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "🖼️",
                        fontSize = 32.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                AuthenticityBadge(isGenuine = item.isGenuine)
            }
        }
    }
}

@Composable
private fun AuthenticityBadge(isGenuine: Boolean) {
    val (text, color, bgColor) = if (isGenuine) {
        Triple("GENUINE", Color(0xFF1B5E20), Color(0xFFC8E6C9))
    } else {
        Triple("FAKE", DarkRed, Color(0xFFFFCDD2))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

data class Item(val name: String, val imageRes: Int?, val isGenuine: Boolean)
