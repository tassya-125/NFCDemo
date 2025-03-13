package com.example.nfcdemo.compose



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcdemo.util.ConstantUtil



    @Preview
    @Composable
    fun MainScreen() {
        var currentPage by remember { mutableStateOf(2) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(currentPage) { newPage ->
                    currentPage = newPage
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (currentPage) {
                    ConstantUtil.PAGE_USER -> UserProfileScreen()
                    ConstantUtil.PAGE_HOME -> NFCCheckScreen()
                    ConstantUtil.PAGE_SEARCH -> SearchScreen()
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(currentPage: Int, onPageSelected: (Int) -> Unit) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val items = listOf(
                Triple(Icons.Default.Home, "主页", ConstantUtil.PAGE_HOME),
                Triple(Icons.Default.Search, "搜索", ConstantUtil.PAGE_SEARCH),
                Triple(Icons.Default.Person, "用户", ConstantUtil.PAGE_USER)
            )

            items.forEachIndexed { index, item ->
                val selected = currentPage == item.third
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onPageSelected(item.third) }
                        .background(
                            if (selected) Color(0xFF81D4FA).copy(alpha = 0.2f)
                            else Color.Transparent
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = item.first,
                            contentDescription = item.second,
                            tint =   if (selected) Color(0xFF81D4FA).copy(alpha = 0.6f)
                            else Color.Black.copy(0.6f)
                        )
                        Text(
                            text = item.second,
                            color =    if (selected) Color(0xFF81D4FA).copy(alpha = 0.6f)
                            else Color.Black.copy(0.6f)
                        )
                    }
                }
            }
        }
    }



    @Composable
    fun SEARCHScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "设置页面", fontSize = 24.sp)
        }
    }