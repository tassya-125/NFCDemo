package com.example.nfcdemo.compose


import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.nfcdemo.util.ConstantUtil

class MainCompose {


    @Preview
    @Composable
    fun MainScreen() {
        var currentPage by remember { mutableStateOf(ConstantUtil.PAGE_SEARCH) }

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
                    ConstantUtil.PAGE_USER -> UserScreen()
                    ConstantUtil.PAGE_HOME -> NFCCheckScreen()
                    ConstantUtil.PAGE_SEARCH -> SearchScreen()
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(currentPage: Int, onPageSelected: (Int) -> Unit) {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("主页") },
                selected = currentPage == ConstantUtil.PAGE_HOME,
                onClick = { onPageSelected(ConstantUtil.PAGE_HOME) }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Search, contentDescription = "SEARCH") },
                label = { Text("搜索") },
                selected = currentPage == ConstantUtil.PAGE_SEARCH,
                onClick = { onPageSelected(ConstantUtil.PAGE_SEARCH) }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Person, contentDescription = "User") },
                label = { Text("用户") },
                selected = currentPage == ConstantUtil.PAGE_USER,
                onClick = { onPageSelected(ConstantUtil.PAGE_USER) }
            )
        }
    }



    @Composable
    fun SEARCHScreen() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "设置页面", fontSize = 24.sp)
        }
    }
}