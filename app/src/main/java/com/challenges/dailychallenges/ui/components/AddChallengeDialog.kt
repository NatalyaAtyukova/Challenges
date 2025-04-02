package com.challenges.dailychallenges.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.challenges.dailychallenges.data.model.ChallengeCategory
import com.challenges.dailychallenges.data.model.ChallengeDifficulty
import com.challenges.dailychallenges.ui.animations.DialogAnimation
import com.challenges.dailychallenges.ui.theme.NeonBlue
import com.challenges.dailychallenges.ui.theme.NeonGreen
import com.challenges.dailychallenges.ui.theme.NeonPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChallengeDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, category: String, points: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ChallengeCategory.OTHER.toString()) }
    var categoryDisplayName by remember { mutableStateOf(ChallengeCategory.OTHER.displayName) }
    var points by remember { mutableStateOf(50) }
    var expanded by remember { mutableStateOf(false) }
    
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = true) {
        isVisible = true
    }
    
    Dialog(
        onDismissRequest = onDismiss
    ) {
        DialogAnimation(visible = isVisible) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            listOf(NeonBlue, NeonPink, NeonGreen),
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Новый челлендж",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Заголовок") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Описание") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Выбор категории
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = categoryDisplayName,
                            onValueChange = { },
                            label = { Text("Категория") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.clickable { expanded = true }
                                )
                            },
                            readOnly = true
                        )
                        
                        // Invisible clickable overlay
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { expanded = true }
                        )
                        
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(vertical = 4.dp)
                        ) {
                            ChallengeCategory.values().forEach { categoryOption ->
                                DropdownMenuItem(
                                    text = { Text(categoryOption.displayName) },
                                    onClick = {
                                        category = categoryOption.toString()
                                        categoryDisplayName = categoryOption.displayName
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Выбор баллов
                    Text(
                        text = "Баллы: $points",
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Slider(
                        value = points.toFloat(),
                        onValueChange = { points = it.toInt() },
                        valueRange = 10f..100f,
                        steps = 9,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                        ) {
                            Text("Отмена")
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Button(
                            onClick = {
                                onConfirm(title, description, category, points)
                            },
                            enabled = title.isNotBlank() && description.isNotBlank()
                        ) {
                            Text("Добавить")
                        }
                    }
                }
            }
        }
    }
} 