package com.challenges.dailychallenges.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.ui.components.AddChallengeDialog
import com.challenges.dailychallenges.ui.components.AppTopBar
import com.challenges.dailychallenges.ui.components.BottomNavigation
import com.challenges.dailychallenges.ui.components.EditChallengeDialog
import com.challenges.dailychallenges.ui.components.StatsCardHorizontal
import com.challenges.dailychallenges.ui.navigation.Screen
import com.challenges.dailychallenges.ui.viewmodel.AuthViewModel
import com.challenges.dailychallenges.ui.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.challenges.dailychallenges.ui.animations.PulseAnimation
import com.challenges.dailychallenges.ui.theme.NeonGreen

private const val TAG = "MainScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    onNavigateToAuth: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedChallenge by remember { mutableStateOf<Challenge?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var filterCategory by remember { mutableStateOf("") }
    
    LaunchedEffect(key1 = true) {
        viewModel.loadChallenges()
    }
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Ежедневные челленджи",
                onSyncClicked = {
                    viewModel.loadChallenges()
                },
                onSignOutClicked = {
                    FirebaseAuth.getInstance().signOut()
                    onNavigateToAuth()
                }
            )
        },
        bottomBar = {
            BottomNavigation(navController)
        },
        floatingActionButton = {
            PulseAnimation(pulseMagnitude = 0.03f) { animModifier ->
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    modifier = animModifier,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить челлендж")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Статистика
            StatsCardHorizontal(
                totalChallenges = uiState.challenges.size,
                completedChallenges = uiState.challenges.count { it.completed },
                streakDays = 5 // TODO: Реализовать подсчет дней
            )
            
            // Поиск
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { isSearchActive = false },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                placeholder = { Text("Поиск челленджей") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
                trailingIcon = {
                    IconButton(onClick = { isSearchActive = !isSearchActive }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Фильтры")
                    }
                }
            ) {
                // Содержимое поиска
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.challenges.filter { 
                        it.title.contains(searchQuery, ignoreCase = true) || 
                        it.description.contains(searchQuery, ignoreCase = true)
                    }) { challenge ->
                        ChallengeItem(
                            challenge = challenge,
                            onClick = {
                                selectedChallenge = challenge
                                showEditDialog = true
                                isSearchActive = false
                            },
                            index = uiState.challenges.indexOf(challenge)
                        )
                    }
                }
            }
            
            // Фильтры категорий
            val categories = uiState.challenges
                .map { it.category }
                .distinct()
                .filter { it.isNotBlank() }
            
            if (categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = filterCategory.isEmpty(),
                        onClick = { filterCategory = "" },
                        label = { Text("Все") }
                    )
                    
                    categories.forEach { category ->
                        FilterChip(
                            selected = filterCategory == category,
                            onClick = { filterCategory = if (filterCategory == category) "" else category },
                            label = { Text(category) }
                        )
                    }
                }
            }
            
            // Список челленджей
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.challenges.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет активных челленджей.\nНажмите + чтобы добавить!",
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val filteredChallenges = uiState.challenges.filter { challenge ->
                        val matchesCategory = filterCategory.isEmpty() || challenge.category == filterCategory
                        val matchesSearch = searchQuery.isEmpty() || 
                                challenge.title.contains(searchQuery, ignoreCase = true) ||
                                challenge.description.contains(searchQuery, ignoreCase = true)
                        matchesCategory && matchesSearch
                    }
                    
                    if (filteredChallenges.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Нет челленджей, соответствующих фильтрам")
                            }
                        }
                    } else {
                        items(filteredChallenges) { challenge ->
                            ChallengeItem(
                                challenge = challenge,
                                onClick = {
                                    selectedChallenge = challenge
                                    showEditDialog = true
                                },
                                index = filteredChallenges.indexOf(challenge)
                            )
                        }
                    }
                }
            }
            
            // Отображение ошибки
            uiState.error?.let { error ->
                Text(
                    text = "Ошибка: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
    
    // Диалог добавления челленджа
    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onAddChallenge = { title, description, category, difficulty ->
                viewModel.addChallenge(title, description, category, difficulty)
                showAddDialog = false
            }
        )
    }
    
    // Диалог редактирования челленджа
    if (showEditDialog && selectedChallenge != null) {
        EditChallengeDialog(
            challenge = selectedChallenge!!,
            onDismiss = { 
                showEditDialog = false
                selectedChallenge = null
            },
            onSaveChallenge = { updatedChallenge ->
                viewModel.editChallenge(updatedChallenge)
                showEditDialog = false
                selectedChallenge = null
            },
            onDeleteChallenge = { challenge ->
                viewModel.deleteChallenge(challenge.id)
                showEditDialog = false
                selectedChallenge = null
            },
            onToggleCompleted = { challenge ->
                viewModel.toggleChallengeCompleted(challenge.id)
                showEditDialog = false
                selectedChallenge = null
            }
        )
    }
}

@Composable
fun ChallengeItem(
    challenge: Challenge,
    onClick: () -> Unit,
    index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                if (challenge.completed) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Выполнено",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Категория: ${challenge.category.ifEmpty { "Общая" }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Сложность: ${challenge.difficulty}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Дата создания
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val createdDate = challenge.createdDate?.let { formatter.format(Date(it)) } ?: "Нет даты"
            
            Text(
                text = "Создан: $createdDate",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Прогресс
            LinearProgressIndicator(
                progress = if (challenge.completed) 1f else 0.5f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
} 