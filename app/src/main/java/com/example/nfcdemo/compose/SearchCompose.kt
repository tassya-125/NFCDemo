package com.example.nfcdemo.compose



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    val items = listOf(
        Item("Item 1", null, true),
        Item("Item 2", null, false),
        Item("Item 3", null, true)
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.Blue, RoundedCornerShape(8.dp)),
                placeholder = { Text("Please input", color = Color.Gray) },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor =  Color(0xFFE3F2FD), // 设置聚焦时边框颜色为浅蓝色
                    unfocusedBorderColor = Color(0xFF1E88E5) // 设置未聚焦时边框颜色为白色// 设置未聚焦时边框颜色
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* 执行搜索操作 */ },
                colors = ButtonDefaults.buttonColors( Color(0xFF42A5F5)),
                modifier = Modifier.clip(RoundedCornerShape(8.dp))
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Search", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ItemList(items)
    }
}

@Composable
fun ItemList(items: List<Item>) {
    Column {
        items.forEach { item ->
            ItemRow(item)
        }
    }
}

@Composable
fun ItemRow(item: Item) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.imageRes != null) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = item.name, fontSize = 20.sp, color = Color.Black, modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (item.isGenuine) Color(0xFFE3F2FD) else Color(0xFFFFEBEE))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (item.isGenuine) "正品" else "假的",
                fontSize = 14.sp,
                color = if (item.isGenuine) Color(0xFF1E88E5) else Color(0xFFD32F2F)
            )
        }
    }
}

data class Item(val name: String, val imageRes: Int?, val isGenuine: Boolean)