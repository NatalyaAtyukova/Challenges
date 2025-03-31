package com.challenges.dailychallenges.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenges.dailychallenges.data.model.CommunityChallenge
import com.challenges.dailychallenges.data.model.Comment
import com.challenges.dailychallenges.data.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CommunityUiState(
    val challenges: List<CommunityChallenge> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val selectedChallenge: CommunityChallenge? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortBy: String = "created_at",
    val selectedCategory: String? = null
)

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val repository: CommunityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    init {
        loadChallenges()
    }

    private fun loadChallenges() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.getCommunityChallenges(
                    category = _uiState.value.selectedCategory,
                    sortBy = _uiState.value.sortBy
                ).collect { challenges ->
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

    fun selectChallenge(challenge: CommunityChallenge) {
        _uiState.value = _uiState.value.copy(selectedChallenge = challenge)
        loadComments(challenge.id)
    }

    private fun loadComments(challengeId: String) {
        viewModelScope.launch {
            try {
                repository.getComments(challengeId).collect { comments ->
                    _uiState.value = _uiState.value.copy(comments = comments)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun addComment(text: String) {
        val challenge = _uiState.value.selectedChallenge ?: return
        viewModelScope.launch {
            try {
                val comment = Comment(
                    challengeId = challenge.id,
                    userId = "current_user_id", // TODO: Get from auth
                    userName = "Current User", // TODO: Get from auth
                    text = text
                )
                repository.addComment(comment)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun likeChallenge(challengeId: String) {
        viewModelScope.launch {
            try {
                repository.likeChallenge(challengeId, "current_user_id") // TODO: Get from auth
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun unlikeChallenge(challengeId: String) {
        viewModelScope.launch {
            try {
                repository.unlikeChallenge(challengeId, "current_user_id") // TODO: Get from auth
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun reportChallenge(reason: String) {
        val challenge = _uiState.value.selectedChallenge ?: return
        viewModelScope.launch {
            try {
                repository.reportChallenge(challenge.id, "current_user_id", reason) // TODO: Get from auth
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun reportComment(commentId: String, reason: String) {
        viewModelScope.launch {
            try {
                repository.reportComment(commentId, "current_user_id", reason) // TODO: Get from auth
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun setSortBy(sortBy: String) {
        _uiState.value = _uiState.value.copy(sortBy = sortBy)
        loadChallenges()
    }

    fun setCategory(category: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        loadChallenges()
    }
} 