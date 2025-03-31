package com.challenges.dailychallenges.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.challenges.dailychallenges.ui.screens.*
import com.challenges.dailychallenges.ui.viewmodel.AuthViewModel
import com.challenges.dailychallenges.ui.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Auth.route
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                viewModel = authViewModel,
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Main.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(
                viewModel = mainViewModel,
                authViewModel = authViewModel,
                navController = navController,
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Achievements.route) {
            AchievementScreen(
                navController = navController
            )
        }
        
        composable(Screen.MyChallenges.route) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MyChallengesScreen(
                viewModel = mainViewModel,
                navController = navController
            )
        }
        
        composable(Screen.Community.route) {
            CommunityScreen(
                navController = navController
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                navController = navController,
                onSignOut = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
} 