package com.salmanajmal.ziya.ui.screens.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PremiumPackagesScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val pinkGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB)) // gray-50
            .verticalScroll(scrollState)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(pinkGradient)
                .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Upgrade to Premium",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Crown Icon Placeholder (Using Star for now as Crown isn't standard)
                Icon(
                    imageVector = Icons.Default.Star, // Replace with Crown if available or drawable
                    contentDescription = "Premium",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(64.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Unlock all premium features and find your perfect match faster",
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 300.dp)
                )
            }
        }

        // Packages
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Choose Your Plan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827), // gray-900
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Weekly Package
            PackageCard(
                title = "Weekly",
                duration = "7 days access",
                price = "900",
                features = listOf(
                    "30 swipes per day",
                    "See who liked you",
                    "See who viewed your profile",
                    "Unlimited text messages",
                    "Ad-free experience"
                ),
                onSelect = { /* Handle selection */ }
            )

            // Monthly Package - Popular
            PackageCard(
                title = "Monthly",
                duration = "30 days access",
                price = "1,600",
                pricePerWeek = "PKR 400/week - Save 56%",
                isPopular = true,
                features = listOf(
                    "100 swipes per day",
                    "See who liked you",
                    "See who viewed your profile",
                    "Unlimited text and voice messages",
                    "Advanced filters",
                    "AI compatibility search feature"
                ),
                onSelect = { /* Handle selection */ }
            )

            // 3 Months Package
            PackageCard(
                title = "3 Months",
                duration = "90 days access",
                price = "4,200",
                pricePerWeek = "PKR 350/week - Save 61%",
                features = listOf(
                    "All other features and unlimited swipes",
                    "Text, voice and photo messages inside chat",
                    "Advanced filters",
                    "Priority profile visibility",
                    "AI compatibility search feature"
                ),
                onSelect = { /* Handle selection */ }
            )
        }

        // Footer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cancel anytime • Safe & Secure Payment",
                fontSize = 12.sp,
                color = Color(0xFF6B7280) // gray-500
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "By purchasing, you agree to our Terms of Service",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
fun PackageCard(
    title: String,
    duration: String,
    price: String,
    pricePerWeek: String? = null,
    isPopular: Boolean = false,
    features: List<String>,
    onSelect: () -> Unit
) {
    val pinkColor = Color(0xFFEC4899)
    val pinkGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .then(
                if (isPopular) Modifier.border(2.dp, pinkColor, RoundedCornerShape(16.dp))
                else Modifier.border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Header Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = duration,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "PKR",
                        fontSize = 16.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(bottom = 4.dp, end = 4.dp)
                    )
                    Text(
                        text = price,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }

                if (pricePerWeek != null) {
                    Text(
                        text = pricePerWeek,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Features
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = pinkColor,
                            modifier = Modifier.size(20.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = feature,
                            fontSize = 14.sp,
                            color = Color(0xFF374151) // gray-700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Button
            if (isPopular) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(pinkGradient)
                        .clickable(onClick = onSelect)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Select Plan",
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            } else {
                Button(
                    onClick = onSelect,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF3F4F6), // gray-100
                        contentColor = Color(0xFF111827)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = "Select Plan",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Popular Badge
        if (isPopular) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-12).dp)
                    .background(pinkGradient, CircleShape)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Most Popular",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
