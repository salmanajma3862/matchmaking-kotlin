package com.example.dummyapp.ui.screens.profile_setup

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dummyapp.ui.theme.AppSpacing
import com.example.dummyapp.viewmodel.ProfileSetupEvent
import com.example.dummyapp.viewmodel.ProfileSetupViewModel

import androidx.compose.runtime.LaunchedEffect
import com.example.dummyapp.viewmodel.ProfileSetupNavigationEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ProfileSetupScreen(
    onNavigateToHome: () -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ProfileSetupNavigationEvent.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Step ${state.currentStep} of ${state.totalSteps}",
                        style = MaterialTheme.typography.titleMedium
                    ) 
                },
                navigationIcon = {
                    if (state.currentStep > 1) {
                        IconButton(onClick = { viewModel.onEvent(ProfileSetupEvent.PreviousStep) }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = AppSpacing.ScreenHorizontal)
        ) {
            // Progress Bar
            LinearProgressIndicator(
                progress = { state.currentStep / state.totalSteps.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppSpacing.Small),
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            // Content
            Box(modifier = Modifier.weight(1f)) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    when (state.currentStep) {
                        1 -> Step1BasicInfo(state, viewModel::onEvent)
                        2 -> Step2Location(state, viewModel::onEvent)
                        3 -> Step3Photos(state, viewModel::onEvent)
                        4 -> Step4PhysicalDetails(state, viewModel::onEvent)
                        5 -> Step5Background(state, viewModel::onEvent)
                        6 -> Step6Habits(state, viewModel::onEvent)
                        7 -> Step7Interests(state, viewModel::onEvent)
                        8 -> Step8Preferences(state, viewModel::onEvent)
                        else -> Text("Coming Soon")
                    }
                }
            }
            
            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            // Navigation Buttons
            Button(
                onClick = { 
                    if (state.currentStep < 8) {
                        viewModel.onEvent(ProfileSetupEvent.NextStep)
                    } else {
                        // Finish
                        viewModel.onEvent(ProfileSetupEvent.NextStep)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isStepValid(state) && !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = if (state.currentStep == 8) "Finish" else "Next")
                }
            }
            
            Spacer(modifier = Modifier.height(AppSpacing.Large))
        }
    }
}

fun isStepValid(state: com.example.dummyapp.viewmodel.ProfileSetupState): Boolean {
    return when (state.currentStep) {
        1 -> state.name.isNotBlank() && state.gender.isNotBlank() && state.dob != null
        2 -> state.city.isNotBlank() && state.country.isNotBlank()
        3 -> state.photos.isNotEmpty()
        4 -> state.height.isNotBlank() && state.weight.isNotBlank()
        5 -> state.religion.isNotBlank() && state.maritalStatus.isNotBlank() && state.education.isNotBlank() && state.profession.isNotBlank()
        6 -> state.dietPreference.isNotBlank()
        7 -> state.interests.isNotEmpty() || state.hobbies.isNotEmpty() // At least one interest or hobby
        8 -> state.intention.isNotBlank()
        else -> true
    }
}
