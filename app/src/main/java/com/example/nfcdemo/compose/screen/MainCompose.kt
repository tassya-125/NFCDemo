package com.example.nfcdemo.compose.screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfcdemo.MainActivity
import com.example.nfcdemo.R
import com.example.nfcdemo.compose.SearchScreen
import com.example.nfcdemo.compose.UserProfileScreen
import com.example.nfcdemo.util.ConstantUtil

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun MainScreen() {


    var currentPage by remember { mutableStateOf(ConstantUtil.PAGE_HOME) }

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
            AnimatedScreenTransition(targetPage = currentPage)
        }
    }
}

@Composable
private fun AnimatedScreenTransition(targetPage: Int) {
    val activity = LocalContext.current as MainActivity

    AnimatedContent(
        targetState = targetPage,
        transitionSpec = {
            if (targetState > initialState) {
                (scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + fadeIn())
                    .togetherWith(scaleOut() + fadeOut())
            } else {
                (scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + fadeIn())
                    .togetherWith(scaleOut() + fadeOut())
            }.using(SizeTransform(clip = false))
        },
        label = "ScreenTransition"
    ) { page ->
        when (page) {
            ConstantUtil.PAGE_USER -> UserProfileScreen()
            ConstantUtil.PAGE_HOME -> NFCCheckScreen(activity)
            ConstantUtil.PAGE_SEARCH -> SearchScreen()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNavigationBar(
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .height(72.dp)
            .clip(MaterialTheme.shapes.medium),
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            NavItem(
                icon = Icons.Default.Home,
                label = stringResource(R.string.home),
                page = ConstantUtil.PAGE_HOME
            ),
            NavItem(
                icon = Icons.Default.Search,
                label = stringResource(R.string.search),
                page = ConstantUtil.PAGE_SEARCH
            ),
            NavItem(
                icon = Icons.Default.Person,
                label = stringResource(R.string.profile),
                page = ConstantUtil.PAGE_USER
            )
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = currentPage == item.page,
                onClick = { onPageSelected(item.page) },
                icon = {
                    AnimatedNavIcon(
                        icon = item.icon,
                        isSelected = currentPage == item.page
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                    selectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(if (isSelected) 48.dp else 40.dp)
                .background(
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    } else {
                        Color.Transparent
                    },
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

private data class NavItem(
    val icon: ImageVector,
    val label: String,
    val page: Int
)
