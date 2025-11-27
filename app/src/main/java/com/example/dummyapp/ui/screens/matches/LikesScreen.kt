package com.example.dummyapp.ui.screens.matches

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.viewmodel.MatchViewModel

@Composable
fun LikesScreen(
    viewModel: MatchViewModel = hiltViewModel(),
    onNavigateToProfileDetail: (String, String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Sent", "Received")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> SentTab(viewModel, onNavigateToProfileDetail)
            1 -> ReceivedTab(viewModel, onNavigateToProfileDetail)
        }
    }
}
