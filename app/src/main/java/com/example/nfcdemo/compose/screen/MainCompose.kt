package com.example.nfcdemo.compose.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcdemo.MainActivity
import com.example.nfcdemo.R
import com.example.nfcdemo.compose.SearchScreen
import com.example.nfcdemo.compose.UserProfileScreen
import com.example.nfcdemo.util.ConstantUtil


@Composable
fun MainScreen(onLogOut:()->Unit) {
    var currentPage by remember { mutableStateOf(ConstantUtil.PAGE_HOME) }
    val activity = LocalContext.current as MainActivity
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedNavigationBar(
                currentPage = currentPage,
                onPageSelected = { currentPage = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AnimatedScreenTransition(targetPage = currentPage,activity,onLogOut)
        }
    }
}

@Composable
private fun AnimatedScreenTransition(targetPage: Int, activity: MainActivity, onLogOut: () -> Unit) {
    AnimatedContent(
        targetState = targetPage,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
            } else {
                slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
            }.using(SizeTransform(clip = false))
        },
        label = "ScreenTransition"
    ) { page ->
        when (page) {
            ConstantUtil.PAGE_USER -> UserProfileScreen(onLogOut)
            ConstantUtil.PAGE_HOME -> NFCApp(activity)
            ConstantUtil.PAGE_SEARCH -> SearchScreen()
        }
    }
}

@Composable
fun AnimatedNavigationBar(
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .height(72.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)),
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            NavItem(Icons.Default.Home, stringResource(R.string.home), ConstantUtil.PAGE_HOME),
            NavItem(Icons.Default.Search, stringResource(R.string.search), ConstantUtil.PAGE_SEARCH),
            NavItem(Icons.Default.Person, stringResource(R.string.profile), ConstantUtil.PAGE_USER)
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = currentPage == item.page,
                onClick = { onPageSelected(item.page) },
                icon = {
                    AnimatedNavIcon(icon = item.icon, isSelected = currentPage == item.page)
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                ),
                alwaysShowLabel = true
            )
        }
    }
}

@Composable
private fun AnimatedNavIcon(
    icon: ImageVector,
    isSelected: Boolean
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f, animationSpec = tween(300))

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private data class NavItem(
    val icon: ImageVector,
    val label: String,
    val page: Int
)