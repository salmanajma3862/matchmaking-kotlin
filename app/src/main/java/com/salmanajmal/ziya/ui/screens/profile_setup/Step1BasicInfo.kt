package com.salmanajmal.ziya.ui.screens.profile_setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1BasicInfo(
    state: ProfileSetupState,
    onEvent: (ProfileSetupEvent) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.dob
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Let's start with the basics",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Text(
            text = "Tell us a bit about yourself so we can find the best matches for you.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Name
        AppTextField(
            value = state.name,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateName(it)) },
            label = "Full Name",
            placeholder = "Enter your full name",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Date of Birth
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.dob?.let { 
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it)) 
                } ?: "",
                onValueChange = {},
                label = { Text("Date of Birth") },
                placeholder = { Text("Select your date of birth") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = false, // Disable typing
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledContainerColor = Color.Transparent
                )
            )
            
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        onEvent(ProfileSetupEvent.UpdateDob(datePickerState.selectedDateMillis))
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // Gender
        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Male", "Female").forEach { gender ->
                Row(
                    Modifier
                        .weight(1f)
                        .height(56.dp)
                        .selectable(
                            selected = (state.gender.equals(gender, ignoreCase = true)),
                            onClick = { onEvent(ProfileSetupEvent.UpdateGender(gender.lowercase())) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (state.gender.equals(gender, ignoreCase = true)),
                        onClick = null // null recommended for accessibility with selectable
                    )
                    Text(
                        text = gender,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}
