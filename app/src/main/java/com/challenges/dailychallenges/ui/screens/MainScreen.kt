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
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.challenges.dailychallenges.R
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.ui.components.AddChallengeDialog
import com.challenges.dailychallenges.ui.components.AppTopBar
import com.challenges.dailychallenges.ui.components.BottomNavigation
import com.challenges.dailychallenges.ui.components.EditChallengeDialog
import com.challenges.dailychallenges.ui.components.StatsCardHorizontal
import com.challenges.dailychallenges.ui.navigation.Screen
import com.challenges.dailychallenges.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.challenges.dailychallenges.ui.animations.PulseAnimation
import com.challenges.dailychallenges.ui.theme.NeonGreen
import com.challenges.dailychallenges.ui.theme.NeonBlue
import androidx.compose.foundation.layout.PaddingValues
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

private const val TAG = "MainScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    onNavigateToAuth: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val challenges by viewModel.challenges.collectAsState()
    val completedChallengesCount by viewModel.completedChallengesCount.collectAsState()
    val unlockedAchievements by viewModel.unlockedAchievementsCount.collectAsState(initial = 0)
    val totalPoints by viewModel.totalPoints.collectAsState(initial = 0)
    
    var searchQuery by remember { mutableStateOf("") }
    val (selectedCategory, setSelectedCategory) = remember { mutableStateOf<String?>(null) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var currentEditChallenge by remember { mutableStateOf<Challenge?>(null) }
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.syncChallenges()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.sync_challenges),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onNavigateToAuth) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                navController = navController
            )
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
                totalChallenges = challenges.size,
                completedChallenges = completedChallengesCount,
                streakDays = unlockedAchievements
            )
            
            // Поиск
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                query = searchQuery,
                onQueryChange = { 
                    searchQuery = it
                    viewModel.updateSearchQuery(it)
                },
                onSearch = { 
                    // Handle search
                },
                active = false,
                onActiveChange = { 
                    // Handle search active change
                },
                placeholder = { Text("Поиск челленджей") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") }
            ) {
                // Содержимое поиска
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(challenges.filter { 
                        it.title.contains(searchQuery, ignoreCase = true) || 
                        it.description.contains(searchQuery, ignoreCase = true)
                    }) { challenge ->
                        ChallengeItem(
                            challenge = challenge,
                            onClick = {
                                currentEditChallenge = challenge
                                showEditDialog = true
                            }
                        )
                    }
                }
            }
            
            // Фильтры категорий
            val categories = challenges
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
                        selected = selectedCategory.isNullOrEmpty(),
                        onClick = { 
                            setSelectedCategory(null)
                            viewModel.setCategory(null)
                        },
                        label = { Text("Все") }
                    )
                    
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { 
                                setSelectedCategory(if (selectedCategory == category) null else category)
                                viewModel.setCategory(if (selectedCategory == category) null else category)
                            },
                            label = { Text(category) }
                        )
                    }
                }
            }
            
            // Список моих челленджей
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (viewModel.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = viewModel.error ?: "Неизвестная ошибка",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val filteredChallenges = challenges.filter { challenge ->
                    val matchesCategory = selectedCategory.isNullOrEmpty() || challenge.category == selectedCategory
                    val matchesSearch = searchQuery.isEmpty() || 
                            challenge.title.contains(searchQuery, ignoreCase = true) ||
                            challenge.description.contains(searchQuery, ignoreCase = true)
                    matchesCategory && matchesSearch
                }.distinctBy { it.id }
                
                if (filteredChallenges.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isNotBlank() || selectedCategory?.isNotBlank() == true) {
                                "Нет челленджей, соответствующих фильтрам"
                            } else {
                                "Нет активных челленджей.\nНажмите + чтобы добавить!"
                            },
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredChallenges) { challenge ->
                            ChallengeItem(
                                challenge = challenge,
                                onClick = {
                                    currentEditChallenge = challenge
                                    showEditDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Диалоги
    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description, category, points ->
                viewModel.createCustomChallenge(title, description, category, points)
                showAddDialog = false
            }
        )
    }
    
    if (showEditDialog && currentEditChallenge != null) {
        EditChallengeDialog(
            challenge = currentEditChallenge!!,
            onDismiss = { 
                showEditDialog = false
                currentEditChallenge = null
            },
            onConfirm = { title, description, category, points ->
                viewModel.editChallenge(currentEditChallenge!!, title, description, category, points)
                showEditDialog = false
                currentEditChallenge = null
            },
            onDelete = {
                viewModel.deleteChallenge(currentEditChallenge!!)
                showEditDialog = false
                currentEditChallenge = null
            }
        )
    }
}

@Composable
fun ChallengeItem(
    challenge: Challenge,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
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
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (challenge.isCustom) {
                        Text(
                            text = "Пользовательский",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    if (challenge.completed) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Выполнено",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
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
                    text = "${challenge.points} очков",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
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
                modifier = Modifier.fillMaxWidth(),
                color = NeonGreen,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
} 