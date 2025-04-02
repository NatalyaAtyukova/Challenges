package com.challenges.dailychallenges

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.ui.navigation.AppNavigation
import com.challenges.dailychallenges.ui.theme.ChallengesTheme
import com.challenges.dailychallenges.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState(initial = isSystemInDarkTheme())
            
            ChallengesTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        onNavigateToAuth = {
                            // В этом примере у нас нет экрана авторизации, 
                            // но можно было бы добавить навигацию в будущем
                            // Пока просто логирование выхода
                            // Здесь мог бы быть выход из приложения или переход на экран авторизации
                        }
                    )
                }
            }
        }
    }

    private fun shareChallenge(challenge: Challenge) {
        val shareText = buildString {
            appendLine(challenge.title)
            appendLine(challenge.description)
            appendLine("${getString(R.string.points)}: ${challenge.points}")
            appendLine(getString(R.string.app_name))
        }
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareTitle = getString(R.string.share_via)
        startActivity(Intent.createChooser(sendIntent, shareTitle))
    }

    @Preview(showBackground = true)
    fun preview() {
        // No preview implementation needed
    }
}