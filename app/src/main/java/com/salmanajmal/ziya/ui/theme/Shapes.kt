package com.salmanajmal.ziya.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape System for Consistent UI Elements
 * Following Material Design 3 shape system
 * 
 * Categories:
 * - None: Square corners (0dp)
 * - ExtraSmall: Subtle rounding (4dp)
 * - Small: Light rounding (8dp)
 * - Medium: Moderate rounding (12dp)
 * - Large: Pronounced rounding (16dp)
 * - ExtraLarge: Heavy rounding (28dp)
 * - Full: Circular (50%)
 */

// Material 3 Shape Scale
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * Custom Shapes for Specific UI Elements
 */
object CustomShapes {
    // Basic Rounded Corners
    val None = RoundedCornerShape(0.dp)
    val ExtraSmall = RoundedCornerShape(4.dp)
    val Small = RoundedCornerShape(8.dp)
    val Medium = RoundedCornerShape(12.dp)
    val Large = RoundedCornerShape(16.dp)
    val ExtraLarge = RoundedCornerShape(28.dp)
    val Huge = RoundedCornerShape(32.dp)
    val Circle = CircleShape
    
    // Button Shapes
    val ButtonSmall = RoundedCornerShape(8.dp)
    val ButtonMedium = RoundedCornerShape(12.dp)
    val ButtonLarge = RoundedCornerShape(16.dp)
    val ButtonPill = RoundedCornerShape(50) // Fully rounded
    
    // Card Shapes
    val CardSmall = RoundedCornerShape(8.dp)
    val CardMedium = RoundedCornerShape(12.dp)
    val CardLarge = RoundedCornerShape(16.dp)
    val CardExtraLarge = RoundedCornerShape(20.dp)
    
    // Profile Card Shape (rounded top corners, square bottom for swipe)
    val ProfileCard = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomStart = 20.dp,
        bottomEnd = 20.dp
    )
    
    // Profile Image Shapes
    val ProfileImageSmall = RoundedCornerShape(8.dp)
    val ProfileImageMedium = RoundedCornerShape(12.dp)
    val ProfileImageCircle = CircleShape
    
    // Input Field Shapes
    val InputSmall = RoundedCornerShape(8.dp)
    val InputMedium = RoundedCornerShape(12.dp)
    val InputLarge = RoundedCornerShape(16.dp)
    
    // Chip Shapes
    val ChipSmall = RoundedCornerShape(8.dp)
    val ChipMedium = RoundedCornerShape(12.dp)
    val ChipPill = RoundedCornerShape(50) // Fully rounded pill
    
    // Badge Shape
    val Badge = CircleShape
    val BadgeSquare = RoundedCornerShape(4.dp)
    
    // Dialog & Bottom Sheet Shapes
    val Dialog = RoundedCornerShape(24.dp)
    val BottomSheet = RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 28.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    val ModalSheet = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    
    // Message Bubble Shapes
    val MessageSent = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 4.dp // Less rounded on bottom right
    )
    val MessageReceived = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 4.dp, // Less rounded on bottom left
        bottomEnd = 16.dp
    )
    
    // Tab Shape
    val Tab = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    
    // Search Bar Shape
    val SearchBar = RoundedCornerShape(24.dp)
    val SearchBarFull = RoundedCornerShape(50) // Fully rounded
    
    // FAB (Floating Action Button) Shapes
    val FabSmall = RoundedCornerShape(12.dp)
    val FabMedium = RoundedCornerShape(16.dp)
    val FabLarge = RoundedCornerShape(20.dp)
    val FabCircle = CircleShape
    
    // Swipe Action Button Shapes
    val SwipeButton = CircleShape
    val SwipeButtonSquare = RoundedCornerShape(16.dp)
    
    // Image Gallery Shapes
    val GalleryThumbnail = RoundedCornerShape(8.dp)
    val GalleryPreview = RoundedCornerShape(12.dp)
    val GalleryFull = RoundedCornerShape(0.dp)
    
    // Tag/Label Shapes
    val Tag = RoundedCornerShape(6.dp)
    val Label = RoundedCornerShape(50) // Pill shape
    
    // Navigation Bar Shape
    val NavigationBar = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    
    // Top App Bar Shape
    val TopAppBar = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    
    // Segmented Button Shapes
    val SegmentedButtonLeft = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 0.dp,
        bottomStart = 12.dp,
        bottomEnd = 0.dp
    )
    val SegmentedButtonMiddle = RoundedCornerShape(0.dp)
    val SegmentedButtonRight = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 12.dp,
        bottomStart = 0.dp,
        bottomEnd = 12.dp
    )
    
    // Progress Bar Shape
    val ProgressBar = RoundedCornerShape(50) // Fully rounded
    val ProgressBarSquare = RoundedCornerShape(4.dp)
    
    // Divider with Rounded Ends
    val DividerRounded = RoundedCornerShape(50)
    
    // Premium Badge Shape
    val PremiumBadge = RoundedCornerShape(6.dp)
    val PremiumBadgePill = RoundedCornerShape(50)
    
    // Notification Dot
    val NotificationDot = CircleShape
    
    // Switch Track Shape
    val SwitchTrack = RoundedCornerShape(50)
    
    // Slider Track Shape
    val SliderTrack = RoundedCornerShape(50)
    
    // Tooltip Shape
    val Tooltip = RoundedCornerShape(8.dp)
    
    // Menu Shape
    val Menu = RoundedCornerShape(12.dp)
    
    // Snackbar Shape
    val Snackbar = RoundedCornerShape(8.dp)
    
    // Banner Shape
    val Banner = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
}

/**
 * Shape Sizes for Consistency
 */
object ShapeSize {
    val None = 0.dp
    val ExtraSmall = 4.dp
    val Small = 8.dp
    val Medium = 12.dp
    val Large = 16.dp
    val ExtraLarge = 28.dp
    val Huge = 32.dp
}
