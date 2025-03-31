package com.challenges.dailychallenges.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.challenges.dailychallenges.ui.components.AppTopBar
import com.challenges.dailychallenges.ui.components.BottomNavigation
import com.challenges.dailychallenges.ui.components.ChallengeItem
import com.challenges.dailychallenges.ui.components.EditChallengeDialog
import com.challenges.dailychallenges.ui.viewmodel.MainViewModel

@Composable
fun MyChallengesScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedChallengeId by remember { mutableStateOf<String?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    
    val selectedChallenge = selectedChallengeId?.let { id ->
        uiState.challenges.find { it.id == id }
    }
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Мои челленджи",
                onSyncClicked = {
                    viewModel.loadCompletedChallenges()
                }
            )
        },
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.challenges.isEmpty()) {
                Text(
                    text = "У вас пока нет челленджей",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.challenges) { challenge ->
                        ChallengeItem(
                            challenge = challenge,
                            onClick = {
                                selectedChallengeId = challenge.id
                                showEditDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Диалог редактирования челленджа
    if (showEditDialog && selectedChallenge != null) {
        EditChallengeDialog(
            challenge = selectedChallenge,
            onDismiss = { 
                showEditDialog = false
                selectedChallengeId = null
            },
            onSaveChallenge = { updatedChallenge ->
                viewModel.editChallenge(updatedChallenge)
                showEditDialog = false
                selectedChallengeId = null
            },
            onDeleteChallenge = { challenge ->
                viewModel.deleteChallenge(challenge.id)
                showEditDialog = false
                selectedChallengeId = null
            },
            onToggleCompleted = { challenge ->
                viewModel.toggleChallengeCompleted(challenge.id)
                showEditDialog = false
                selectedChallengeId = null
            }
        )
    }
} 