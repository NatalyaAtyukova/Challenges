package com.challenges.dailychallenges.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenges.dailychallenges.data.model.Achievement
import com.challenges.dailychallenges.data.model.AchievementType
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.data.model.ChallengeCategory
import com.challenges.dailychallenges.data.repository.AchievementRepository
import com.challenges.dailychallenges.data.repository.FirebaseRepository
import com.challenges.dailychallenges.ui.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

private const val TAG = "MainViewModel"

enum class SortOption {
    POINTS,
    CATEGORY,
    DATE
}

// Define achievement constants
private const val COMPLETE_50_CHALLENGES = "COMPLETE_50_CHALLENGES"
private const val COMPLETE_25_CHALLENGES = "COMPLETE_25_CHALLENGES" 
private const val COMPLETE_10_CHALLENGES = "COMPLETE_10_CHALLENGES"
private const val COMPLETE_5_CHALLENGES = "COMPLETE_5_CHALLENGES"
private const val COMPLETE_FIRST_CHALLENGE = "COMPLETE_FIRST_CHALLENGE"
private const val CONVERSATION_MASTER = "CONVERSATION_MASTER"
private const val VIDEO_MASTER = "VIDEO_MASTER"
private const val PUBLIC_SPEAKING_MASTER = "PUBLIC_SPEAKING_MASTER"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
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
    
    // Properties needed for the updated UI
    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges = _challenges.asStateFlow()
    
    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements = _achievements.asStateFlow()
    
    // Use a derived flow for completedChallengesCount to keep it in sync with challenges
    private val _completedChallengesCount = MutableStateFlow(0)
    val completedChallengesCount = _challenges.map { challenges ->
        challenges.count { it.completed }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )
    
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    init {
        Log.d(TAG, "MainViewModel initialized")
        loadChallenges()
        loadAchievements()
    }

    // Основной поток челленджей
    private val challengesFlow = combine(
        searchQuery,
        selectedSortOption,
        selectedCategory,
        showCustomChallenges,
        showSeasonalChallenges
    ) { query, sort, category, custom, seasonal ->
        Log.d(TAG, "Комбинация параметров поиска: query=$query, sort=$sort, category=$category, custom=$custom, seasonal=$seasonal")
        (when {
            query.isNotBlank() -> firebaseRepository.searchChallenges(query)
            custom -> firebaseRepository.getCustomChallenges()
            category != null -> firebaseRepository.getChallengesByCategory(category)
            else -> firebaseRepository.getAllChallenges()
        })
    }.flatMapLatest { it }
        .catch { e -> 
            Log.e(TAG, "Ошибка в потоке challenges: ${e.message}", e)
            emit(emptyList()) 
        }

    val totalPoints = firebaseRepository.getTotalPoints()
    val unlockedAchievementsCount = achievementRepository.getUnlockedAchievementsCount()
    val recentlyUnlockedAchievements = achievementRepository.getRecentlyUnlockedAchievements()

    // Специальный поток для отображения только пользовательских челленджей
    val customChallenges = firebaseRepository.getCustomChallenges()

    // Проверяем и обеспечиваем аутентификацию пользователя (анонимно, если необходимо)
    private suspend fun ensureAuthentication(): Boolean {
        return true // Simplified for now - we'll just return true as we know the user is authenticated
    }

    fun loadChallenges() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Loading challenges")
                isLoading = true
                error = null
                
                // Check authentication
                val isAuthenticated = ensureAuthentication()
                if (!isAuthenticated) {
                    Log.w(TAG, "User not authenticated, data may be unavailable")
                }
                
                // Load challenges
                firebaseRepository.getAllChallenges().collect { challengesList ->
                    _challenges.value = challengesList
                    // Remove manual update of completedChallengesCount as it's now derived from challenges
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading challenges: ${e.message}", e)
                error = e.message
                isLoading = false
                // Try fallback method
                fallbackLoadChallenges()
            }
        }
    }
    
    private fun fallbackLoadChallenges() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Using fallback path for loading")
                firebaseRepository.getAllChallenges().collect { challenges ->
                    Log.d(TAG, "Received ${challenges.size} challenges via fallback path")
                    _challenges.value = challenges
                    // Remove manual update of completedChallengesCount as it's now derived from challenges
                    isLoading = false
                    
                    // If collection is empty, create test data
                    if (challenges.isEmpty()) {
                        Log.d(TAG, "Collection is empty, creating sample data")
                        createSampleChallenges()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during fallback loading: ${e.message}", e)
                error = "Failed to load data. ${e.message}"
                isLoading = false
                // Also try to create sample challenges
                createSampleChallenges()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortOption(option: SortOption) {
        _selectedSortOption.value = option
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun toggleCustomChallenges(show: Boolean) {
        _showCustomChallenges.value = show
    }

    fun toggleSeasonalChallenges(show: Boolean) {
        _showSeasonalChallenges.value = show
    }

    // Создание пользовательского челленджа
    fun createCustomChallenge(title: String, description: String, category: String, points: Int, isFromSystem: Boolean = false) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Creating custom challenge: $title")
                val newChallenge = Challenge(
                    id = UUID.randomUUID().toString(),
                    title = if (isFromSystem) "ADDED: $title" else title,
                    description = description,
                    category = category,
                    points = points,
                    isCustom = true,
                    completed = false,
                    createdDate = System.currentTimeMillis()
                )
                firebaseRepository.addChallenge(newChallenge)
                _challenges.update { currentList ->
                    currentList + newChallenge
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error creating challenge: ${e.message}", e)
                error = "Error creating: ${e.message}"
            }
        }
    }

    fun editChallenge(challenge: Challenge, title: String, description: String, category: String, points: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Editing challenge: ${challenge.id}")
                val updatedChallenge = challenge.copy(
                    title = title,
                    description = description,
                    category = category,
                    points = points
                )
                firebaseRepository.updateChallenge(updatedChallenge)
                _challenges.update { currentList ->
                    currentList.map { if (it.id == challenge.id) updatedChallenge else it }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error editing challenge: ${e.message}", e)
                error = "Error editing: ${e.message}"
            }
        }
    }

    // Specifically update the completion status only
    fun updateChallengeCompletion(challenge: Challenge, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Updating completion status for challenge: ${challenge.id} to $isCompleted")
                
                // Don't update if status is unchanged
                if (challenge.completed == isCompleted) return@launch
                
                val updatedChallenge = challenge.copy(completed = isCompleted)
                firebaseRepository.updateChallenge(updatedChallenge)
                
                // Update local list - the completedChallengesCount will be updated automatically
                _challenges.update { currentList ->
                    currentList.map { if (it.id == challenge.id) updatedChallenge else it }
                }
                
                // If challenge is completed, check achievements
                if (isCompleted) {
                    checkAchievements()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating completion status: ${e.message}", e)
                error = "Error updating status: ${e.message}"
            }
        }
    }

    fun toggleChallengeCompletion(challenge: Challenge) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Toggling completion status for challenge: ${challenge.id}")
                val updatedChallenge = challenge.copy(completed = !challenge.completed)
                firebaseRepository.updateChallenge(updatedChallenge)
                
                // Update local list - the completedChallengesCount will be updated automatically
                _challenges.update { currentList ->
                    currentList.map { if (it.id == challenge.id) updatedChallenge else it }
                }
                
                // If challenge is completed, check achievements
                if (updatedChallenge.completed) {
                    checkAchievements()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating completion status: ${e.message}", e)
                error = "Error updating status: ${e.message}"
            }
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Deleting challenge: ${challenge.id}")
                firebaseRepository.deleteChallenge(challenge)
                
                // Update local list - the completedChallengesCount will be updated automatically
                _challenges.update { currentList ->
                    currentList.filter { it.id != challenge.id }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting challenge: ${e.message}", e)
                error = "Error deleting: ${e.message}"
            }
        }
    }

    private fun checkAchievements() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Checking achievements")
                val completedCount = _completedChallengesCount.value
                
                // Check different achievements based on number of completed challenges
                when {
                    completedCount >= 50 -> {
                        val achievement = Achievement(
                            title = "Challenge Master",
                            description = "Complete 50 challenges",
                            condition = COMPLETE_50_CHALLENGES,
                            points = 100,
                            threshold = 50
                        )
                        unlockAchievement(achievement)
                    }
                    completedCount >= 25 -> {
                        val achievement = Achievement(
                            title = "Challenge Expert",
                            description = "Complete 25 challenges",
                            condition = COMPLETE_25_CHALLENGES,
                            points = 75,
                            threshold = 25
                        )
                        unlockAchievement(achievement)
                    }
                    completedCount >= 10 -> {
                        val achievement = Achievement(
                            title = "Challenge Enthusiast",
                            description = "Complete 10 challenges",
                            condition = COMPLETE_10_CHALLENGES,
                            points = 50,
                            threshold = 10
                        )
                        unlockAchievement(achievement)
                    }
                    completedCount >= 5 -> {
                        val achievement = Achievement(
                            title = "Challenge Beginner",
                            description = "Complete 5 challenges",
                            condition = COMPLETE_5_CHALLENGES,
                            points = 30,
                            threshold = 5
                        )
                        unlockAchievement(achievement)
                    }
                    completedCount >= 1 -> {
                        val achievement = Achievement(
                            title = "First Challenge",
                            description = "Complete your first challenge",
                            condition = COMPLETE_FIRST_CHALLENGE,
                            points = 10,
                            threshold = 1
                        )
                        unlockAchievement(achievement)
                    }
                }
                
                // Also check category achievements
                checkCategoryAchievements()
            } catch (e: Exception) {
                Log.e(TAG, "Error checking achievements: ${e.message}", e)
                // Don't update UI, this is a background operation
            }
        }
    }

    private fun checkCategoryAchievements() {
        viewModelScope.launch {
            try {
                // Check completed challenges by category
                val completedChallenges = _challenges.value.filter { it.completed }
                val categoriesCompleted = completedChallenges.groupBy { it.category }
                
                for ((category, challenges) in categoriesCompleted) {
                    if (challenges.size >= 5) {
                        when (category) {
                            "CONVERSATION" -> {
                                val achievement = Achievement(
                                    title = "Conversation Master",
                                    description = "Complete 5 conversation challenges",
                                    condition = CONVERSATION_MASTER,
                                    category = "CONVERSATION",
                                    points = 50,
                                    threshold = 5
                                )
                                unlockAchievement(achievement)
                            }
                            "VIDEO" -> {
                                val achievement = Achievement(
                                    title = "Video Master",
                                    description = "Complete 5 video challenges",
                                    condition = VIDEO_MASTER,
                                    category = "VIDEO",
                                    points = 50,
                                    threshold = 5
                                )
                                unlockAchievement(achievement)
                            }
                            "PUBLIC" -> {
                                val achievement = Achievement(
                                    title = "Public Speaking Master",
                                    description = "Complete 5 public speaking challenges",
                                    condition = PUBLIC_SPEAKING_MASTER,
                                    category = "PUBLIC",
                                    points = 50,
                                    threshold = 5
                                )
                                unlockAchievement(achievement)
                            }
                            // Other categories...
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking category achievements: ${e.message}", e)
            }
        }
    }

    // Update to use Achievement object instead of AchievementType
    private suspend fun unlockAchievement(achievement: Achievement) {
        try {
            // First check if we need to create achievement
            val achievements = _achievements.value
            val existingAchievement = achievements.find { it.condition == achievement.condition }
            
            if (existingAchievement == null) {
                // Create new achievement
                achievementRepository.insertAchievement(achievement)
            } else {
                // Update existing achievement
                achievementRepository.updateAchievementProgress(existingAchievement.id, existingAchievement.threshold)
            }
            
            Log.d(TAG, "Achievement processing complete: ${achievement.title}")
        } catch (e: Exception) {
            Log.e(TAG, "Error with achievement ${achievement.title}: ${e.message}", e)
        }
    }

    fun loadCompletedChallenges() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Loading completed challenges")
                isLoading = true
                
                firebaseRepository.getCompletedChallenges().collect { completedChallenges ->
                    Log.d(TAG, "Received ${completedChallenges.size} completed challenges")
                    _challenges.value = completedChallenges
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading completed challenges: ${e.message}", e)
                error = e.message
                isLoading = false
            }
        }
    }

    // Метод для создания тестовых данных, если коллекция пуста
    private fun createSampleChallenges() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Creating sample challenges")
                val sampleChallenges = listOf(
                    Challenge(
                        id = "1",
                        title = "Record a 5-minute video",
                        description = "Record a 5-minute video about your favorite topic and share it with friends",
                        category = "VIDEO",
                        points = 10,
                        completed = false,
                        createdDate = System.currentTimeMillis()
                    ),
                    Challenge(
                        id = "2",
                        title = "Give a presentation",
                        description = "Prepare and give a 10-minute presentation on any topic",
                        category = "PUBLIC",
                        points = 20,
                        completed = false,
                        createdDate = System.currentTimeMillis()
                    ),
                    Challenge(
                        id = "3",
                        title = "Write an article",
                        description = "Write a 1000+ word article on a topic that interests you",
                        category = "WRITING",
                        points = 15,
                        completed = false,
                        createdDate = System.currentTimeMillis()
                    ),
                    Challenge(
                        id = "4",
                        title = "Record a podcast",
                        description = "Record a 15-minute podcast on a topic of your choice",
                        category = "PODCAST",
                        points = 25,
                        completed = false,
                        createdDate = System.currentTimeMillis()
                    ),
                    Challenge(
                        id = "5",
                        title = "Conduct an interview",
                        description = "Conduct an interview with an interesting person on a topic important to you",
                        category = "CONVERSATION",
                        points = 15,
                        completed = false,
                        createdDate = System.currentTimeMillis()
                    )
                )
                
                for (challenge in sampleChallenges) {
                    firebaseRepository.addChallenge(challenge)
                }
                
                _challenges.value = sampleChallenges
                isLoading = false
                
                Log.d(TAG, "Created ${sampleChallenges.size} sample challenges")
            } catch (e: Exception) {
                Log.e(TAG, "Error creating sample data: ${e.message}", e)
                error = "Error creating sample data: ${e.message}"
                isLoading = false
            }
        }
    }
    
    // New functions for the updated UI
    suspend fun syncChallenges() {
        try {
            Log.d(TAG, "Syncing challenges")
            isLoading = true
            error = null
            
            firebaseRepository.syncChallenges()
            loadChallenges()
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing challenges: ${e.message}", e)
            error = "Error syncing: ${e.message}"
            isLoading = false
        }
    }
    
    suspend fun syncAchievements() {
        try {
            Log.d(TAG, "Syncing achievements")
            isLoading = true
            error = null
            
            achievementRepository.syncAchievements()
            loadAchievements()
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing achievements: ${e.message}", e)
            error = "Error syncing: ${e.message}"
            isLoading = false
        }
    }
    
    fun loadAchievements() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Loading achievements")
                isLoading = true
                error = null
                
                achievementRepository.getAllAchievements().collect { achievementsList ->
                    _achievements.value = achievementsList
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading achievements: ${e.message}", e)
                error = e.message
                isLoading = false
            }
        }
    }
} 