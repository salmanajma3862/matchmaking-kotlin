package com.salmanajmal.ziya.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.salmanajmal.ziya.ui.theme.AppSpacing
import com.salmanajmal.ziya.ui.theme.LocalExtendedColors

/**
 * Profile Setup Screen (Placeholder)
 * Will contain Name, Age, Gender, etc.
 */
@Composable
fun ProfileSetupScreen(
    onNavigateToHome: () -> Unit
) {
    val colors = LocalExtendedColors.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(AppSpacing.ScreenHorizontal)
        ) {
            Text(
                text = "Profile Setup",
                style = MaterialTheme.typography.headlineLarge,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Medium))
            
            Text(
                text = "This is a placeholder for the profile setup flow.",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary
            )
            
            Spacer(modifier = Modifier.height(AppSpacing.Large))
            
            // TODO: Implement profile setup flow
        }
    }
}
