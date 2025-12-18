package com.salmanajmal.ziya.data.models

/**
 * Feed Filters Model
 * Holds filter criteria for the feed/discovery screen
 */
data class FeedFilters(
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val city: String? = null,
    val religion: String? = null,
    val maritalStatus: List<String>? = null,
    val education: String? = null,
    val minHeight: Int? = null,
    val maxHeight: Int? = null,
    val smoking: Boolean? = null,
    val drinking: Boolean? = null,
    val hasPhoto: Boolean? = true
) {
    /**
     * Check if any filter is active
     */
    fun hasActiveFilters(): Boolean {
        return minAge != null || 
               maxAge != null || 
               !city.isNullOrBlank() || 
               !religion.isNullOrBlank() || 
               !maritalStatus.isNullOrEmpty() || 
               !education.isNullOrBlank() ||
               minHeight != null ||
               maxHeight != null ||
               smoking != null ||
               drinking != null
    }
    
    /**
     * Count number of active filters
     */
    fun activeFilterCount(): Int {
        var count = 0
        if (minAge != null || maxAge != null) count++ // Age range counts as 1
        if (!city.isNullOrBlank()) count++
        if (!religion.isNullOrBlank()) count++
        if (!maritalStatus.isNullOrEmpty()) count++
        if (!education.isNullOrBlank()) count++
        if (minHeight != null || maxHeight != null) count++ // Height range counts as 1
        if (smoking != null) count++
        if (drinking != null) count++
        return count
    }
    
    companion object {
        val DEFAULT = FeedFilters()
        
        // Age range constants
        const val MIN_AGE_DEFAULT = 18
        const val MAX_AGE_DEFAULT = 60
        
        // Height range constants (in cm)
        const val MIN_HEIGHT_DEFAULT = 140
        const val MAX_HEIGHT_DEFAULT = 210
        
        // Religion options
        val RELIGIONS = listOf(
            "Islam",
            "Christianity", 
            "Hinduism",
            "Sikhism",
            "Buddhism",
            "Judaism",
            "Other"
        )
        
        // Marital status options
        val MARITAL_STATUSES = listOf(
            "single" to "Single",
            "divorced" to "Divorced",
            "widowed" to "Widowed",
            "separated" to "Separated"
        )
        
        // Education options
        val EDUCATION_LEVELS = listOf(
            "high_school" to "High School",
            "bachelors" to "Bachelor's Degree",
            "masters" to "Master's Degree",
            "doctorate" to "Doctorate/PhD",
            "diploma" to "Diploma",
            "vocational" to "Vocational Training",
            "other" to "Other"
        )
    }
}
