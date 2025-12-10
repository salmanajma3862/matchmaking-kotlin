package com.salmanajmal.ziya.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.salmanajmal.ziya.ui.theme.*

/**
 * Primary Button Component
 * Main action button with consistent styling
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null
) {
    val colors = LocalExtendedColors.current
    
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(AppSize.ButtonLarge),
        enabled = enabled && !loading,
        shape = CustomShapes.ButtonMedium,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.buttonPrimary,
            contentColor = colors.buttonText,
            disabledContainerColor = colors.buttonDisabled,
            disabledContentColor = colors.textDisabled
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = AppElevation.Level2,
            pressedElevation = AppElevation.Level1,
            disabledElevation = AppElevation.None
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = colors.buttonText,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.ButtonIconSpacing))
                }
                Text(
                    text = text,
                    style = CustomTextStyles.ButtonLarge
                )
            }
        }
    }
}

/**
 * Secondary Button Component
 * Secondary action button with outlined style
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val colors = LocalExtendedColors.current
    
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(AppSize.ButtonLarge),
        enabled = enabled,
        shape = CustomShapes.ButtonMedium,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colors.buttonSecondary,
            contentColor = colors.buttonTextSecondary,
            disabledContainerColor = colors.buttonDisabled,
            disabledContentColor = colors.textDisabled
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = AppBorder.Medium,
            brush = androidx.compose.ui.graphics.SolidColor(colors.buttonTextSecondary)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(AppSpacing.ButtonIconSpacing))
            }
            Text(
                text = text,
                style = CustomTextStyles.ButtonLarge
            )
        }
    }
}

/**
 * Text Button Component
 * Minimal button for less prominent actions
 */
@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val colors = LocalExtendedColors.current
    
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = colors.buttonPrimary,
            disabledContentColor = colors.textDisabled
        )
    ) {
        Text(
            text = text,
            style = CustomTextStyles.ButtonSmall
        )
    }
}
