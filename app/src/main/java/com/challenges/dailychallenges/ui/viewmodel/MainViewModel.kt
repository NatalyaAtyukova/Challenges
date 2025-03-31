package com.challenges.dailychallenges.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenges.dailychallenges.data.model.Achievement
import com.challenges.dailychallenges.data.model.AchievementType
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.data.model.ChallengeCategory
import com.challenges.dailychallenges.data.repository.AchievementRepository
import com.challenges.dailychallenges.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

private const val TAG = "MainViewModel"

data class MainUiState(
    val challenges: List<Challenge> = emptyList(),
    val selectedCategory: String? = null,
    val currentChallenge: Challenge? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentlyUnlockedAchievements: List<Achievement> = emptyList()
)

enum class SortOption {
    POINTS,
    CATEGORY,
    DATE
}

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

    init {
        Log.d(TAG, "Инициализация MainViewModel")
        loadChallenges()
    }

    // Базовый поток челленджей для экрана
    val challenges = combine(
        searchQuery,
        selectedSortOption,
        selectedCategory,
        showCustomChallenges,
        showSeasonalChallenges
    ) { query, sort, category, custom, seasonal ->
        Log.d(TAG, "Комбинация параметров поиска: query=$query, sort=$sort, category=$category, custom=$custom, seasonal=$seasonal")
        when {
            query.isNotBlank() -> firebaseRepository.searchChallenges(query)
            custom -> firebaseRepository.getCustomChallenges()
            category != null -> firebaseRepository.getChallengesByCategory(category)
            else -> getAllChallenges(sort)
        }
    }.flatMapLatest { it }
        .catch { e -> 
            Log.e(TAG, "Ошибка в потоке challenges: ${e.message}", e)
            emit(emptyList()) 
        }

    // Получение всех челленджей с учетом сортировки
    private fun getAllChallenges(sort: SortOption): Flow<List<Challenge>> {
        Log.d(TAG, "Запрос челленджей с сортировкой: $sort")
        return flow {
            try {
                val challengesFlow = firebaseRepository.getAllChallenges()
                val challenges = challengesFlow.firstOrNull() ?: emptyList()
                
                // Сортировка челленджей
                val sortedChallenges = when (sort) {
                    SortOption.POINTS -> challenges.sortedByDescending { it.points }
                    SortOption.CATEGORY -> challenges.sortedBy { it.category }
                    SortOption.DATE -> challenges.sortedByDescending { it.createdDate }
                }
                
                emit(sortedChallenges)
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка получения челленджей: ${e.message}", e)
                emit(emptyList())
            }
        }
    }

    val totalPoints = firebaseRepository.getTotalPoints()
    val completedChallengesCount = firebaseRepository.getCompletedChallengesCount()
    val achievementPoints = achievementRepository.getAchievementPoints()
    val unlockedAchievementsCount = achievementRepository.getUnlockedAchievementsCount()
    val recentlyUnlockedAchievements = achievementRepository.getRecentlyUnlockedAchievements()

    // Специальный поток для отображения только пользовательских челленджей
    val customChallenges = firebaseRepository.getCustomChallenges()

    fun loadChallenges() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Загрузка челленджей")
                _uiState.update { it.copy(isLoading = true) }
                
                // Загрузка челленджей
                firebaseRepository.getAllChallenges().collect { challenges ->
                    Log.d(TAG, "Получено ${challenges.size} челленджей")
                    _uiState.update { 
                        it.copy(
                            challenges = challenges,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке челленджей: ${e.message}", e)
                _uiState.update { it.copy(error = e.message, isLoading = false) }
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
    fun createCustomChallenge(title: String, description: String, category: String, points: Int) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Создание пользовательского челленджа: $title (категория: $category)")
                val challenge = Challenge(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    category = category,
                    points = points,
                    isCustom = true
                )
                
                firebaseRepository.insertChallenge(challenge)
                Log.d(TAG, "Создан пользовательский челлендж с ID: ${challenge.id}")
                
                // Проверка достижений по созданию челленджей
                achievementRepository.checkCustomChallengesCreation()
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при создании пользовательского челленджа: ${e.message}", e)
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    // Удаление челленджа
    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Удаление челленджа с ID: ${challenge.id}")
                firebaseRepository.deleteChallenge(challenge)
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при удалении челленджа: ${e.message}", e)
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    // Редактирование челленджа
    fun editChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Редактирование челленджа: ${challenge.id}")
                firebaseRepository.updateChallenge(challenge)
                loadChallenges()
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при редактировании челленджа: ${e.message}", e)
                _uiState.update { it.copy(error = "Ошибка редактирования: ${e.message}") }
            }
        }
    }

    fun addChallenge(title: String, description: String, category: String, difficulty: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Добавление нового челленджа: $title")
                
                val finalCategory = if (category.isBlank()) ChallengeCategory.OTHER.value else category
                
                val newChallenge = Challenge(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    category = finalCategory,
                    difficulty = difficulty,
                    completed = false,
                    createdDate = System.currentTimeMillis(),
                    userId = firebaseRepository.getCurrentUserId()
                )
                
                firebaseRepository.insertChallenge(newChallenge)
                loadChallenges()
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при добавлении челленджа: ${e.message}", e)
                _uiState.update { it.copy(error = "Ошибка добавления: ${e.message}") }
            }
        }
    }
    
    fun deleteChallenge(challengeId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Удаление челленджа: $challengeId")
                firebaseRepository.deleteChallengeById(challengeId)
                loadChallenges()
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при удалении челленджа: ${e.message}", e)
                _uiState.update { it.copy(error = "Ошибка удаления: ${e.message}") }
            }
        }
    }
    
    fun toggleChallengeCompleted(challengeId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Переключение статуса выполнения для челленджа: $challengeId")
                val challenge = firebaseRepository.getChallengeById(challengeId)
                if (challenge != null) {
                    val updatedChallenge = challenge.copy(
                        completed = !challenge.completed,
                        completedDate = if (!challenge.completed) System.currentTimeMillis() else null
                    )
                    firebaseRepository.updateChallenge(updatedChallenge)
                    
                    // Если челлендж выполнен, проверяем достижения
                    if (!challenge.completed) {
                        // Запоминаем текущие разблокированные достижения
                        val unlockedAchievementsFlow = achievementRepository.getUnlockedAchievements()
                        val currentUnlocked = unlockedAchievementsFlow.firstOrNull() ?: emptyList()
                    
                        // Проверить достижения по категории
                        achievementRepository.checkCategoryCompletion(challenge.category)
                        
                        // Проверить достижения по сложности
                        if (challenge.difficulty != null) {
                            achievementRepository.checkDifficultyCompletion(challenge.difficulty)
                        }
                        
                        // Получить новые разблокированные достижения
                        val newUnlockedFlow = achievementRepository.getUnlockedAchievements()
                        val newUnlocked = newUnlockedFlow.firstOrNull() ?: emptyList()
                        
                        // Найти новые разблокированные достижения
                        val newlyUnlocked = newUnlocked.filter { new ->
                            currentUnlocked.none { current -> current.id == new.id }
                        }
                        
                        // Обновить UI-состояние с новыми разблокированными достижениями
                        if (newlyUnlocked.isNotEmpty()) {
                            _uiState.update { it.copy(recentlyUnlockedAchievements = newlyUnlocked) }
                        }
                    }
                    
                    loadChallenges()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при переключении статуса выполнения: ${e.message}", e)
                _uiState.update { it.copy(error = "Ошибка обновления статуса: ${e.message}") }
            }
        }
    }
} 