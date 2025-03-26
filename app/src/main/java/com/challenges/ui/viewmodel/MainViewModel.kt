package com.challenges.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenges.data.model.Achievement
import com.challenges.data.model.AchievementType
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.repository.AchievementRepository
import com.challenges.data.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class MainUiState(
    val challenges: List<Challenge> = emptyList(),
    val selectedCategory: String? = null,
    val currentChallenge: Challenge? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentlyUnlockedAchievements: List<com.challenges.data.model.Achievement> = emptyList()
)

enum class SortOption {
    POINTS,
    CATEGORY,
    DATE
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ChallengeRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSortOption = MutableStateFlow(SortOption.DATE)
    val selectedSortOption: StateFlow<SortOption> = _selectedSortOption.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _showCustomChallenges = MutableStateFlow(false)
    val showCustomChallenges: StateFlow<Boolean> = _showCustomChallenges.asStateFlow()

    private val _showSeasonalChallenges = MutableStateFlow(false)
    val showSeasonalChallenges: StateFlow<Boolean> = _showSeasonalChallenges.asStateFlow()

    init {
        loadChallenges()
        
        // Initialize achievements
        viewModelScope.launch {
            achievementRepository.initializeDefaultAchievements()
        }
    }

    val challenges = combine(
        searchQuery,
        selectedSortOption,
        selectedCategory,
        showCustomChallenges,
        showSeasonalChallenges
    ) { query, sort, category, custom, seasonal ->
        when {
            query.isNotBlank() -> repository.searchChallenges(query)
            custom -> repository.getCustomChallenges()
            seasonal -> repository.getActiveSeasonalChallenges(LocalDateTime.now())
            category != null -> repository.getActiveChallengesByCategory(category)
            else -> when (sort) {
                SortOption.POINTS -> repository.getChallengesByPoints()
                SortOption.CATEGORY -> repository.getChallengesByCategory()
                SortOption.DATE -> repository.getChallengesByDate()
            }
        }
    }.flatMapLatest { it }

    val totalPoints = repository.getTotalPoints()
    val completedChallengesCount = repository.getCompletedChallengesCount()
    val achievementPoints = achievementRepository.getAchievementPoints()
    val unlockedAchievementsCount = achievementRepository.getUnlockedAchievementsCount()

    private fun loadChallenges() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.getChallengesByDate().collect { challenges ->
                    _uiState.value = _uiState.value.copy(
                        challenges = challenges,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun selectCategory(category: String?) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    selectedCategory = category,
                    isLoading = true
                )
                if (category != null) {
                    repository.getActiveChallengesByCategory(category).collect { challenges ->
                        _uiState.value = _uiState.value.copy(
                            challenges = challenges,
                            isLoading = false
                        )
                    }
                } else {
                    repository.getChallengesByDate().collect { challenges ->
                        _uiState.value = _uiState.value.copy(
                            challenges = challenges,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun getRandomChallenge() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val challenge = repository.getRandomChallenge()
                _uiState.value = _uiState.value.copy(
                    currentChallenge = challenge,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortOption(option: SortOption) {
        _selectedSortOption.value = option
    }

    fun toggleCustomChallenges() {
        _showCustomChallenges.value = !_showCustomChallenges.value
    }

    fun toggleSeasonalChallenges() {
        _showSeasonalChallenges.value = !_showSeasonalChallenges.value
    }

    fun toggleFavorite(challenge: Challenge) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(challenge.id, !challenge.isFavorite)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun toggleCompletion(challenge: Challenge) {
        viewModelScope.launch {
            try {
                val newCompletionStatus = !challenge.isCompleted
                repository.updateCompletionStatus(challenge.id, newCompletionStatus)
                
                // Check for achievements if challenge is now completed
                if (newCompletionStatus) {
                    // Get updated challenge with new completion status
                    val updatedChallenge = repository.getChallengeById(challenge.id)
                    if (updatedChallenge != null) {
                        achievementRepository.checkAchievements(updatedChallenge)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateNotes(challenge: Challenge, notes: String?) {
        viewModelScope.launch {
            try {
                repository.updateChallenge(challenge.copy(notes = notes))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun createCustomChallenge(title: String, description: String, category: String, points: Int) {
        viewModelScope.launch {
            val challenge = Challenge(
                title = title,
                description = description,
                category = category,
                points = points,
                isCustom = true
            )
            val id = repository.insertChallenge(challenge)
            
            // Check achievements related to creating challenges
            try {
                // Get created challenge
                val createdChallenge = repository.getChallengeById(id)
                
                // Get custom challenge count
                val createdChallengesCount = repository.getCustomChallengesCount()
                
                // Get achievements of type CREATED_CHALLENGES
                val createdChallengesAchievements = achievementRepository
                    .getAllAchievements()
                    .first()
                    .filter { it.condition == com.challenges.data.model.AchievementType.CREATED_CHALLENGES }
                
                // Update progress for each achievement
                for (achievement in createdChallengesAchievements) {
                    achievementRepository.updateAchievementProgress(achievement.id, createdChallengesCount)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun editChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.updateChallenge(challenge)
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.deleteChallenge(challenge)
        }
    }
} 