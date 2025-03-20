package com.challenges.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.data.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val challenges: List<Challenge> = emptyList(),
    val selectedCategory: ChallengeCategory? = null,
    val currentChallenge: Challenge? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadChallenges()
    }

    private fun loadChallenges() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.getAllChallenges().collect { challenges ->
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

    fun selectCategory(category: ChallengeCategory?) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    selectedCategory = category,
                    isLoading = true
                )
                if (category != null) {
                    repository.getChallengesByCategory(category).collect { challenges ->
                        _uiState.value = _uiState.value.copy(
                            challenges = challenges,
                            isLoading = false
                        )
                    }
                } else {
                    loadChallenges()
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

    fun toggleFavorite(challenge: Challenge) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(challenge)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
} 