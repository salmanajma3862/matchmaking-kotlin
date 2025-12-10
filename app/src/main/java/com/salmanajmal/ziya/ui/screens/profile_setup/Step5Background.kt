package com.salmanajmal.ziya.ui.screens.profile_setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.salmanajmal.ziya.ui.components.inputs.AppTextField
import com.salmanajmal.ziya.ui.theme.AppSpacing
import com.salmanajmal.ziya.viewmodel.ProfileSetupEvent
import com.salmanajmal.ziya.viewmodel.ProfileSetupState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step5Background(
    state: ProfileSetupState,
    onEvent: (ProfileSetupEvent) -> Unit
) {
    val religions = listOf("Islam", "Christianity", "Hinduism", "Sikhism", "Other")
    val maritalStatuses = listOf("Single", "Divorced", "Widowed")
    val incomeRanges = listOf("Less than 50k", "50k - 100k", "100k - 200k", "200k - 500k", "500k+")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Background & Lifestyle",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Text(
            text = "Share your background to find compatible matches.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Religion Dropdown
        DropdownField(
            label = "Religion",
            value = state.religion,
            options = religions,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateReligion(it)) }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Sect (Optional)
        AppTextField(
            value = state.sect,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateSect(it)) },
            label = "Sect (Optional)",
            placeholder = "e.g. Sunni, Shia",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Marital Status Dropdown
        DropdownField(
            label = "Marital Status",
            value = state.maritalStatus,
            options = maritalStatuses,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateMaritalStatus(it)) }
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Education
        AppTextField(
            value = state.education,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateEducation(it)) },
            label = "Education",
            placeholder = "e.g. Bachelors in CS",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Profession
        AppTextField(
            value = state.profession,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateProfession(it)) },
            label = "Profession",
            placeholder = "e.g. Software Engineer",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Income Range Dropdown
        DropdownField(
            label = "Income Range",
            value = state.incomeRange,
            options = incomeRanges,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateIncomeRange(it)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text("Select $label") },
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
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
