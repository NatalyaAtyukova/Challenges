package com.challenges.dailychallenges.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

// Пульсация неоновых цветов
@Composable
fun NeonPulseEffect(
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        modifier = modifier,
        content = content
    )
}

// Анимированное появление для элементов списка
@Composable
fun SlideInAnimation(
    visible: Boolean,
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it * (index + 1) / 3 },
            animationSpec = tween(durationMillis = 300, delayMillis = 50 * index)
        ) + fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 50 * index)),
        exit = slideOutVertically() + fadeOut(),
        modifier = modifier,
        content = content
    )
}

// Анимация пульсации для кнопок и важных элементов
@Composable
fun PulseAnimation(
    modifier: Modifier = Modifier,
    pulseMagnitude: Float = 0.05f,
    content: @Composable (Modifier) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f + pulseMagnitude,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box {
        content(modifier.scale(scale))
    }
}

// Эффект неонового свечения
@Composable
fun NeonGlowEffect(
    modifier: Modifier = Modifier,
    glowColor: Color,
    content: @Composable (Modifier) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    Box {
        // Сначала рисуем свечение
        content(modifier.graphicsLayer {
            alpha = glowAlpha
            shadowElevation = 10f
            ambientShadowColor = glowColor
            spotShadowColor = glowColor
        })
        
        // Затем рисуем основной контент поверх
        content(modifier)
    }
}

// Эффект появления с масштабированием
@Composable
fun ScaleInAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            transformOrigin = TransformOrigin(0.5f, 0.5f)
        ) + fadeIn(),
        exit = scaleOut() + fadeOut(),
        modifier = modifier,
        content = content
    )
}

// Эффект появления диалогов 
@Composable
fun DialogAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandIn(
            expandFrom = Alignment.Center,
            initialSize = { IntSize(0, 0) }
        ) + fadeIn(),
        exit = shrinkOut(
            shrinkTowards = Alignment.Center,
            targetSize = { IntSize(0, 0) }
        ) + fadeOut(),
        modifier = modifier,
        content = content
    )
} 