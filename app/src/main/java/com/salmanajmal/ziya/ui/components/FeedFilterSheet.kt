package com.salmanajmal.ziya.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.salmanajmal.ziya.data.models.FeedFilters

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FeedFilterSheet(
    currentFilters: FeedFilters,
    onApply: (FeedFilters) -> Unit,
    onDismiss: () -> Unit
) {
    // Local state for editing filters
    var minAge by remember { mutableIntStateOf(currentFilters.minAge ?: FeedFilters.MIN_AGE_DEFAULT) }
    var maxAge by remember { mutableIntStateOf(currentFilters.maxAge ?: FeedFilters.MAX_AGE_DEFAULT) }
    var city by remember { mutableStateOf(currentFilters.city ?: "") }
    var religion by remember { mutableStateOf(currentFilters.religion ?: "") }
    var selectedMaritalStatuses by remember { mutableStateOf(currentFilters.maritalStatus ?: emptyList()) }
    var education by remember { mutableStateOf(currentFilters.education ?: "") }
    var minHeight by remember { mutableIntStateOf(currentFilters.minHeight ?: FeedFilters.MIN_HEIGHT_DEFAULT) }
    var maxHeight by remember { mutableIntStateOf(currentFilters.maxHeight ?: FeedFilters.MAX_HEIGHT_DEFAULT) }
    var smoking by remember { mutableStateOf(currentFilters.smoking) }
    var drinking by remember { mutableStateOf(currentFilters.drinking) }
    
    // Dropdown states
    var religionExpanded by remember { mutableStateOf(false) }
    var educationExpanded by remember { mutableStateOf(false) }
    
    val scrollState = rememberScrollState()
    
    // Colors
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceColor)
            .padding(horizontal = 20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = onSurfaceColor
                )
            }
            
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            TextButton(
                onClick = {
                    // Reset all filters
                    minAge = FeedFilters.MIN_AGE_DEFAULT
                    maxAge = FeedFilters.MAX_AGE_DEFAULT
                    city = ""
                    religion = ""
                    selectedMaritalStatuses = emptyList()
                    education = ""
                    minHeight = FeedFilters.MIN_HEIGHT_DEFAULT
                    maxHeight = FeedFilters.MAX_HEIGHT_DEFAULT
                    smoking = null
                    drinking = null
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset")
            }
        }
        
        HorizontalDivider()
        
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Age Range Section
            FilterSection(title = "Age Range") {
                Column {
                    RangeSlider(
                        value = minAge.toFloat()..maxAge.toFloat(),
                        onValueChange = { range ->
                            minAge = range.start.toInt()
                            maxAge = range.endInclusive.toInt()
                        },
                        valueRange = 18f..70f,
                        steps = 51,
                        colors = SliderDefaults.colors(
                            thumbColor = primaryColor,
                            activeTrackColor = primaryColor
                        )
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$minAge years",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceColor.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "$maxAge years",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            // Location Section
            FilterSection(title = "Location") {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    placeholder = { Text("Enter city name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
            
            // Religion Section
            FilterSection(title = "Religion") {
                ExposedDropdownMenuBox(
                    expanded = religionExpanded,
                    onExpandedChange = { religionExpanded = it }
                ) {
                    OutlinedTextField(
                        value = religion,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Religion") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = religionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    ExposedDropdownMenu(
                        expanded = religionExpanded,
                        onDismissRequest = { religionExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Any") },
                            onClick = {
                                religion = ""
                                religionExpanded = false
                            }
                        )
                        FeedFilters.RELIGIONS.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    religion = option
                                    religionExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            // Marital Status Section
            FilterSection(title = "Marital Status") {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeedFilters.MARITAL_STATUSES.forEach { (value, label) ->
                        val isSelected = selectedMaritalStatuses.contains(value)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedMaritalStatuses = if (isSelected) {
                                    selectedMaritalStatuses - value
                                } else {
                                    selectedMaritalStatuses + value
                                }
                            },
                            label = { Text(label) },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = primaryColor.copy(alpha = 0.2f),
                                selectedLabelColor = primaryColor
                            )
                        )
                    }
                }
            }
            
            // Education Section
            FilterSection(title = "Education") {
                ExposedDropdownMenuBox(
                    expanded = educationExpanded,
                    onExpandedChange = { educationExpanded = it }
                ) {
                    OutlinedTextField(
                        value = FeedFilters.EDUCATION_LEVELS.find { it.first == education }?.second ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Education Level") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = educationExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    ExposedDropdownMenu(
                        expanded = educationExpanded,
                        onDismissRequest = { educationExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Any") },
                            onClick = {
                                education = ""
                                educationExpanded = false
                            }
                        )
                        FeedFilters.EDUCATION_LEVELS.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    education = value
                                    educationExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            // Height Range Section
            FilterSection(title = "Height Range") {
                Column {
                    RangeSlider(
                        value = minHeight.toFloat()..maxHeight.toFloat(),
                        onValueChange = { range ->
                            minHeight = range.start.toInt()
                            maxHeight = range.endInclusive.toInt()
                        },
                        valueRange = 140f..210f,
                        steps = 69,
                        colors = SliderDefaults.colors(
                            thumbColor = primaryColor,
                            activeTrackColor = primaryColor
                        )
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${minHeight} cm",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceColor.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "${maxHeight} cm",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurfaceColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            // Lifestyle Section
            FilterSection(title = "Lifestyle") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Smoking preference
                    LifestyleToggle(
                        label = "Non-smoker only",
                        value = smoking == false,
                        onValueChange = { checked ->
                            smoking = if (checked) false else null
                        }
                    )
                    
                    // Drinking preference
                    LifestyleToggle(
                        label = "Non-drinker only",
                        value = drinking == false,
                        onValueChange = { checked ->
                            drinking = if (checked) false else null
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Apply Button
        Button(
            onClick = {
                val newFilters = FeedFilters(
                    minAge = if (minAge != FeedFilters.MIN_AGE_DEFAULT) minAge else null,
                    maxAge = if (maxAge != FeedFilters.MAX_AGE_DEFAULT) maxAge else null,
                    city = city.ifBlank { null },
                    religion = religion.ifBlank { null },
                    maritalStatus = selectedMaritalStatuses.ifEmpty { null },
                    education = education.ifBlank { null },
                    minHeight = if (minHeight != FeedFilters.MIN_HEIGHT_DEFAULT) minHeight else null,
                    maxHeight = if (maxHeight != FeedFilters.MAX_HEIGHT_DEFAULT) maxHeight else null,
                    smoking = smoking,
                    drinking = drinking
                )
                onApply(newFilters)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor
            )
        ) {
            Text(
                text = "Apply Filters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        content()
    }
}

@Composable
private fun LifestyleToggle(
    label: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onValueChange(!value) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun ActiveFilterChips(
    filters: FeedFilters,
    onClearFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!filters.hasActiveFilters()) return
    
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Age filter chip
        if (filters.minAge != null || filters.maxAge != null) {
            val minAge = filters.minAge ?: FeedFilters.MIN_AGE_DEFAULT
            val maxAge = filters.maxAge ?: FeedFilters.MAX_AGE_DEFAULT
            ActiveChip(
                text = "$minAge-$maxAge yrs",
                onClose = { onClearFilter("age") }
            )
        }
        
        // City filter chip
        filters.city?.let { city ->
            ActiveChip(
                text = city,
                onClose = { onClearFilter("city") }
            )
        }
        
        // Religion filter chip
        filters.religion?.let { religion ->
            ActiveChip(
                text = religion,
                onClose = { onClearFilter("religion") }
            )
        }
        
        // Marital status filter chip
        filters.maritalStatus?.takeIf { it.isNotEmpty() }?.let { statuses ->
            val label = if (statuses.size == 1) {
                FeedFilters.MARITAL_STATUSES.find { it.first == statuses[0] }?.second ?: statuses[0]
            } else {
                "${statuses.size} statuses"
            }
            ActiveChip(
                text = label,
                onClose = { onClearFilter("maritalStatus") }
            )
        }
        
        // Education filter chip
        filters.education?.let { edu ->
            val label = FeedFilters.EDUCATION_LEVELS.find { it.first == edu }?.second ?: edu
            ActiveChip(
                text = label,
                onClose = { onClearFilter("education") }
            )
        }
        
        // Height filter chip
        if (filters.minHeight != null || filters.maxHeight != null) {
            val minHeight = filters.minHeight ?: FeedFilters.MIN_HEIGHT_DEFAULT
            val maxHeight = filters.maxHeight ?: FeedFilters.MAX_HEIGHT_DEFAULT
            ActiveChip(
                text = "$minHeight-$maxHeight cm",
                onClose = { onClearFilter("height") }
            )
        }
        
        // Lifestyle filter chips
        if (filters.smoking == false) {
            ActiveChip(
                text = "Non-smoker",
                onClose = { onClearFilter("smoking") }
            )
        }
        
        if (filters.drinking == false) {
            ActiveChip(
                text = "Non-drinker",
                onClose = { onClearFilter("drinking") }
            )
        }
    }
}

@Composable
private fun ActiveChip(
    text: String,
    onClose: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove filter",
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
