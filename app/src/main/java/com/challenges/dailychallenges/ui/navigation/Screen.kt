package com.challenges.dailychallenges.ui.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Main : Screen("main")
    object Achievements : Screen("achievements")
    object MyChallenges : Screen("myChallenges")
    object Community : Screen("community")
    object Profile : Screen("profile")
} 