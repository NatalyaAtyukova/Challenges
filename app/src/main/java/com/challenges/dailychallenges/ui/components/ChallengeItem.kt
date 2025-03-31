package com.challenges.dailychallenges.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.challenges.dailychallenges.data.model.Challenge
import com.challenges.dailychallenges.ui.animations.NeonGlowEffect
import com.challenges.dailychallenges.ui.animations.SlideInAnimation
import com.challenges.dailychallenges.ui.theme.NeonBlue
import com.challenges.dailychallenges.ui.theme.NeonGreen
import com.challenges.dailychallenges.ui.theme.NeonPink

@Composable
fun ChallengeItem(
    challenge: Challenge,
    onClick: () -> Unit,
    index: Int = 0
) {
    // Определяем цвет бордера в зависимости от категории
    val borderColor = when {
        challenge.category.contains("CONVERSATION", ignoreCase = true) -> NeonBlue
        challenge.category.contains("VIDEO", ignoreCase = true) -> NeonPink
        challenge.category.contains("PUBLIC", ignoreCase = true) -> NeonGreen
        else -> MaterialTheme.colorScheme.primary
    }
    
    SlideInAnimation(
        visible = true,
        index = index
    ) {
        NeonGlowEffect(
            glowColor = borderColor,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) { glowModifier ->
            Card(
                modifier = glowModifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { onClick() }
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(listOf(borderColor, Color.Transparent)),
                        shape = MaterialTheme.shapes.medium
                    ),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = challenge.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (challenge.completed) 
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = challenge.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (challenge.category.isNotEmpty()) {
                            SuggestionChip(
                                onClick = { },
                                label = { 
                                    Text(
                                        text = challenge.category,
                                        style = MaterialTheme.typography.labelSmall
                                    ) 
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = borderColor.copy(alpha = 0.2f),
                                    labelColor = borderColor
                                ),
                                border = null
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Text(
                            text = "${challenge.points} очков",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        
                        if (challenge.completed) {
                            Spacer(modifier = Modifier.width(8.dp))
                            SuggestionChip(
                                onClick = { },
                                label = { 
                                    Text(
                                        text = "Выполнено",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                                    labelColor = MaterialTheme.colorScheme.tertiary
                                ),
                                border = null
                            )
                        }
                    }
                }
            }
        }
    }
} 