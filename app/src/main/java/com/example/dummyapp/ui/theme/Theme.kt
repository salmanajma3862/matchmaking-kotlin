package com.example.dummyapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Light Theme Color Scheme
 * Comprehensive color palette for light mode
 */
private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = LightColors.Primary,
    onPrimary = LightColors.OnPrimary,
    primaryContainer = LightColors.PrimaryContainer,
    onPrimaryContainer = LightColors.OnPrimary,
    
    // Secondary colors
    secondary = LightColors.Secondary,
    onSecondary = LightColors.OnSecondary,
    secondaryContainer = LightColors.SecondaryContainer,
    onSecondaryContainer = LightColors.OnSecondary,
    
    // Tertiary colors
    tertiary = LightColors.Tertiary,
    onTertiary = LightColors.OnTertiary,
    tertiaryContainer = LightColors.TertiaryContainer,
    onTertiaryContainer = LightColors.OnTertiary,
    
    // Error colors
    error = LightColors.Error,
    onError = Color.White,
    errorContainer = LightColors.ErrorContainer,
    onErrorContainer = LightColors.Error,
    
    // Background colors
    background = LightColors.Background,
    onBackground = LightColors.OnBackground,
    
    // Surface colors
    surface = LightColors.Surface,
    onSurface = LightColors.OnSurface,
    surfaceVariant = LightColors.SurfaceVariant,
    onSurfaceVariant = LightColors.OnSurfaceVariant,
    
    // Outline
    outline = LightColors.Divider,
    outlineVariant = LightColors.CardBorder,
    
    // Inverse colors
    inverseSurface = DarkColors.Surface,
    inverseOnSurface = DarkColors.OnSurface,
    inversePrimary = DarkColors.Primary,
    
    // Scrim
    scrim = LightColors.Scrim,
    
    // Surface tints
    surfaceTint = LightColors.Primary
)

/**
 * Dark Theme Color Scheme
 * Comprehensive color palette for dark mode
 */
private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = DarkColors.Primary,
    onPrimary = DarkColors.OnPrimary,
    primaryContainer = DarkColors.PrimaryContainer,
    onPrimaryContainer = DarkColors.OnPrimary,
    
    // Secondary colors
    secondary = DarkColors.Secondary,
    onSecondary = DarkColors.OnSecondary,
    secondaryContainer = DarkColors.SecondaryContainer,
    onSecondaryContainer = DarkColors.OnSecondary,
    
    // Tertiary colors
    tertiary = DarkColors.Tertiary,
    onTertiary = DarkColors.OnTertiary,
    tertiaryContainer = DarkColors.TertiaryContainer,
    onTertiaryContainer = DarkColors.OnTertiary,
    
    // Error colors
    error = DarkColors.Error,
    onError = Color.Black,
    errorContainer = DarkColors.ErrorContainer,
    onErrorContainer = DarkColors.Error,
    
    // Background colors
    background = DarkColors.Background,
    onBackground = DarkColors.OnBackground,
    
    // Surface colors
    surface = DarkColors.Surface,
    onSurface = DarkColors.OnSurface,
    surfaceVariant = DarkColors.SurfaceVariant,
    onSurfaceVariant = DarkColors.OnSurfaceVariant,
    
    // Outline
    outline = DarkColors.Divider,
    outlineVariant = DarkColors.CardBorder,
    
    // Inverse colors
    inverseSurface = LightColors.Surface,
    inverseOnSurface = LightColors.OnSurface,
    inversePrimary = LightColors.Primary,
    
    // Scrim
    scrim = DarkColors.Scrim,
    
    // Surface tints
    surfaceTint = DarkColors.Primary
)

/**
 * Extended Colors - Custom colors not in Material Theme
 * Access these via LocalExtendedColors.current
 */
data class ExtendedColors(
    // Match & Swipe Actions
    val like: Color,
    val dislike: Color,
    val superLike: Color,
    val boost: Color,
    val rewind: Color,
    
    // Messaging
    val messageSent: Color,
    val messageReceived: Color,
    val messageText: Color,
    val messageTime: Color,
    val onlineIndicator: Color,
    val typingIndicator: Color,
    
    // Premium & Subscription
    val premium: Color,
    val premiumGradientStart: Color,
    val premiumGradientEnd: Color,
    val verified: Color,
    
    // Status Colors
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val info: Color,
    val infoContainer: Color,
    
    // Text Hierarchy
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val textPlaceholder: Color,
    
    // Buttons
    val buttonPrimary: Color,
    val buttonSecondary: Color,
    val buttonDisabled: Color,
    val buttonText: Color,
    val buttonTextSecondary: Color,
    
    // Inputs
    val inputBackground: Color,
    val inputBorder: Color,
    val inputBorderFocused: Color,
    val inputText: Color,
    val inputPlaceholder: Color,
    val inputError: Color,
    
    // Cards
    val cardBackground: Color,
    val cardBorder: Color,
    val cardShadow: Color,
    val cardOverlay: Color,
    val gradientOverlay: Color,
    
    // Navigation
    val navigationBar: Color,
    val navigationSelected: Color,
    val navigationUnselected: Color,
    val divider: Color,
    val ripple: Color,
    
    // Overlays
    val dialogBackground: Color,
    val bottomSheetBackground: Color,
    val tooltipBackground: Color,
    val tooltipText: Color,
    
    // Badges
    val badge: Color,
    val badgeText: Color,
    val notificationDot: Color,
    val unreadBackground: Color,
    
    // Ratings
    val starFilled: Color,
    val starEmpty: Color,
    
    // Progress
    val progressBackground: Color,
    val progressForeground: Color,
    val loadingSpinner: Color,
    
    // Special Effects
    val glassSurface: Color,
    val blurOverlay: Color,
    val gradientStart: Color,
    val gradientEnd: Color
)

/**
 * Light Extended Colors
 */
private val LightExtendedColors = ExtendedColors(
    like = LightColors.Like,
    dislike = LightColors.Dislike,
    superLike = LightColors.SuperLike,
    boost = LightColors.Boost,
    rewind = LightColors.Rewind,
    messageSent = LightColors.MessageSent,
    messageReceived = LightColors.MessageReceived,
    messageText = LightColors.MessageText,
    messageTime = LightColors.MessageTime,
    onlineIndicator = LightColors.OnlineIndicator,
    typingIndicator = LightColors.TypingIndicator,
    premium = LightColors.Premium,
    premiumGradientStart = LightColors.PremiumGradientStart,
    premiumGradientEnd = LightColors.PremiumGradientEnd,
    verified = LightColors.Verified,
    success = LightColors.Success,
    successContainer = LightColors.SuccessContainer,
    warning = LightColors.Warning,
    warningContainer = LightColors.WarningContainer,
    info = LightColors.Info,
    infoContainer = LightColors.InfoContainer,
    textPrimary = LightColors.TextPrimary,
    textSecondary = LightColors.TextSecondary,
    textTertiary = LightColors.TextTertiary,
    textDisabled = LightColors.TextDisabled,
    textPlaceholder = LightColors.TextPlaceholder,
    buttonPrimary = LightColors.ButtonPrimary,
    buttonSecondary = LightColors.ButtonSecondary,
    buttonDisabled = LightColors.ButtonDisabled,
    buttonText = LightColors.ButtonText,
    buttonTextSecondary = LightColors.ButtonTextSecondary,
    inputBackground = LightColors.InputBackground,
    inputBorder = LightColors.InputBorder,
    inputBorderFocused = LightColors.InputBorderFocused,
    inputText = LightColors.InputText,
    inputPlaceholder = LightColors.InputPlaceholder,
    inputError = LightColors.InputError,
    cardBackground = LightColors.CardBackground,
    cardBorder = LightColors.CardBorder,
    cardShadow = LightColors.CardShadow,
    cardOverlay = LightColors.CardOverlay,
    gradientOverlay = LightColors.GradientOverlay,
    navigationBar = LightColors.NavigationBar,
    navigationSelected = LightColors.NavigationSelected,
    navigationUnselected = LightColors.NavigationUnselected,
    divider = LightColors.Divider,
    ripple = LightColors.Ripple,
    dialogBackground = LightColors.DialogBackground,
    bottomSheetBackground = LightColors.BottomSheetBackground,
    tooltipBackground = LightColors.TooltipBackground,
    tooltipText = LightColors.TooltipText,
    badge = LightColors.Badge,
    badgeText = LightColors.BadgeText,
    notificationDot = LightColors.NotificationDot,
    unreadBackground = LightColors.UnreadBackground,
    starFilled = LightColors.StarFilled,
    starEmpty = LightColors.StarEmpty,
    progressBackground = LightColors.ProgressBackground,
    progressForeground = LightColors.ProgressForeground,
    loadingSpinner = LightColors.LoadingSpinner,
    glassSurface = LightColors.GlassSurface,
    blurOverlay = LightColors.BlurOverlay,
    gradientStart = LightColors.GradientStart,
    gradientEnd = LightColors.GradientEnd
)

/**
 * Dark Extended Colors
 */
private val DarkExtendedColors = ExtendedColors(
    like = DarkColors.Like,
    dislike = DarkColors.Dislike,
    superLike = DarkColors.SuperLike,
    boost = DarkColors.Boost,
    rewind = DarkColors.Rewind,
    messageSent = DarkColors.MessageSent,
    messageReceived = DarkColors.MessageReceived,
    messageText = DarkColors.MessageText,
    messageTime = DarkColors.MessageTime,
    onlineIndicator = DarkColors.OnlineIndicator,
    typingIndicator = DarkColors.TypingIndicator,
    premium = DarkColors.Premium,
    premiumGradientStart = DarkColors.PremiumGradientStart,
    premiumGradientEnd = DarkColors.PremiumGradientEnd,
    verified = DarkColors.Verified,
    success = DarkColors.Success,
    successContainer = DarkColors.SuccessContainer,
    warning = DarkColors.Warning,
    warningContainer = DarkColors.WarningContainer,
    info = DarkColors.Info,
    infoContainer = DarkColors.InfoContainer,
    textPrimary = DarkColors.TextPrimary,
    textSecondary = DarkColors.TextSecondary,
    textTertiary = DarkColors.TextTertiary,
    textDisabled = DarkColors.TextDisabled,
    textPlaceholder = DarkColors.TextPlaceholder,
    buttonPrimary = DarkColors.ButtonPrimary,
    buttonSecondary = DarkColors.ButtonSecondary,
    buttonDisabled = DarkColors.ButtonDisabled,
    buttonText = DarkColors.ButtonText,
    buttonTextSecondary = DarkColors.ButtonTextSecondary,
    inputBackground = DarkColors.InputBackground,
    inputBorder = DarkColors.InputBorder,
    inputBorderFocused = DarkColors.InputBorderFocused,
    inputText = DarkColors.InputText,
    inputPlaceholder = DarkColors.InputPlaceholder,
    inputError = DarkColors.InputError,
    cardBackground = DarkColors.CardBackground,
    cardBorder = DarkColors.CardBorder,
    cardShadow = DarkColors.CardShadow,
    cardOverlay = DarkColors.CardOverlay,
    gradientOverlay = DarkColors.GradientOverlay,
    navigationBar = DarkColors.NavigationBar,
    navigationSelected = DarkColors.NavigationSelected,
    navigationUnselected = DarkColors.NavigationUnselected,
    divider = DarkColors.Divider,
    ripple = DarkColors.Ripple,
    dialogBackground = DarkColors.DialogBackground,
    bottomSheetBackground = DarkColors.BottomSheetBackground,
    tooltipBackground = DarkColors.TooltipBackground,
    tooltipText = DarkColors.TooltipText,
    badge = DarkColors.Badge,
    badgeText = DarkColors.BadgeText,
    notificationDot = DarkColors.NotificationDot,
    unreadBackground = DarkColors.UnreadBackground,
    starFilled = DarkColors.StarFilled,
    starEmpty = DarkColors.StarEmpty,
    progressBackground = DarkColors.ProgressBackground,
    progressForeground = DarkColors.ProgressForeground,
    loadingSpinner = DarkColors.LoadingSpinner,
    glassSurface = DarkColors.GlassSurface,
    blurOverlay = DarkColors.BlurOverlay,
    gradientStart = DarkColors.GradientStart,
    gradientEnd = DarkColors.GradientEnd
)

/**
 * CompositionLocal for Extended Colors
 */
val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

/**
 * Main App Theme
 * 
 * @param darkTheme Whether to use dark theme (defaults to system setting)
 * @param dynamicColor Whether to use dynamic colors on Android 12+ (disabled by default for consistent branding)
 * @param content The content to be themed
 * 
 * Usage:
 * ```
 * MatchmakingTheme(darkTheme = false) {
 *     // Your app content
 * }
 * ```
 * 
 * Access extended colors:
 * ```
 * val extendedColors = LocalExtendedColors.current
 * Text("Hello", color = extendedColors.textPrimary)
 * ```
 */
@Composable
fun MatchmakingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled for consistent branding
    content: @Composable () -> Unit
) {
    // Choose color scheme
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    // Choose extended colors
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors
    
    // Update system bars
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            
            // Set status bar icons to dark on light theme, light on dark theme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }
    
    // Provide extended colors
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

/**
 * Alias for backward compatibility
 */
@Composable
fun DummyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MatchmakingTheme(darkTheme, dynamicColor, content)
}
