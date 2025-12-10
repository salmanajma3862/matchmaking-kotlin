package com.salmanajmal.ziya.ui.screens.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.salmanajmal.ziya.data.models.User
import com.salmanajmal.ziya.utils.NetworkResult
import com.salmanajmal.ziya.viewmodel.MatchViewModel
import java.util.Calendar

@Composable
fun MatchScreen(
    viewModel: MatchViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onNavigateToProfileDetail: (String, String) -> Unit
) {
    val matchesState by viewModel.matches.collectAsState()

    when (val state = matchesState) {
        is NetworkResult.Loading -> LoadingView()
        is NetworkResult.Error -> ErrorView(state.message)
        is NetworkResult.Success -> {
            val matches = state.data
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header
                Surface(
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = "Matches",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "${matches?.size ?: 0} ${if ((matches?.size ?: 0) == 1) "match" else "matches"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                if (matches.isNullOrEmpty()) {
                    EmptyView("No matches yet.")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(matches) { match ->
                            match.user?.let { user ->
                                MatchProfileCard(
                                    user = user,
                                    isNew = false, // You might want to map this from data if available
                                    onMessage = { onNavigateToProfileDetail(user.id, "match") }, // Or open chat directly
                                    onViewProfile = { onNavigateToProfileDetail(user.id, "match") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

