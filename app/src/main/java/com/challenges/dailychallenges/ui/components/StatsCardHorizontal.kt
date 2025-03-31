package com.challenges.dailychallenges.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.challenges.dailychallenges.ui.animations.NeonGlowEffect
import com.challenges.dailychallenges.ui.theme.*

@Composable
fun StatsCardHorizontal(
    totalChallenges: Int,
    completedChallenges: Int,
    streakDays: Int
) {
    // Анимация числовых значений
    var displayTotal by remember { mutableStateOf(0) }
    var displayCompleted by remember { mutableStateOf(0) }
    var displayStreak by remember { mutableStateOf(0) }
    
    // Анимированный градиент
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient offset"
    )
    
    // Градиент для фона
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            DeepDarkBlue,
            DarkBlue.copy(alpha = 0.7f),
            DeepDarkBlue
        )
    )
    
    // Анимация чисел
    LaunchedEffect(totalChallenges, completedChallenges, streakDays) {
        animate(initialValue = 0f, targetValue = totalChallenges.toFloat()) { value, _ -> 
            displayTotal = value.toInt() 
        }
        animate(initialValue = 0f, targetValue = completedChallenges.toFloat()) { value, _ -> 
            displayCompleted = value.toInt() 
        }
        animate(initialValue = 0f, targetValue = streakDays.toFloat()) { value, _ -> 
            displayStreak = value.toInt() 
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonBlue.copy(alpha = 0.7f),
                        NeonPink.copy(alpha = 0.7f),
                        NeonGreen.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.dp, Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Общее количество
                NeonGlowEffect(
                    glowColor = NeonBlue,
                    modifier = Modifier.weight(1f)
                ) { modifier ->
                    StatItem(
                        modifier = modifier,
                        value = displayTotal.toString(),
                        title = "Всего",
                        color = NeonBlue
                    )
                }
                
                // Выполненные
                NeonGlowEffect(
                    glowColor = NeonGreen,
                    modifier = Modifier.weight(1f)
                ) { modifier ->
                    StatItem(
                        modifier = modifier,
                        value = displayCompleted.toString(),
                        title = "Выполнено",
                        color = NeonGreen
                    )
                }
                
                // Серия дней
                NeonGlowEffect(
                    glowColor = NeonPink,
                    modifier = Modifier.weight(1f)
                ) { modifier ->
                    StatItem(
                        modifier = modifier,
                        value = displayStreak.toString(),
                        title = "Дней подряд",
                        color = NeonPink
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    modifier: Modifier = Modifier,
    value: String,
    title: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = color,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
} 