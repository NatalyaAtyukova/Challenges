package com.challenges.dailychallenges.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.challenges.dailychallenges.ui.navigation.Screen
import com.challenges.dailychallenges.ui.theme.*

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Main.route, "Главная", Icons.Default.Home, NeonBlue),
        BottomNavItem(Screen.Achievements.route, "Достижения", Icons.Default.EmojiEvents, NeonPurple),
        BottomNavItem(Screen.MyChallenges.route, "Мои челленджи", Icons.Default.FitnessCenter, NeonGreen),
        BottomNavItem(Screen.Community.route, "Сообщество", Icons.Default.Public, NeonPink),
        BottomNavItem(Screen.Profile.route, "Профиль", Icons.Default.Person, NeonYellow)
    )
    
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        DeepDarkBlue
                    )
                )
            ),
        containerColor = Color.Transparent
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            val color = if (selected) item.color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            val backgroundColor = if (selected) item.color.copy(alpha = 0.2f) else Color.Transparent
            
            val infiniteTransition = rememberInfiniteTransition(label = "icon")
            val iconScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (selected) 1.2f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "icon scale"
            )
            
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { 
                    Box(
                        modifier = Modifier
                            .size(if (selected) 36.dp else 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Отображаем фон если элемент выбран
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = backgroundColor,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                item.color,
                                                item.color.copy(alpha = 0.5f)
                                            )
                                        ),
                                        shape = MaterialTheme.shapes.small
                                    )
                            )
                        }
                        
                        Icon(
                            imageVector = item.icon, 
                            contentDescription = item.title,
                            tint = color,
                            modifier = Modifier
                                .size(if (selected) 24.dp else 20.dp)
                                .scale(iconScale)
                        )
                    }
                },
                label = { 
                    Text(
                        text = item.title,
                        color = color
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = color,
                    unselectedIconColor = color.copy(alpha = 0.6f),
                    selectedTextColor = color,
                    unselectedTextColor = color.copy(alpha = 0.6f),
                    indicatorColor = Color.Transparent
                ),
            )
        }
    }
} 