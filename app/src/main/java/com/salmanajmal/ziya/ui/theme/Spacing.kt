package com.salmanajmal.ziya.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing System for Consistent Layout
 * Following 8dp grid system for Material Design
 * 
 * Usage:
 * - Use spacing values consistently across the app
 * - Prefer these constants over hardcoded values
 * - Maintains visual rhythm and consistency
 */
object AppSpacing {
    // Extra Small Spacing
    val ExtraSmall: Dp = 4.dp
    val ExtraSmall2x: Dp = 8.dp
    
    // Small Spacing
    val Small: Dp = 12.dp
    val Small2x: Dp = 16.dp
    
    // Medium Spacing
    val Medium: Dp = 20.dp
    val Medium2x: Dp = 24.dp
    
    // Large Spacing
    val Large: Dp = 28.dp
    val Large2x: Dp = 32.dp
    
    // Extra Large Spacing
    val ExtraLarge: Dp = 40.dp
    val ExtraLarge2x: Dp = 48.dp
    
    // Huge Spacing (for headers, sections)
    val Huge: Dp = 56.dp
    val Huge2x: Dp = 64.dp
    
    // Common Use Cases
    val None: Dp = 0.dp
    val Hairline: Dp = 1.dp
    val Divider: Dp = 2.dp
    
    // Padding Values
    val PaddingTiny: Dp = 4.dp
    val PaddingSmall: Dp = 8.dp
    val PaddingMedium: Dp = 16.dp
    val PaddingLarge: Dp = 24.dp
    val PaddingExtraLarge: Dp = 32.dp
    
    // Margin Values
    val MarginTiny: Dp = 4.dp
    val MarginSmall: Dp = 8.dp
    val MarginMedium: Dp = 16.dp
    val MarginLarge: Dp = 24.dp
    val MarginExtraLarge: Dp = 32.dp
    
    // Screen Edge Padding
    val ScreenHorizontal: Dp = 16.dp
    val ScreenVertical: Dp = 16.dp
    val ScreenTop: Dp = 16.dp
    val ScreenBottom: Dp = 16.dp
    
    // Card Spacing
    val CardPadding: Dp = 16.dp
    val CardMargin: Dp = 12.dp
    val CardSpacing: Dp = 12.dp
    
    // List Item Spacing
    val ListItemPadding: Dp = 16.dp
    val ListItemSpacing: Dp = 8.dp
    val ListItemVertical: Dp = 12.dp
    val ListItemHorizontal: Dp = 16.dp
    
    // Button Spacing
    val ButtonPaddingHorizontal: Dp = 24.dp
    val ButtonPaddingVertical: Dp = 12.dp
    val ButtonSpacing: Dp = 12.dp
    val ButtonIconSpacing: Dp = 8.dp
    
    // Input Field Spacing
    val InputPaddingHorizontal: Dp = 16.dp
    val InputPaddingVertical: Dp = 14.dp
    val InputSpacing: Dp = 16.dp
    val InputLabelSpacing: Dp = 8.dp
    
    // Icon Spacing
    val IconSmall: Dp = 16.dp
    val IconMedium: Dp = 24.dp
    val IconLarge: Dp = 32.dp
    val IconExtraLarge: Dp = 48.dp
    val IconPadding: Dp = 8.dp
    
    // Dialog & Modal Spacing
    val DialogPadding: Dp = 24.dp
    val DialogSpacing: Dp = 16.dp
    val BottomSheetPadding: Dp = 24.dp
    val ModalPadding: Dp = 20.dp
    
    // Chip & Badge Spacing
    val ChipPaddingHorizontal: Dp = 12.dp
    val ChipPaddingVertical: Dp = 6.dp
    val ChipSpacing: Dp = 8.dp
    val BadgePadding: Dp = 4.dp
    val BadgeOffset: Dp = (-4).dp
    
    // Profile Card Spacing
    val ProfileCardPadding: Dp = 20.dp
    val ProfileImageSpacing: Dp = 16.dp
    val ProfileInfoSpacing: Dp = 12.dp
    
    // Navigation Spacing
    val NavigationHeight: Dp = 56.dp
    val NavigationItemPadding: Dp = 8.dp
    val TabHeight: Dp = 48.dp
    val TabPadding: Dp = 16.dp
    
    // Message Bubble Spacing
    val MessageBubblePadding: Dp = 12.dp
    val MessageSpacing: Dp = 8.dp
    val MessageAvatarSize: Dp = 32.dp
    val MessageAvatarSpacing: Dp = 8.dp
    
    // Section Spacing
    val SectionSpacing: Dp = 24.dp
    val SectionTitleSpacing: Dp = 12.dp
    val SectionContentSpacing: Dp = 16.dp
    
    // Swipe Card Spacing
    val SwipeCardPadding: Dp = 16.dp
    val SwipeCardActionPadding: Dp = 24.dp
    val SwipeButtonSpacing: Dp = 16.dp
}

/**
 * Elevation Values for Consistent Shadows
 */
object AppElevation {
    val None: Dp = 0.dp
    val Level1: Dp = 1.dp // Subtle elevation
    val Level2: Dp = 2.dp // Cards at rest
    val Level3: Dp = 4.dp // Cards raised
    val Level4: Dp = 6.dp // Dialogs
    val Level5: Dp = 8.dp // Navigation bar
    val Level6: Dp = 12.dp // Floating action button
    val Level7: Dp = 16.dp // Modal bottom sheet
    val Level8: Dp = 24.dp // Modal dialogs
}

/**
 * Border & Stroke Widths
 */
object AppBorder {
    val None: Dp = 0.dp
    val Thin: Dp = 1.dp
    val Medium: Dp = 2.dp
    val Thick: Dp = 3.dp
    val ExtraThick: Dp = 4.dp
    
    // Common Use Cases
    val Default: Dp = 1.dp
    val Focused: Dp = 2.dp
    val Selected: Dp = 2.dp
    val Divider: Dp = 1.dp
}

/**
 * Size Constants for Common UI Elements
 */
object AppSize {
    // Avatar Sizes
    val AvatarTiny: Dp = 24.dp
    val AvatarSmall: Dp = 32.dp
    val AvatarMedium: Dp = 48.dp
    val AvatarLarge: Dp = 64.dp
    val AvatarExtraLarge: Dp = 96.dp
    val AvatarHuge: Dp = 128.dp
    
    // Button Heights
    val ButtonSmall: Dp = 32.dp
    val ButtonMedium: Dp = 40.dp
    val ButtonLarge: Dp = 48.dp
    val ButtonExtraLarge: Dp = 56.dp
    
    // Input Field Heights
    val InputSmall: Dp = 40.dp
    val InputMedium: Dp = 48.dp
    val InputLarge: Dp = 56.dp
    
    // Icon Sizes
    val IconTiny: Dp = 12.dp
    val IconSmall: Dp = 16.dp
    val IconMedium: Dp = 24.dp
    val IconLarge: Dp = 32.dp
    val IconExtraLarge: Dp = 48.dp
    
    // Thumbnail Sizes
    val ThumbnailSmall: Dp = 48.dp
    val ThumbnailMedium: Dp = 80.dp
    val ThumbnailLarge: Dp = 120.dp
    
    // Progress Indicator Sizes
    val ProgressSmall: Dp = 24.dp
    val ProgressMedium: Dp = 36.dp
    val ProgressLarge: Dp = 48.dp
    
    // Badge Sizes
    val BadgeSmall: Dp = 16.dp
    val BadgeMedium: Dp = 20.dp
    val BadgeLarge: Dp = 24.dp
    
    // Swipe Card Size
    val SwipeCardHeight: Dp = 560.dp
    val SwipeCardWidth: Dp = 340.dp
    
    // Bottom Sheet
    val BottomSheetMinHeight: Dp = 200.dp
    val BottomSheetPeekHeight: Dp = 120.dp
    
    // Dialog
    val DialogMaxWidth: Dp = 560.dp
    val DialogMinWidth: Dp = 280.dp
    
    // Minimum Touch Target (Accessibility)
    val MinTouchTarget: Dp = 48.dp
}
