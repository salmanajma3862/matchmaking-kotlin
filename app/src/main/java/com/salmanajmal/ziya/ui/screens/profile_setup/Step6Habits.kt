package com.salmanajmal.ziya.ui.screens.profile_setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.salmanajmal.ziya.ui.theme.AppSpacing
import com.salmanajmal.ziya.viewmodel.ProfileSetupEvent
import com.salmanajmal.ziya.viewmodel.ProfileSetupState

@Composable
fun Step6Habits(
    state: ProfileSetupState,
    onEvent: (ProfileSetupEvent) -> Unit
) {
    val dietPreferences = listOf("Vegetarian", "Non-Vegetarian", "Vegan", "Halal")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Habits & Preferences",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Text(
            text = "Be honest about your lifestyle.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Smoking
        YesNoQuestion(
            question = "Do you smoke?",
            answer = state.smoking,
            onAnswerChange = { onEvent(ProfileSetupEvent.UpdateSmoking(it)) }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Drinking
        YesNoQuestion(
            question = "Do you drink alcohol?",
            answer = state.drinking,
            onAnswerChange = { onEvent(ProfileSetupEvent.UpdateDrinking(it)) }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Living with Family
        YesNoQuestion(
            question = "Do you live with your family?",
            answer = state.livingWithFamily,
            onAnswerChange = { onEvent(ProfileSetupEvent.UpdateLivingWithFamily(it)) }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Diet Preference
        Text(
            text = "Dietary Preference",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        DropdownField(
            label = "Diet",
            value = state.dietPreference,
            options = dietPreferences,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateDietPreference(it)) }
        )
    }
}

@Composable
fun YesNoQuestion(
    question: String,
    answer: Boolean,
    onAnswerChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .weight(1f)
                    .height(56.dp)
                    .selectable(
                        selected = answer,
                        onClick = { onAnswerChange(true) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = answer,
                    onClick = null
                )
                Text(
                    text = "Yes",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                Modifier
                    .weight(1f)
                    .height(56.dp)
                    .selectable(
                        selected = !answer,
                        onClick = { onAnswerChange(false) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = !answer,
                    onClick = null
                )
                Text(
                    text = "No",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
