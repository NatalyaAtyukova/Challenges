package com.challenges.dailychallenges.ui.screens.community

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.challenges.dailychallenges.R
import com.challenges.dailychallenges.data.model.Comment
import com.challenges.dailychallenges.ui.viewmodel.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeDetailsScreen(
    viewModel: CommunityViewModel,
    onNavigateBack: () -> Unit
) {
    var showReportDialog by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    val challenge = uiState.selectedChallenge ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(challenge.title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { showReportDialog = true }) {
                        Icon(Icons.Default.Report, contentDescription = stringResource(R.string.report))
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = challenge.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { /* TODO: Toggle like */ }
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = stringResource(R.string.like),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = challenge.likesCount.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    
                    Text(
                        text = "${challenge.points} ${stringResource(R.string.points)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = stringResource(R.string.comments),
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text(stringResource(R.string.add_comment)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (commentText.isNotBlank()) {
                                    viewModel.addComment(commentText)
                                    commentText = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (uiState.comments.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_comments),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(uiState.comments) { comment ->
                    CommentItem(comment = comment)
                }
            }
        }
    }

    if (showReportDialog) {
        var reportReason by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text(stringResource(R.string.report)) },
            text = {
                OutlinedTextField(
                    value = reportReason,
                    onValueChange = { reportReason = it },
                    label = { Text(stringResource(R.string.report_reason)) },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (reportReason.isNotBlank()) {
                            viewModel.reportChallenge(reportReason)
                            showReportDialog = false
                        }
                    }
                ) {
                    Text(stringResource(R.string.report))
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
                    text = comment.userName,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = comment.createdAt.toDate().toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { /* TODO: Toggle like */ }
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = stringResource(R.string.like),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = comment.likesCount.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                IconButton(
                    onClick = { /* TODO: Show report dialog */ }
                ) {
                    Icon(
                        Icons.Default.Report,
                        contentDescription = stringResource(R.string.report),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
} 