package com.challenges

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenges.R
import com.challenges.data.model.Challenge
import com.challenges.data.model.ChallengeCategory
import com.challenges.ui.components.*
import com.challenges.ui.theme.ChallengesTheme
import com.challenges.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengesTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                var selectedTab by remember { mutableStateOf(0) }
                val context = LocalContext.current
                val appName = stringResource(R.string.app_name)
                val shareVia = stringResource(R.string.share_via)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(stringResource(R.string.app_name)) }
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                                    label = { Text(stringResource(R.string.home)) }
                                )
                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                                    label = { Text(stringResource(R.string.favorites)) }
                                )
                            }
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            if (selectedTab == 0) {
                                CategoryChips(
                                    categories = ChallengeCategory.values().toList(),
                                    selectedCategory = uiState.selectedCategory,
                                    onCategorySelected = { viewModel.selectCategory(it) }
                                )

                                if (uiState.isLoading) {
                                    LoadingSpinner()
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(vertical = 8.dp)
                                    ) {
                                        items(uiState.challenges) { challenge ->
                                            ChallengeCard(
                                                challenge = challenge,
                                                onFavoriteClick = { viewModel.toggleFavorite(challenge) },
                                                onShareClick = {
                                                    val shareText = buildString {
                                                        append(challenge.title)
                                                        append("\n\n")
                                                        append(challenge.description)
                                                        append("\n\n")
                                                        append(appName)
                                                    }

                                                    val sendIntent = Intent().apply {
                                                        action = Intent.ACTION_SEND
                                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                                        type = "text/plain"
                                                    }

                                                    context.startActivity(Intent.createChooser(sendIntent, shareVia))
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                if (uiState.isLoading) {
                                    LoadingSpinner()
                                } else {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(vertical = 8.dp)
                                    ) {
                                        items(uiState.challenges.filter { it.isFavorite }) { challenge ->
                                            ChallengeCard(
                                                challenge = challenge,
                                                onFavoriteClick = { viewModel.toggleFavorite(challenge) },
                                                onShareClick = {
                                                    val shareText = buildString {
                                                        append(challenge.title)
                                                        append("\n\n")
                                                        append(challenge.description)
                                                        append("\n\n")
                                                        append(appName)
                                                    }

                                                    val sendIntent = Intent().apply {
                                                        action = Intent.ACTION_SEND
                                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                                        type = "text/plain"
                                                    }

                                                    context.startActivity(Intent.createChooser(sendIntent, shareVia))
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            uiState.error?.let { error ->
                                LaunchedEffect(error) {
                                    snackbarHostState.showSnackbar(error)
                                    viewModel.clearError()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}