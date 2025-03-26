package com.challenges.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenges.R
import com.challenges.data.model.Challenge
import com.challenges.ui.components.*
import com.challenges.ui.viewmodel.AuthViewModel
import com.challenges.ui.viewmodel.MainViewModel
import com.challenges.ui.viewmodel.SortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onShareClick: (Challenge) -> Unit,
    onNavigateToAchievements: () -> Unit = {},
    onNavigateToMyChallenges: () -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedChallenge by remember { mutableStateOf<Challenge?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    val challenges by viewModel.challenges.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSortOption by viewModel.selectedSortOption.collectAsState()
    val showCustomChallenges by viewModel.showCustomChallenges.collectAsState()
    val showSeasonalChallenges by viewModel.showSeasonalChallenges.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState(initial = 0)
    val completedChallengesCount by viewModel.completedChallengesCount.collectAsState(initial = 0)
    val achievementPoints by viewModel.achievementPoints.collectAsState(initial = 0)
    val unlockedAchievementsCount by viewModel.unlockedAchievementsCount.collectAsState(initial = 0)
    val authState by authViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(stringResource(R.string.app_name))
                        if (authState.userName.isNotEmpty()) {
                            Text(
                                text = "Привет, ${authState.userName}!",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToMyChallenges() }) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Мои челленджи"
                        )
                    }
                    IconButton(onClick = { viewModel.toggleSeasonalChallenges() }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Сезонные челленджи"
                        )
                    }
                    IconButton(onClick = { onNavigateToAchievements() }) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = "Достижения"
                        )
                    }
                    IconButton(onClick = { onSignOut() }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Выйти"
                        )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::setSearchQuery
            )
            SortOptions(
                selectedOption = selectedSortOption,
                onOptionSelected = viewModel::setSortOption
            )
            StatsCard(
                totalPoints = totalPoints,
                completedChallenges = completedChallengesCount,
                achievementPoints = achievementPoints,
                unlockedAchievements = unlockedAchievementsCount,
                onAchievementsClick = onNavigateToAchievements
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(challenges) { challenge ->
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
                }
            }
        )
    }
} 