package com.salmanajmal.ziya.ui.screens.profile_setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.salmanajmal.ziya.ui.components.inputs.AppTextField
import com.salmanajmal.ziya.ui.theme.AppSpacing
import com.salmanajmal.ziya.viewmodel.ProfileSetupEvent
import com.salmanajmal.ziya.viewmodel.ProfileSetupState

@Composable
fun Step2Location(
    state: ProfileSetupState,
    onEvent: (ProfileSetupEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Where are you located?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(AppSpacing.Small))
        
        Text(
            text = "This helps us find matches near you.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(AppSpacing.Large))

        // City
        AppTextField(
            value = state.city,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateCity(it)) },
            label = "City",
            placeholder = "Enter your city",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Country
        AppTextField(
            value = state.country,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateCountry(it)) },
            label = "Country",
            placeholder = "Enter your country",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        // Address (Optional)
        AppTextField(
            value = state.address,
            onValueChange = { onEvent(ProfileSetupEvent.UpdateAddress(it)) },
            label = "Address (Optional)",
            placeholder = "Enter your address",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
