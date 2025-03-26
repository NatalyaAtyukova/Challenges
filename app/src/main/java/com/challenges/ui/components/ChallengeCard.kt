package com.challenges.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.challenges.data.model.Challenge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeCard(
    challenge: Challenge,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCompletionClick: () -> Unit,
    onNotesChange: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(challenge.notes ?: "") }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { showNotesDialog = true }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    IconButton(onClick = onCompletionClick) {
                        Icon(
                            if (challenge.isCompleted) Icons.Default.CheckCircle
                            else Icons.Default.CheckCircleOutline,
                            contentDescription = if (challenge.isCompleted) "Выполнено" else "Не выполнено",
                            tint = if (challenge.isCompleted) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            if (challenge.isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = if (challenge.isFavorite) "В избранном" else "Добавить в избранное"
                        )
                    }
                    IconButton(onClick = onShareClick) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Поделиться"
                        )
                    }
                    if (challenge.isCustom) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Редактировать"
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Удалить"
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = challenge.category,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${challenge.points} очков",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить челлендж") },
            text = { Text("Вы уверены, что хотите удалить этот челлендж?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showNotesDialog) {
        AlertDialog(
            onDismissRequest = { showNotesDialog = false },
            title = { Text("Заметки") },
            text = {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onNotesChange(notes)
                        showNotesDialog = false
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotesDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
} 