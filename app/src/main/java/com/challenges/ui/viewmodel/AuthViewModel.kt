package com.challenges.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenges.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val userName: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Check if user is already authenticated
        checkAuth()
    }
    
    fun checkAuth() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // Get authentication state from preferences
                val isLoggedIn = userPreferences.isLoggedIn.first()
                
                if (isLoggedIn) {
                    val userName = userPreferences.userName.first()
                    _uiState.update { it.copy(
                        isAuthenticated = true,
                        userName = userName,
                        isLoading = false
                    ) }
                } else {
                    _uiState.update { it.copy(
                        isAuthenticated = false,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to check authentication status"
                ) }
            }
        }
    }
    
    fun signIn(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        viewModelScope.launch {
            try {
                delay(1000)  // Simulate network call
                
                // Demo authentication - accept any non-empty values
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    // Save auth state to preferences
                    val userName = email.substringBefore('@')
                    userPreferences.saveUserAuth(email, userName)
                    
                    _uiState.update { it.copy(
                        isAuthenticated = true,
                        userName = userName,
                        isLoading = false
                    ) }
                } else {
                    _uiState.update { it.copy(
                        error = "Email and password must not be empty",
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "Authentication failed",
                    isLoading = false
                ) }
            }
        }
    }
    
    fun signUp(email: String, password: String, displayName: String) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        viewModelScope.launch {
            try {
                delay(1500)  // Simulate network call
                
                // Demo registration - accept any non-empty values
                if (email.isNotEmpty() && password.isNotEmpty() && displayName.isNotEmpty()) {
                    // Save auth state to preferences
                    userPreferences.saveUserAuth(email, displayName)
                    
                    _uiState.update { it.copy(
                        isAuthenticated = true,
                        userName = displayName,
                        isLoading = false
                    ) }
                } else {
                    _uiState.update { it.copy(
                        error = "All fields must be filled",
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "Registration failed",
                    isLoading = false
                ) }
            }
        }
    }
    
    fun signOut() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // Clear auth state from preferences
                userPreferences.clearUserAuth()
                
                _uiState.update { it.copy(
                    isAuthenticated = false,
                    userName = "",
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "Sign out failed",
                    isLoading = false
                ) }
            }
        }
    }
} 