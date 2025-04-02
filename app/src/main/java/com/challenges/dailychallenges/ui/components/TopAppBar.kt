package com.challenges.dailychallenges.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onSyncClicked: (suspend () -> Unit)? = null,
    onSignOutClicked: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    
    CenterAlignedTopAppBar(
        title = { Text(title) },
        actions = {
            if (actions != null) {
                actions()
            } else {
                if (onSyncClicked != null) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    onSyncClicked()
                                } catch (e: Exception) {
                                    Log.e("AppTopBar", "Error during sync: ${e.message}", e)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Синхронизировать с Firebase"
                        )
                    }
                }
                
                if (onSignOutClicked != null) {
                    IconButton(onClick = { onSignOutClicked() }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Выйти"
                        )
                    }
                }
            }
        }
    )
} 