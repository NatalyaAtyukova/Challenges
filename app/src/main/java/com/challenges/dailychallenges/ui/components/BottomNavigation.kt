package com.challenges.dailychallenges.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.challenges.dailychallenges.R
import com.challenges.dailychallenges.ui.navigation.Screen
import com.challenges.dailychallenges.ui.theme.*

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun BottomNavigation(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem(
            route = Screen.Main.route,
            title = stringResource(R.string.home),
            icon = Icons.Filled.Home,
            color = NeonGreen
        ),
        BottomNavItem(
            route = Screen.MyChallenges.route,
            title = stringResource(R.string.my_challenges),
            icon = Icons.Filled.FitnessCenter,
            color = NeonBlue
        ),
        BottomNavItem(
            route = Screen.Achievements.route,
            title = stringResource(R.string.achievements),
            icon = Icons.Filled.EmojiEvents,
            color = NeonPink
        ),
        BottomNavItem(
            route = Screen.Settings.route,
            title = stringResource(R.string.settings),
            icon = Icons.Filled.Settings,
            color = NeonYellow
        )
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Get screen width to adapt sizing
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val widthBasedPadding = if (screenWidth > 400.dp) 16.dp else 8.dp
    val iconSize = if (screenWidth > 400.dp) 28.dp else 24.dp
    
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { 
                it.route == item.route 
            } == true
            
            val animatedScale by animateFloatAsState(
                targetValue = if (selected) 1.2f else 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            
            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(iconSize)
                            .scale(animatedScale),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (selected) item.color else Color.Gray.copy(alpha = 0.6f),
                            modifier = Modifier.size(iconSize)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        color = if (selected) item.color else Color.Gray.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (selected) item.color.copy(alpha = 0.1f) else Color.Transparent,
                    selectedIconColor = item.color,
                    selectedTextColor = item.color,
                    unselectedIconColor = Color.Gray.copy(alpha = 0.6f),
                    unselectedTextColor = Color.Gray.copy(alpha = 0.6f)
                )
            )
        }
    }
} 