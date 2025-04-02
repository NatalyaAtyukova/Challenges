package com.challenges.dailychallenges.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val preferences: SharedPreferences = context.getSharedPreferences(
        "app_settings", Context.MODE_PRIVATE
    )
    
    private val _isDarkMode = MutableStateFlow(preferences.getBoolean(KEY_DARK_MODE, false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    private val _isNotificationsEnabled = MutableStateFlow(preferences.getBoolean(KEY_NOTIFICATIONS, true))
    val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled.asStateFlow()
    
    private val _isAchievementNotificationsEnabled = MutableStateFlow(preferences.getBoolean(KEY_ACHIEVEMENT_NOTIFICATIONS, true))
    val isAchievementNotificationsEnabled: StateFlow<Boolean> = _isAchievementNotificationsEnabled.asStateFlow()
    
    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
            _isDarkMode.value = enabled
        }
    }
    
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferences.edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
            _isNotificationsEnabled.value = enabled
        }
    }
    
    fun toggleAchievementNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferences.edit().putBoolean(KEY_ACHIEVEMENT_NOTIFICATIONS, enabled).apply()
            _isAchievementNotificationsEnabled.value = enabled
        }
    }
    
    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS = "notifications"
        private const val KEY_ACHIEVEMENT_NOTIFICATIONS = "achievement_notifications"
        
        // App version from build.gradle
        const val APP_VERSION = "1.0.0"
    }
} 