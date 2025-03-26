package com.challenges.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.challenges.R
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory

@Composable
fun LoadingSpinner() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Snackbar(
        action = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    ) {
        Text(message)
    }
}

@Composable
fun CategoryChips(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("CONVERSATION", "VIDEO", "PUBLIC", "DAILY")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(getCategoryName(category)) }
            )
        }
    }
}

@Composable
fun getCategoryName(category: String): String {
    return when (category) {
        "CONVERSATION" -> stringResource(R.string.category_conversation)
        "VIDEO" -> stringResource(R.string.category_video)
        "PUBLIC" -> stringResource(R.string.category_public)
        "DAILY" -> stringResource(R.string.category_daily)
        else -> category
    }
} 