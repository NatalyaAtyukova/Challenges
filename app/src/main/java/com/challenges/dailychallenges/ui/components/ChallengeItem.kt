package com.challenges.dailychallenges.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.challenges.dailychallenges.data.model.Challenge

@Composable
fun ChallengeItem(
    challenge: Challenge,
    onChallengeClick: (Challenge) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onChallengeClick(challenge) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = challenge.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
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
                    Chip(
                        onClick = { /* No action */ },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = challenge.category,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = "${challenge.points} очков",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                
                if (challenge.completed) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Chip(
                        onClick = { /* No action */ },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text(
                            text = "Выполнено",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
} 