package com.challenges.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.challenges.data.model.Challenge
import com.challenges.ui.screens.AchievementScreen
import com.challenges.ui.screens.AuthScreen
import com.challenges.ui.screens.MainScreen
import com.challenges.ui.screens.MyChallengesScreen
import com.challenges.ui.viewmodel.AuthViewModel

enum class AppScreen(val route: String) {
    AUTH("auth"),
    MAIN("main"),
    ACHIEVEMENTS("achievements"),
    MY_CHALLENGES("my_challenges")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
    startDestination: String = AppScreen.AUTH.route,
    onShareClick: (Challenge) -> Unit = {}
) {
    val authState by authViewModel.uiState.collectAsState()
    
    // Auto-login effect
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate(AppScreen.MAIN.route) {
                popUpTo(AppScreen.AUTH.route) { inclusive = true }
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = AppScreen.AUTH.route
    ) {
        composable(AppScreen.AUTH.route) {
            AuthScreen(
                onNavigateToMain = {
                    navController.navigate(AppScreen.MAIN.route) {
                        popUpTo(AppScreen.AUTH.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(AppScreen.MAIN.route) {
            MainScreen(
                onShareClick = onShareClick,
                onNavigateToAchievements = {
                    navController.navigate(AppScreen.ACHIEVEMENTS.route)
                },
                onNavigateToMyChallenges = {
                    navController.navigate(AppScreen.MY_CHALLENGES.route)
                },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(AppScreen.AUTH.route) {
                        popUpTo(AppScreen.MAIN.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(AppScreen.ACHIEVEMENTS.route) {
            AchievementScreen()
        }
        
        composable(AppScreen.MY_CHALLENGES.route) {
            MyChallengesScreen(
                onShareClick = onShareClick,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 