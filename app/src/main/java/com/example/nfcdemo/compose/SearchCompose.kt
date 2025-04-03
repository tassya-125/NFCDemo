package com.example.nfcdemo.compose

import android.net.Uri
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.nfcdemo.network.data.response.VerificationHistoryResponse
import com.example.nfcdemo.viewmodel.VerificationHistoryViewModel

@Preview(showBackground = true)
@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }

    val viewModel : VerificationHistoryViewModel =  viewModel()
    val items = viewModel.pager.collectAsLazyPagingItems()
    items.refresh()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEEEEEE), Color(0xFFBDBDBD))))
    ) {
        SearchBar(query, onQueryChange = { query = it },viewModel)

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items.itemCount) { index ->
                items[index]?.let { ProductCard(it) }
            }

            // 加载状态
            items.apply {
                when {
                    loadState.append is LoadState.Loading -> item { CircularProgressIndicator() }
                    loadState.append is LoadState.Error -> item { Text("加载失败") }
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit,viewModel: VerificationHistoryViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp)),
            placeholder = { Text("查看验证历史", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF1976D2)
                )
            },
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = {viewModel.refresh()},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
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
fun ProductCard(item: VerificationHistoryResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFE3F2FD))))
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
                    .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (item.pottery?.imageUrl != null) {
                    AsyncImage(
                        model = Uri.parse(item.pottery.imageUrl),
                        contentDescription = "紫砂壶图片",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(12.dp))
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
                    text = item.pottery?.potteryName?:"无",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                AuthenticityBadge(isGenuine = item.verificationResult)
            }
        }
    }
}

@Composable
fun AuthenticityBadge(isGenuine: Boolean) {
    val (text, textColor, bgColor) = if (isGenuine) {
        Triple("GENUINE", Color(0xFF388E3C), Color(0xFFC8E6C9))
    } else {
        Triple("FAKE", Color(0xFFD32F2F), Color(0xFFFFCDD2))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

