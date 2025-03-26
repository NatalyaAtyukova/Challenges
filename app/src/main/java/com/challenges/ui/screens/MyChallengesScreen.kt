package com.challenges.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenges.data.model.Challenge
import com.challenges.ui.components.ChallengeCard
import com.challenges.ui.components.ChallengeDialog
import com.challenges.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyChallengesScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onShareClick: (Challenge) -> Unit,
    onNavigateBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedChallenge by remember { mutableStateOf<Challenge?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    // Получаем только пользовательские челленджи
    val customChallenges by viewModel.customChallenges.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои Челленджи") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить челлендж")
            }
        }
    ) { padding ->
        if (customChallenges.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "У вас пока нет собственных челленджей. Создайте первый!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(customChallenges) { challenge ->
                    ChallengeCard(
                        challenge = challenge,
                        onFavoriteClick = { viewModel.toggleFavorite(challenge) },
                        onShareClick = { onShareClick(challenge) },
                        onEditClick = {
                            selectedChallenge = challenge
                            showEditDialog = true
                        },
                        onDeleteClick = { viewModel.deleteChallenge(challenge) },
                        onCompletionClick = { viewModel.toggleCompletion(challenge) },
                        onNotesChange = { notes -> viewModel.updateNotes(challenge, notes) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        ChallengeDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, description, category, points ->
                viewModel.createCustomChallenge(title, description, category, points)
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedChallenge != null) {
        ChallengeDialog(
            challenge = selectedChallenge,
            onDismiss = {
                showEditDialog = false
                selectedChallenge = null
            },
            onSave = { title, description, category, points ->
                selectedChallenge?.let { challenge ->
                    viewModel.editChallenge(
                        challenge.copy(
                            title = title,
                            description = description,
                            category = category,
                            points = points
                        )
                    )
                    showEditDialog = false
                }
            }
        )
    }
} 