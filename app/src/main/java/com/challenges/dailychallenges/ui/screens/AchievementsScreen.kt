package com.challenges.dailychallenges.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.challenges.dailychallenges.R
import com.challenges.dailychallenges.ui.components.AppTopBar
import com.challenges.dailychallenges.ui.components.BottomNavigation
import com.challenges.dailychallenges.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun AchievementsScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    onNavigateToAuth: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val achievements by viewModel.achievements.collectAsState()
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.achievements),
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.syncAchievements()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.sync_achievements)
                        )
                    }
                    IconButton(onClick = onNavigateToAuth) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                navController = navController
            )
        }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (viewModel.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewModel.error ?: stringResource(R.string.unknown_error),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        } else if (achievements.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.no_achievements))
                        Text(text = stringResource(R.string.complete_challenges_for_achievements))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Here will be the list of achievements
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.your_achievements))
                        Text(text = "${stringResource(R.string.count)}: ${achievements.size}")
                    }
                }
            }
        }
    }
} 