package com.challenges.dailychallenges

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.ui.navigation.AppNavigation
import com.challenges.dailychallenges.ui.theme.ChallengesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController
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