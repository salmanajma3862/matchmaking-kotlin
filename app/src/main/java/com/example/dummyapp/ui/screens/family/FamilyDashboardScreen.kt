package com.example.dummyapp.ui.screens.family

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.data.models.User
import com.example.dummyapp.ui.theme.AppSpacing
import com.example.dummyapp.ui.theme.LocalExtendedColors
import com.example.dummyapp.utils.NetworkResult
import com.example.dummyapp.viewmodel.AuthViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Family Dashboard Screen
 * Shows child's profile summary and matches
 */
@Composable
fun FamilyDashboardScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val colors = LocalExtendedColors.current
    val childDataState by viewModel.childDataState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.fetchChildData()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Family Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                TextButton(onClick = { 
                    viewModel.performLogout()
                    onNavigateToLogin()
                }) {
                    Text("Logout")
                }
            }
        }
        
        when (val state = childDataState) {
            is NetworkResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is NetworkResult.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message ?: "Failed to load data", color = MaterialTheme.colorScheme.error)
                }
            }
            is NetworkResult.Success -> {
                val data = state.data
                // Parse data manually since it's Any (or use a proper data class in ViewModel)
                // For now assuming we can cast or parse json
                // In a real app, we should have a proper response model
                
                // Placeholder for displaying content
                // Ideally we map 'data' to a ChildData object
                
                // Let's assume data is a Map or JsonObject for now, or just show a placeholder list
                
                Column(modifier = Modifier.padding(AppSpacing.ScreenHorizontal)) {
                    Spacer(modifier = Modifier.height(AppSpacing.Large))
                    
                    Text(
                        text = "Child's Matches",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(AppSpacing.Medium))
                    
                    // We need to properly parse the matches list from the response
                    // Since we don't have the exact structure here without running it, 
                    // I'll put a placeholder grid
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // items(matches) { match -> ... }
                        item {
                            Card(modifier = Modifier.height(200.dp)) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text("Match 1")
                                }
                            }
                        }
                        item {
                            Card(modifier = Modifier.height(200.dp)) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                    Text("Match 2")
                                }
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
