package com.challenges.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory

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
    var category by remember { mutableStateOf(challenge?.category ?: ChallengeCategory.OTHER.value) }
    var points by remember { mutableStateOf(challenge?.points?.toString() ?: "50") }
    
    // Для выпадающего списка категорий
    var expanded by remember { mutableStateOf(false) }
    val categories = ChallengeCategory.values()

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
                
                // Выпадающий список категорий
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = ChallengeCategory.getDisplayName(category),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Категория") },
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, "Выбрать категорию")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { categoryOption ->
                            DropdownMenuItem(
                                text = { Text(categoryOption.displayName) },
                                onClick = {
                                    category = categoryOption.value
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                OutlinedTextField(
                    value = points,
                    onValueChange = { 
                        // Разрешаем только цифры
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            points = it 
                        }
                    },
                    label = { Text("Очки") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        onSave(
                            title,
                            description,
                            category,
                            points.toIntOrNull() ?: 50
                        )
                    }
                },
                enabled = title.isNotBlank() && description.isNotBlank()
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