package com.challenges.dailychallenges.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenges.dailychallenges.data.model.Achievement
import com.challenges.dailychallenges.data.repository.AchievementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AchievementUiState(
    val achievements: List<Achievement> = emptyList(),
    val unlockedAchievements: List<Achievement> = emptyList(),
    val lockedAchievements: List<Achievement> = emptyList(),
    val achievementPoints: Int = 0,
    val unlockedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String = ""
)

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AchievementUiState(isLoading = true))
    val uiState: StateFlow<AchievementUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            try {
                // Загрузить достижения, создание дефолтных скрыто в репозитории
                loadAchievements()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "Failed to initialize achievements",
                    isLoading = false
                ) }
            }
        }
    }
    
    private fun loadAchievements() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                // Collect data
                val achievements = achievementRepository.getAllAchievements().first()
                val unlockedAchievements = achievements.filter { it.isUnlocked }
                val lockedAchievements = achievements.filter { !it.isUnlocked }
                val totalPoints = achievementRepository.getAchievementPoints().first() ?: 0
                
                _uiState.update { it.copy(
                    achievements = achievements,
                    unlockedAchievements = unlockedAchievements,
                    lockedAchievements = lockedAchievements,
                    achievementPoints = totalPoints,
                    unlockedCount = unlockedAchievements.size,
                    totalCount = achievements.size,
                    isLoading = false,
                    error = ""
                ) }
                
                // Continue listening for updates
                combine(
                    achievementRepository.getAllAchievements(),
                    achievementRepository.getAchievementPoints()
                ) { achList, points ->
                    val unlocked = achList.filter { it.isUnlocked }
                    val locked = achList.filter { !it.isUnlocked }
                    
                    _uiState.update { it.copy(
                        achievements = achList,
                        unlockedAchievements = unlocked,
                        lockedAchievements = locked,
                        achievementPoints = points ?: 0,
                        unlockedCount = unlocked.size,
                        totalCount = achList.size
                    ) }
                }.collect()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "Failed to load achievements",
                    isLoading = false
                ) }
            }
        }
    }
    
    // Refresh achievements data
    fun refreshAchievements() {
        loadAchievements()
    }
} 