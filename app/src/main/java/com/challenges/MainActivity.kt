package com.challenges

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.challenges.data.model.Challenge
import com.challenges.ui.screens.MainScreen
import com.challenges.ui.theme.ChallengesTheme
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
                    MainScreen(
                        onShareClick = { challenge ->
                            shareChallenge(challenge)
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
}