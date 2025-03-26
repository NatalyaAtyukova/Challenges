package com.challenges.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.challenges.ui.components.LoadingSpinner
import com.challenges.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    
    // If authenticated, navigate to main screen
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onNavigateToMain()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            LoadingSpinner()
        } else {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isLogin) "Вход" else "Регистрация",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Display name field (only for registration)
                if (!isLogin) {
                    OutlinedTextField(
                        value = displayName,
                        onValueChange = { displayName = it },
                        label = { Text("Имя пользователя") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Error message
                if (uiState.error.isNotEmpty()) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                // Submit button
                Button(
                    onClick = {
                        if (isLogin) {
                            viewModel.signIn(email, password)
                        } else {
                            viewModel.signUp(email, password, displayName)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isLogin) "Войти" else "Зарегистрироваться"
                    )
                }
                
                // Toggle button
                TextButton(
                    onClick = { isLogin = !isLogin },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = if (isLogin) "Создать аккаунт" else "Уже есть аккаунт"
                    )
                }
            }
        }
    }
} 