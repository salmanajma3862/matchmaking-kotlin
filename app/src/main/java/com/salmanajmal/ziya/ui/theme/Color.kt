package com.salmanajmal.ziya.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Light Theme Colors - Elegant and Modern Palette
 * Inspired by premium matchmaking apps with warm, inviting tones
 */
object LightColors {
    // Primary Colors - Rose Gold & Deep Pink (Romance & Elegance)
    val Primary = Color(0xFFE91E63) // Vibrant pink for main actions
    val PrimaryVariant = Color(0xFFC2185B) // Deeper pink for emphasis
    val PrimaryLight = Color(0xFFF8BBD0) // Light pink for subtle backgrounds
    val PrimaryContainer = Color(0xFFFFEBEE) // Very light pink container
    
    // Secondary Colors - Teal & Turquoise (Trust & Sophistication)
    val Secondary = Color(0xFF00BCD4) // Bright teal for secondary actions
    val SecondaryVariant = Color(0xFF0097A7) // Darker teal
    val SecondaryLight = Color(0xFFB2EBF2) // Light teal
    val SecondaryContainer = Color(0xFFE0F7FA) // Very light teal container
    
    // Tertiary Colors - Purple (Premium & Exclusivity)
    val Tertiary = Color(0xFF9C27B0) // Rich purple
    val TertiaryVariant = Color(0xFF7B1FA2) // Deep purple
    val TertiaryLight = Color(0xFFE1BEE7) // Light purple
    val TertiaryContainer = Color(0xFFF3E5F5) // Very light purple
    
    // Background & Surface
    val Background = Color(0xFFFFFFFF) // Pure white background
    val Surface = Color(0xFFFFFFFF) // Pure white for cards
    val SurfaceVariant = Color(0xFFF5F5F5) // Subtle gray for variations
    val SurfaceContainer = Color(0xFFEEEEEE) // Container background
    val SurfaceContainerHigh = Color(0xFFE8E8E8) // Elevated container
    
    // Text Colors
    val OnPrimary = Color(0xFFFFFFFF) // White text on primary
    val OnSecondary = Color(0xFFFFFFFF) // White text on secondary
    val OnTertiary = Color(0xFFFFFFFF) // White text on tertiary
    val OnBackground = Color(0xFF1A1A1A) // Dark text on background
    val OnSurface = Color(0xFF1A1A1A) // Dark text on surface
    val OnSurfaceVariant = Color(0xFF424242) // Medium text
    
    // Text Hierarchy
    val TextPrimary = Color(0xFF1A1A1A) // Primary text - almost black
    val TextSecondary = Color(0xFF616161) // Secondary text - medium gray
    val TextTertiary = Color(0xFF9E9E9E) // Tertiary text - light gray
    val TextDisabled = Color(0xFFBDBDBD) // Disabled text
    val TextPlaceholder = Color(0xFFB0B0B0) // Placeholder text
    
    // Interactive Elements
    val ButtonPrimary = Color(0xFFE91E63) // Primary button
    val ButtonSecondary = Color(0xFFFFFFFF) // Secondary button (white)
    val ButtonDisabled = Color(0xFFE0E0E0) // Disabled button
    val ButtonText = Color(0xFFFFFFFF) // Button text
    val ButtonTextSecondary = Color(0xFFE91E63) // Secondary button text
    
    // Input Fields
    val InputBackground = Color(0xFFF5F5F5) // Input field background
    val InputBorder = Color(0xFFE0E0E0) // Input border
    val InputBorderFocused = Color(0xFFE91E63) // Focused input border
    val InputText = Color(0xFF1A1A1A) // Input text
    val InputPlaceholder = Color(0xFF9E9E9E) // Input placeholder
    val InputError = Color(0xFFD32F2F) // Error state
    
    // Status Colors
    val Success = Color(0xFF4CAF50) // Success green
    val SuccessContainer = Color(0xFFE8F5E9) // Success background
    val Warning = Color(0xFFFF9800) // Warning orange
    val WarningContainer = Color(0xFFFFF3E0) // Warning background
    val Error = Color(0xFFD32F2F) // Error red
    val ErrorContainer = Color(0xFFFFEBEE) // Error background
    val Info = Color(0xFF2196F3) // Info blue
    val InfoContainer = Color(0xFFE3F2FD) // Info background
    
    // Match & Swipe Actions
    val Like = Color(0xFF4CAF50) // Green for like
    val Dislike = Color(0xFFF44336) // Red for dislike/pass
    val SuperLike = Color(0xFF2196F3) // Blue for super like
    val Boost = Color(0xFFFF9800) // Orange for boost
    val Rewind = Color(0xFFFFEB3B) // Yellow for rewind
    
    // Messaging
    val MessageSent = Color(0xFFE91E63) // User's message bubble
    val MessageReceived = Color(0xFFF5F5F5) // Other's message bubble
    val MessageText = Color(0xFF1A1A1A) // Message text
    val MessageTime = Color(0xFF9E9E9E) // Timestamp
    val OnlineIndicator = Color(0xFF4CAF50) // Online status
    val TypingIndicator = Color(0xFF2196F3) // Typing indicator
    
    // Cards & Profiles
    val CardBackground = Color(0xFFFFFFFF) // Profile card background
    val CardBorder = Color(0xFFE0E0E0) // Card border
    val CardShadow = Color(0x1A000000) // Card shadow (10% black)
    val CardOverlay = Color(0x80000000) // Card overlay (50% black)
    val GradientOverlay = Color(0xCC000000) // Gradient overlay (80% black)
    
    // Premium & Subscription
    val Premium = Color(0xFFFFD700) // Gold for premium
    val PremiumGradientStart = Color(0xFFFFD700) // Gold gradient start
    val PremiumGradientEnd = Color(0xFFFF6F00) // Gold gradient end
    val Verified = Color(0xFF2196F3) // Blue checkmark for verified
    
    // Navigation & UI Elements
    val NavigationBar = Color(0xFFFFFFFF) // Bottom navigation
    val NavigationSelected = Color(0xFFE91E63) // Selected nav item
    val NavigationUnselected = Color(0xFF9E9E9E) // Unselected nav item
    val TabSelected = Color(0xFFE91E63) // Selected tab
    val TabUnselected = Color(0xFF9E9E9E) // Unselected tab
    val Divider = Color(0xFFE0E0E0) // Divider line
    val Ripple = Color(0x1FE91E63) // Ripple effect (12% primary)
    
    // Overlays & Modals
    val Scrim = Color(0x99000000) // Modal scrim (60% black)
    val DialogBackground = Color(0xFFFFFFFF) // Dialog background
    val BottomSheetBackground = Color(0xFFFFFFFF) // Bottom sheet
    val TooltipBackground = Color(0xFF616161) // Tooltip background
    val TooltipText = Color(0xFFFFFFFF) // Tooltip text
    
    // Badges & Notifications
    val Badge = Color(0xFFE91E63) // Notification badge
    val BadgeText = Color(0xFFFFFFFF) // Badge text
    val NotificationDot = Color(0xFFE91E63) // Unread indicator
    val UnreadBackground = Color(0xFFFFEBEE) // Unread message background
    
    // Ratings & Reviews
    val StarFilled = Color(0xFFFFB300) // Filled star (amber)
    val StarEmpty = Color(0xFFE0E0E0) // Empty star
    val RatingGood = Color(0xFF4CAF50) // Good rating
    val RatingBad = Color(0xFFF44336) // Bad rating
    
    // Progress & Loading
    val ProgressBackground = Color(0xFFE0E0E0) // Progress bar background
    val ProgressForeground = Color(0xFFE91E63) // Progress bar fill
    val LoadingSpinner = Color(0xFFE91E63) // Loading spinner
    val Shimmer = Color(0xFFE0E0E0) // Shimmer effect
    val ShimmerHighlight = Color(0xFFF5F5F5) // Shimmer highlight
    
    // Special Effects
    val GlassSurface = Color(0xCCFFFFFF) // Glassmorphism (80% white)
    val BlurOverlay = Color(0x66000000) // Blur overlay (40% black)
    val GradientStart = Color(0xFFE91E63) // Gradient start
    val GradientEnd = Color(0xFF9C27B0) // Gradient end
}

/**
 * Dark Theme Colors - Elegant Dark Mode
 * Designed for comfortable viewing in low-light conditions
 */
object DarkColors {
    // Primary Colors - Softer Pink (Easier on eyes)
    val Primary = Color(0xFFFF4081) // Lighter pink for dark mode
    val PrimaryVariant = Color(0xFFF50057) // Vivid pink
    val PrimaryLight = Color(0xFFFF80AB) // Very light pink
    val PrimaryContainer = Color(0xFF3E2723) // Dark brown-pink container
    
    // Secondary Colors - Bright Teal
    val Secondary = Color(0xFF18FFFF) // Bright cyan for dark mode
    val SecondaryVariant = Color(0xFF00E5FF) // Electric cyan
    val SecondaryLight = Color(0xFF84FFFF) // Very light cyan
    val SecondaryContainer = Color(0xFF1A2F35) // Dark teal container
    
    // Tertiary Colors - Bright Purple
    val Tertiary = Color(0xFFCE93D8) // Light purple for dark mode
    val TertiaryVariant = Color(0xFFBA68C8) // Medium purple
    val TertiaryLight = Color(0xFFE1BEE7) // Very light purple
    val TertiaryContainer = Color(0xFF2E1A35) // Dark purple container
    
    // Background & Surface
    val Background = Color(0xFF121212) // Pure dark background
    val Surface = Color(0xFF1E1E1E) // Elevated surface
    val SurfaceVariant = Color(0xFF2C2C2C) // Lighter surface variant
    val SurfaceContainer = Color(0xFF252525) // Container background
    val SurfaceContainerHigh = Color(0xFF333333) // Elevated container
    
    // Text Colors
    val OnPrimary = Color(0xFF000000) // Black text on primary
    val OnSecondary = Color(0xFF000000) // Black text on secondary
    val OnTertiary = Color(0xFF000000) // Black text on tertiary
    val OnBackground = Color(0xFFE0E0E0) // Light text on background
    val OnSurface = Color(0xFFE0E0E0) // Light text on surface
    val OnSurfaceVariant = Color(0xFFB0B0B0) // Medium text
    
    // Text Hierarchy
    val TextPrimary = Color(0xFFE0E0E0) // Primary text - light gray
    val TextSecondary = Color(0xFFB0B0B0) // Secondary text - medium gray
    val TextTertiary = Color(0xFF808080) // Tertiary text - darker gray
    val TextDisabled = Color(0xFF606060) // Disabled text
    val TextPlaceholder = Color(0xFF707070) // Placeholder text
    
    // Interactive Elements
    val ButtonPrimary = Color(0xFFFF4081) // Primary button
    val ButtonSecondary = Color(0xFF2C2C2C) // Secondary button (dark)
    val ButtonDisabled = Color(0xFF3A3A3A) // Disabled button
    val ButtonText = Color(0xFF000000) // Button text (dark on bright)
    val ButtonTextSecondary = Color(0xFFFF4081) // Secondary button text
    
    // Input Fields
    val InputBackground = Color(0xFF2C2C2C) // Input field background
    val InputBorder = Color(0xFF404040) // Input border
    val InputBorderFocused = Color(0xFFFF4081) // Focused input border
    val InputText = Color(0xFFE0E0E0) // Input text
    val InputPlaceholder = Color(0xFF808080) // Input placeholder
    val InputError = Color(0xFFEF5350) // Error state
    
    // Status Colors
    val Success = Color(0xFF66BB6A) // Success green (lighter)
    val SuccessContainer = Color(0xFF1B3A1C) // Success background
    val Warning = Color(0xFFFFB74D) // Warning orange (lighter)
    val WarningContainer = Color(0xFF3E2A1A) // Warning background
    val Error = Color(0xFFEF5350) // Error red (lighter)
    val ErrorContainer = Color(0xFF3E1A1A) // Error background
    val Info = Color(0xFF42A5F5) // Info blue (lighter)
    val InfoContainer = Color(0xFF1A2A3E) // Info background
    
    // Match & Swipe Actions
    val Like = Color(0xFF66BB6A) // Lighter green for like
    val Dislike = Color(0xFFEF5350) // Lighter red for dislike
    val SuperLike = Color(0xFF42A5F5) // Lighter blue for super like
    val Boost = Color(0xFFFFB74D) // Lighter orange for boost
    val Rewind = Color(0xFFFFF176) // Lighter yellow for rewind
    
    // Messaging
    val MessageSent = Color(0xFFFF4081) // User's message bubble
    val MessageReceived = Color(0xFF2C2C2C) // Other's message bubble
    val MessageText = Color(0xFFE0E0E0) // Message text
    val MessageTime = Color(0xFF808080) // Timestamp
    val OnlineIndicator = Color(0xFF66BB6A) // Online status
    val TypingIndicator = Color(0xFF42A5F5) // Typing indicator
    
    // Cards & Profiles
    val CardBackground = Color(0xFF1E1E1E) // Profile card background
    val CardBorder = Color(0xFF404040) // Card border
    val CardShadow = Color(0x33000000) // Card shadow (20% black)
    val CardOverlay = Color(0x99000000) // Card overlay (60% black)
    val GradientOverlay = Color(0xE6000000) // Gradient overlay (90% black)
    
    // Premium & Subscription
    val Premium = Color(0xFFFFD54F) // Lighter gold for premium
    val PremiumGradientStart = Color(0xFFFFD54F) // Gold gradient start
    val PremiumGradientEnd = Color(0xFFFF8A65) // Gold gradient end
    val Verified = Color(0xFF42A5F5) // Blue checkmark for verified
    
    // Navigation & UI Elements
    val NavigationBar = Color(0xFF1E1E1E) // Bottom navigation
    val NavigationSelected = Color(0xFFFF4081) // Selected nav item
    val NavigationUnselected = Color(0xFF808080) // Unselected nav item
    val TabSelected = Color(0xFFFF4081) // Selected tab
    val TabUnselected = Color(0xFF808080) // Unselected tab
    val Divider = Color(0xFF404040) // Divider line
    val Ripple = Color(0x1FFF4081) // Ripple effect (12% primary)
    
    // Overlays & Modals
    val Scrim = Color(0xCC000000) // Modal scrim (80% black)
    val DialogBackground = Color(0xFF2C2C2C) // Dialog background
    val BottomSheetBackground = Color(0xFF2C2C2C) // Bottom sheet
    val TooltipBackground = Color(0xFF424242) // Tooltip background
    val TooltipText = Color(0xFFE0E0E0) // Tooltip text
    
    // Badges & Notifications
    val Badge = Color(0xFFFF4081) // Notification badge
    val BadgeText = Color(0xFF000000) // Badge text
    val NotificationDot = Color(0xFFFF4081) // Unread indicator
    val UnreadBackground = Color(0xFF3E1A1A) // Unread message background
    
    // Ratings & Reviews
    val StarFilled = Color(0xFFFFCA28) // Filled star (lighter amber)
    val StarEmpty = Color(0xFF404040) // Empty star
    val RatingGood = Color(0xFF66BB6A) // Good rating
    val RatingBad = Color(0xFFEF5350) // Bad rating
    
    // Progress & Loading
    val ProgressBackground = Color(0xFF404040) // Progress bar background
    val ProgressForeground = Color(0xFFFF4081) // Progress bar fill
    val LoadingSpinner = Color(0xFFFF4081) // Loading spinner
    val Shimmer = Color(0xFF2C2C2C) // Shimmer effect
    val ShimmerHighlight = Color(0xFF404040) // Shimmer highlight
    
    // Special Effects
    val GlassSurface = Color(0xCC1E1E1E) // Glassmorphism (80% dark)
    val BlurOverlay = Color(0x99000000) // Blur overlay (60% black)
    val GradientStart = Color(0xFFFF4081) // Gradient start
    val GradientEnd = Color(0xFFCE93D8) // Gradient end
}