package com.challenges.dailychallenges.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.challenges.dailychallenges.ui.screens.*
import com.challenges.dailychallenges.ui.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    onNavigateToAuth: () -> Unit
) {
    // Создаем общую ViewModel для всех экранов
    val mainViewModel: MainViewModel = hiltViewModel()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                viewModel = mainViewModel,
                navController = navController,
                onNavigateToAuth = onNavigateToAuth
            )
        }
        
        composable(Screen.MyChallenges.route) {
            MyChallengesScreen(
                viewModel = mainViewModel,
                navController = navController,
                onNavigateToAuth = onNavigateToAuth
            )
        }
        
        composable(Screen.Achievements.route) {
            AchievementsScreen(
                viewModel = mainViewModel,
                navController = navController,
                onNavigateToAuth = onNavigateToAuth
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToAuth = onNavigateToAuth,
                navController = navController
            )
        }
    }
} 