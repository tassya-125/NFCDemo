package com.example.nfcdemo.compose

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

@Preview(showBackground = true)
@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    val items = listOf(
        Item("Premium Watch", null, true),
        Item("Designer Bag", null, false),
        Item("Designer Bag", null, false),
        Item("Smartphone X", null, true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFEEEEEE), Color(0xFFBDBDBD))))
    ) {
        SearchBar(query, onQueryChange = { query = it })

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                ProductCard(item)
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
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
            placeholder = { Text("Search products...", color = Color.Gray) },
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
            onClick = {},
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
fun ProductCard(item: Item) {
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

data class Item(val name: String, val imageRes: Int?, val isGenuine: Boolean)