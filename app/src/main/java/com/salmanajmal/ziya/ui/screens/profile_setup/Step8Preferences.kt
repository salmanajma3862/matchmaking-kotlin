package com.salmanajmal.ziya.ui.screens.profile_setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.salmanajmal.ziya.ui.components.inputs.AppTextField
import com.salmanajmal.ziya.ui.theme.AppSpacing
import com.salmanajmal.ziya.viewmodel.ProfileSetupEvent
import com.salmanajmal.ziya.viewmodel.ProfileSetupState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step8Preferences(
    state: ProfileSetupState,
    onEvent: (ProfileSetupEvent) -> Unit
) {
    val intentions = listOf("Marriage", "Friendship", "Dating", "Not Sure")
    val timeframes = listOf("Soon", "6 Months", "1 Year", "Not Sure")
    val genders = listOf("Male", "Female", "Both")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "What are you looking for?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Text(
            text = "Set your preferences to find the right partner.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Intention
        DropdownField(
            label = "I'm looking for",
            value = state.intention,
            options = intentions,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateIntention(it)) }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Timeframe
        DropdownField(
            label = "Ready for marriage in",
            value = state.readyForMarriageTimeframe,
            options = timeframes,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateReadyForMarriageTimeframe(it)) }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Text(
            text = "Partner Preferences",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Partner Gender
        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            genders.forEach { gender ->
                Row(
                    Modifier
                        .weight(1f)
                        .height(48.dp)
                        .selectable(
                            selected = (state.partnerGender.equals(gender, ignoreCase = true)),
                            onClick = { onEvent(ProfileSetupEvent.UpdatePartnerGender(gender.lowercase())) },
                            role = Role.RadioButton
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (state.partnerGender.equals(gender, ignoreCase = true)),
                        onClick = null
                    )
                    Text(
                        text = gender,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Age Range
        Text(
            text = "Age Range: ${state.partnerMinAge.roundToInt()} - ${state.partnerMaxAge.roundToInt()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        RangeSlider(
            value = state.partnerMinAge..state.partnerMaxAge,
            onValueChange = { range ->
                onEvent(ProfileSetupEvent.UpdatePartnerAgeRange(range.start, range.endInclusive))
            },
            valueRange = 18f..100f,
            steps = 82
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Partner City
        AppTextField(
            value = state.partnerCity,
            onValueChange = { onEvent(ProfileSetupEvent.UpdatePartnerCity(it)) },
            label = "Preferred City (Optional)",
            placeholder = "Any",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
