package com.challenges.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.challenges.data.model.Challenge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDialog(
    challenge: Challenge? = null,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(challenge?.title ?: "") }
    var description by remember { mutableStateOf(challenge?.description ?: "") }
    var category by remember { mutableStateOf(challenge?.category ?: "") }
    var points by remember { mutableStateOf(challenge?.points?.toString() ?: "0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (challenge == null) "Новый челлендж" else "Редактировать челлендж") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Категория") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = points,
                    onValueChange = { points = it },
                    label = { Text("Очки") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        title,
                        description,
                        category,
                        points.toIntOrNull() ?: 0
                    )
                    onDismiss()
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
} 