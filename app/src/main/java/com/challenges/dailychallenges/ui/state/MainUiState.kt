package com.challenges.dailychallenges.ui.state

import com.challenges.dailychallenges.data.model.Achievement
import com.challenges.dailychallenges.data.model.Challenge

data class MainUiState(
    val challenges: List<Challenge> = emptyList(),
    val selectedCategory: String? = null,
    val currentChallenge: Challenge? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentlyUnlockedAchievements: List<Achievement> = emptyList()
) 