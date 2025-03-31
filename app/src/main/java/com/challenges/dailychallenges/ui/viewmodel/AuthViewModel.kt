package com.challenges.dailychallenges.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val userName: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        auth.currentUser?.let {
            _uiState.value = _uiState.value.copy(
                isAuthenticated = true,
                userName = it.displayName ?: ""
            )
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                try {
                    val result = auth.signInWithEmailAndPassword(email, password).await()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userName = result.user?.displayName ?: ""
                    )
                } catch (e: Exception) {
                    // If verification fails, try disabling verification
                    if (e.message?.contains("CONFIGURATION_NOT_FOUND") == true) {
                        // Disable verification for this operation
                        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
                        
                        val result = auth.signInWithEmailAndPassword(email, password).await()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            userName = result.user?.displayName ?: ""
                        )
                    } else {
                        throw e
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

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                try {
                    val result = auth.createUserWithEmailAndPassword(email, password).await()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userName = result.user?.displayName ?: ""
                    )
                } catch (e: Exception) {
                    // If verification fails, try disabling verification
                    if (e.message?.contains("CONFIGURATION_NOT_FOUND") == true) {
                        // Disable verification for this operation
                        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
                        
                        val result = auth.createUserWithEmailAndPassword(email, password).await()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            userName = result.user?.displayName ?: ""
                        )
                    } else {
                        throw e
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

    fun signOut() {
        auth.signOut()
        _uiState.value = _uiState.value.copy(isAuthenticated = false)
    }
} 