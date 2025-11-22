package com.example.dummyapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.ui.components.cards.SwipeableCard
import com.example.dummyapp.viewmodel.FeedViewModel

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val userList by viewModel.userList.collectAsState()
    // We can also observe loading state if needed, but userList updates are enough for now

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (userList.isEmpty()) {
            // Empty State or Loading
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No more profiles to show.",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Searching for more...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        } else {
            // Card Stack
            // We reverse the list so the first user in the array is at the bottom of the stack
            // and the last user is at the top, ready to be swiped.
            // Wait, if we want to swipe through the list in order [0, 1, 2],
            // we should render [2, 1, 0] so 0 is on top.
            // But standard Box rendering is: first child at bottom, last child at top.
            // So if we render users.forEach, the last user in the list is on top.
            // If we want to swipe User[0] first, we need User[0] to be on top.
            // So we should render User[N]...User[0].
            // So we need to reverse the list for rendering.
            
            userList.asReversed().forEach { user ->
                key(user.id) { // Use key to help Compose identify items
                    SwipeableCard(
                        user = user,
                        onSwipeLeft = {
                            viewModel.swipeUser(user.id, "dislike")
                        },
                        onSwipeRight = {
                            viewModel.swipeUser(user.id, "like")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
