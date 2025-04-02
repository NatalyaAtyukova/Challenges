package com.challenges.dailychallenges.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenges.dailychallenges.data.model.Achievement
import com.challenges.dailychallenges.ui.components.AppTopBar
import com.challenges.dailychallenges.ui.components.BottomNavigation
import com.challenges.dailychallenges.ui.components.LoadingSpinner
import com.challenges.dailychallenges.ui.viewmodel.AchievementViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementScreen(
    navController: NavController,
    viewModel: AchievementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Достижения",
                onSyncClicked = null
            )
        },
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingSpinner()
        } else if (uiState.error != null && uiState.error.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Achievement stats
                AchievementStats(
                    totalCount = uiState.totalCount,
                    unlockedCount = uiState.unlockedCount,
                    points = uiState.achievementPoints,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                
                // Unlock progress
                LinearProgressIndicator(
                    progress = if (uiState.totalCount > 0) {
                        uiState.unlockedCount.toFloat() / uiState.totalCount
                    } else {
                        0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Achievements list
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Unlocked achievements section
                    if (uiState.unlockedAchievements.isNotEmpty()) {
                        item {
                            Text(
                                text = "Разблокированные (${uiState.unlockedAchievements.size})",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(uiState.unlockedAchievements) { achievement ->
                            AchievementCard(
                                achievement = achievement,
                                isUnlocked = true
                            )
                        }
                    }
                    
                    // Locked achievements section
                    if (uiState.lockedAchievements.isNotEmpty()) {
                        item {
                            Text(
                                text = "Заблокированные (${uiState.lockedAchievements.size})",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(uiState.lockedAchievements) { achievement ->
                            AchievementCard(
                                achievement = achievement,
                                isUnlocked = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementStats(
    totalCount: Int,
    unlockedCount: Int,
    points: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$unlockedCount/$totalCount",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Разблокировано",
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$points",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Очки достижений",
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${(unlockedCount.toFloat() / totalCount * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Выполнено",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AchievementCard(
    achievement: Achievement,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Achievement icon
            val icon = when (achievement.iconName) {
                "emoji_events" -> Icons.Default.EmojiEvents
                "workspace_premium" -> Icons.Default.WorkspacePremium
                "military_tech" -> Icons.Default.MilitaryTech
                "toll" -> Icons.Default.Toll
                "diamond" -> Icons.Default.Diamond
                "chat" -> Icons.Default.Chat
                "fitness_center" -> Icons.Default.FitnessCenter
                "brush" -> Icons.Default.Brush
                else -> Icons.Default.Star
            }
            
            Icon(
                imageVector = icon,
                contentDescription = achievement.title,
                tint = if (isUnlocked) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                )
                
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress indicator
                if (!isUnlocked) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${achievement.progress}/${achievement.threshold}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            
                            Text(
                                text = "${achievement.points} очков",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        LinearProgressIndicator(
                            progress = achievement.progress.toFloat() / achievement.threshold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "+${achievement.points} очков",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
} 