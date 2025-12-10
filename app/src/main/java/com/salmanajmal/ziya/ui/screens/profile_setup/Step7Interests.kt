package com.salmanajmal.ziya.ui.screens.profile_setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.salmanajmal.ziya.ui.theme.AppSpacing
import com.salmanajmal.ziya.viewmodel.ProfileSetupEvent
import com.salmanajmal.ziya.viewmodel.ProfileSetupState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step7Interests(
    state: ProfileSetupState,
    onEvent: (ProfileSetupEvent) -> Unit
) {
    val availableInterests = listOf(
        "Travel", "Music", "Movies", "Reading", "Cooking", "Sports", "Fitness",
        "Art", "Photography", "Technology", "Gaming", "Fashion", "Nature", "Animals"
    )
    
    val availableHobbies = listOf(
        "Hiking", "Swimming", "Cycling", "Painting", "Writing", "Dancing", "Singing",
        "Gardening", "Yoga", "Meditation", "Coding", "DIY", "Collecting"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Interests & Hobbies",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Text(
            text = "Select a few things you love.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Interests
        Text(
            text = "Interests",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableInterests.forEach { interest ->
                FilterChip(
                    selected = state.interests.contains(interest),
                    onClick = { onEvent(ProfileSetupEvent.ToggleInterest(interest)) },
                    label = { Text(interest) },
                    leadingIcon = if (state.interests.contains(interest)) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Hobbies
        Text(
            text = "Hobbies",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableHobbies.forEach { hobby ->
                FilterChip(
                    selected = state.hobbies.contains(hobby),
                    onClick = { onEvent(ProfileSetupEvent.ToggleHobby(hobby)) },
                    label = { Text(hobby) },
                    leadingIcon = if (state.hobbies.contains(hobby)) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }
    }
}
