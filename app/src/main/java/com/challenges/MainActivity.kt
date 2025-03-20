package com.challenges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenges.data.model.ChallengeCategory
import com.challenges.ui.components.*
import com.challenges.ui.theme.ChallengesTheme
import com.challenges.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengesTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text("Daily Challenges") },
                            actions = {
                                IconButton(onClick = { /* TODO: Navigate to favorites */ }) {
                                    Icon(Icons.Default.Favorite, "Favorites")
                                }
                                IconButton(onClick = { /* TODO: Navigate to settings */ }) {
                                    Icon(Icons.Default.Settings, "Settings")
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { viewModel.getRandomChallenge() }
                        ) {
                            Icon(Icons.Default.Casino, "Random Challenge")
                        }
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CategoryChips(
                            categories = ChallengeCategory.values().toList(),
                            selectedCategory = uiState.selectedCategory,
                            onCategorySelected = { category ->
                                viewModel.selectCategory(category)
                            },
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        if (uiState.isLoading) {
                            LoadingSpinner()
                        } else {
                            uiState.currentChallenge?.let { challenge ->
                                ChallengeCard(
                                    challenge = challenge,
                                    onFavoriteClick = { viewModel.toggleFavorite(challenge) },
                                    onShareClick = {
                                        // TODO: Implement sharing
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Sharing coming soon!")
                                        }
                                    }
                                )
                            }
                        }

                        uiState.error?.let { error ->
                            LaunchedEffect(error) {
                                snackbarHostState.showSnackbar(error)
                                viewModel.clearError()
                            }
                        }
                    }
                }
            }
        }
    }
}