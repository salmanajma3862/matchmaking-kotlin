package com.example.dummyapp.ui.screens.profile_setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.dummyapp.ui.components.inputs.AppTextField
import com.example.dummyapp.ui.theme.AppSpacing
import com.example.dummyapp.viewmodel.ProfileSetupEvent
import com.example.dummyapp.viewmodel.ProfileSetupState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step4PhysicalDetails(
    state: ProfileSetupState,
    onEvent: (ProfileSetupEvent) -> Unit
) {
    val bodyTypes = listOf("Slim", "Athletic", "Average", "Curvy", "Heavy", "Other")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Physical & Personal Details",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Text(
            text = "Help others get a better idea of who you are.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        Row(modifier = Modifier.fillMaxWidth()) {
            // Height
            AppTextField(
                value = state.height,
                onValueChange = { onEvent(ProfileSetupEvent.UpdateHeight(it)) },
                label = "Height (cm)",
                placeholder = "175",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(AppSpacing.Medium))

            // Weight
            AppTextField(
                value = state.weight,
                onValueChange = { onEvent(ProfileSetupEvent.UpdateWeight(it)) },
                label = "Weight (kg)",
                placeholder = "70",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Body Type Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.bodyType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Body Type") },
                placeholder = { Text("Select body type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                bodyTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(text = type) },
                        onClick = {
                            onEvent(ProfileSetupEvent.UpdateBodyType(type))
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Bio
        AppTextField(
            value = state.bio,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateBio(it)) },
            label = "Bio",
            placeholder = "Write a short bio about yourself...",
            singleLine = false,
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
