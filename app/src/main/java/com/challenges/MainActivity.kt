package com.challenges

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenges.R
import com.challenges.data.model.Challenge
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
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val sharingComingSoon = stringResource(R.string.sharing_coming_soon)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(stringResource(R.string.app_name)) }
                            )
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            CategoryChips(
                                categories = ChallengeCategory.values().toList(),
                                selectedCategory = uiState.selectedCategory,
                                onCategorySelected = { viewModel.selectCategory(it) }
                            )

                            if (uiState.isLoading) {
                                LoadingSpinner()
                            } else {
                                LazyColumn {
                                    items(uiState.challenges) { challenge ->
                                        ChallengeCard(
                                            challenge = challenge,
                                            onFavoriteClick = { viewModel.toggleFavorite(challenge) },
                                            onShareClick = {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(sharingComingSoon)
                                                }
                                            }
                                        )
                                    }
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
}