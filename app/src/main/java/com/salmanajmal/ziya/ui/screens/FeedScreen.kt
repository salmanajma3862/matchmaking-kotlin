package com.salmanajmal.ziya.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.salmanajmal.ziya.data.models.FeedFilters
import com.salmanajmal.ziya.ui.components.ActiveFilterChips
import com.salmanajmal.ziya.ui.components.FeedFilterSheet
import com.salmanajmal.ziya.ui.components.cards.SwipeableCard
import com.salmanajmal.ziya.viewmodel.FeedViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onNavigateToProfileDetail: (String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val userList by viewModel.userList.collectAsState()
    val filters by viewModel.filters.collectAsState()
    
    // Bottom sheet state
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Filter sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            FeedFilterSheet(
                currentFilters = filters,
                onApply = { newFilters ->
                    viewModel.applyFilters(newFilters)
                    showFilterSheet = false
                },
                onDismiss = { showFilterSheet = false }
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar with filter button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Filter button with badge
                Box {
                    IconButton(
                        onClick = { showFilterSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filters",
                            tint = if (filters.hasActiveFilters()) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    // Filter count badge
                    val filterCount = filters.activeFilterCount()
                    if (filterCount > 0) {
                        Badge(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-4).dp, y = 4.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                text = filterCount.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            
            // Active filter chips
            ActiveFilterChips(
                filters = filters,
                onClearFilter = { filterName ->
                    viewModel.clearFilter(filterName)
                }
            )
            
            // Main content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (userList.isEmpty()) {
                    // Empty State or Loading
                    var showRetryButton by remember { mutableStateOf(false) }

                    LaunchedEffect(userList.isEmpty()) {
                        if (userList.isEmpty()) {
                            showRetryButton = false
                            delay(5000) // 5 seconds timeout
                            showRetryButton = true
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (filters.hasActiveFilters()) 
                                "No profiles match your filters" 
                            else 
                                "No more profiles to show",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (filters.hasActiveFilters()) {
                            Text(
                                text = "Try adjusting your filters",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedButton(onClick = { showFilterSheet = true }) {
                                    Text("Adjust Filters")
                                }
                                Button(onClick = { viewModel.clearAllFilters() }) {
                                    Text("Clear Filters")
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (showRetryButton) {
                                Button(onClick = {
                                    showRetryButton = false
                                    viewModel.refreshFeed()
                                }) {
                                    Text("Try Again")
                                }
                            } else {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Searching for more...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    // Card Stack
                    userList.asReversed().forEach { user ->
                        key(user.id) {
                            SwipeableCard(
                                user = user,
                                onSwipeLeft = {
                                    viewModel.swipeUser(user.id, "dislike")
                                },
                                onSwipeRight = {
                                    viewModel.swipeUser(user.id, "like")
                                },
                                onClick = {
                                    onNavigateToProfileDetail(user.id)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
