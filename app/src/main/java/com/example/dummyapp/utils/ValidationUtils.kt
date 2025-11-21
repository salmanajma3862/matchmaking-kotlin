package com.example.dummyapp.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Validation Result
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

/**
 * Validation Utilities
 * Helper functions for input validation
 */
object ValidationUtils {
    
    /**
     * Validate email address
     */
    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
    
    /**
     * Validate phone number (Pakistani format)
     */
    fun isValidPhone(phone: String): Boolean {
        // Remove spaces and dashes
        val cleanPhone = phone.replace(Regex("[-\\s]"), "")
        
        // Check if starts with +92 or 03 and has correct length
        return when {
            cleanPhone.startsWith("+92") -> cleanPhone.length == 13
            cleanPhone.startsWith("92") -> cleanPhone.length == 12
            cleanPhone.startsWith("03") -> cleanPhone.length == 11
            else -> false
        }
    }
    
    /**
     * Validate password strength
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= Constants.Validation.MIN_PASSWORD_LENGTH
    }
    
    /**
     * Validate password with detailed result
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Password is required")
            password.length < Constants.Validation.MIN_PASSWORD_LENGTH -> 
                ValidationResult(false, "Password must be at least ${Constants.Validation.MIN_PASSWORD_LENGTH} characters")
            !password.any { it.isDigit() } -> 
                ValidationResult(false, "Password must contain at least one number")
            !password.any { it.isUpperCase() } -> 
                ValidationResult(false, "Password must contain at least one uppercase letter")
            !password.any { it.isLowerCase() } -> 
                ValidationResult(false, "Password must contain at least one lowercase letter")
            !password.any { !it.isLetterOrDigit() } -> 
                ValidationResult(false, "Password must contain at least one special character")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Validate name with detailed result
     */
    fun validateName(name: String): ValidationResult {
        return when {
            name.isEmpty() -> ValidationResult(false, "Name is required")
            name.length < Constants.Validation.MIN_NAME_LENGTH -> 
                ValidationResult(false, "Name must be at least ${Constants.Validation.MIN_NAME_LENGTH} characters")
            name.length > Constants.Validation.MAX_NAME_LENGTH -> 
                ValidationResult(false, "Name must be less than ${Constants.Validation.MAX_NAME_LENGTH} characters")
            !name.matches(Regex("^[a-zA-Z\\s]+$")) -> 
                ValidationResult(false, "Name can only contain letters and spaces")
            else -> ValidationResult(true)
        }
    }
    
    /**
     * Validate age from date string (DD/MM/YYYY)
     */
    fun validateAge(dateOfBirth: String): ValidationResult {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            sdf.isLenient = false
            val date = sdf.parse(dateOfBirth) ?: return ValidationResult(false, "Invalid date format")
            
            val dobCalendar = Calendar.getInstance().apply { time = date }
            val today = Calendar.getInstance()
            
            var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            
            when {
                age < Constants.Validation.MIN_AGE -> 
                    ValidationResult(false, "You must be at least ${Constants.Validation.MIN_AGE} years old")
                age > Constants.Validation.MAX_AGE -> 
                    ValidationResult(false, "Age cannot exceed ${Constants.Validation.MAX_AGE} years")
                else -> ValidationResult(true)
            }
        } catch (e: Exception) {
            ValidationResult(false, "Invalid date format. Use DD/MM/YYYY")
        }
    }
    
    /**
     * Get password strength message
     */
    fun getPasswordStrengthMessage(password: String): String {
        return when {
            password.isEmpty() -> ""
            password.length < Constants.Validation.MIN_PASSWORD_LENGTH -> 
                "Password must be at least ${Constants.Validation.MIN_PASSWORD_LENGTH} characters"
            !password.any { it.isDigit() } -> 
                "Password should contain at least one number"
            !password.any { it.isUpperCase() } -> 
                "Password should contain at least one uppercase letter"
            else -> "Strong password"
        }
    }
    
    /**
     * Validate name
     */
    fun isValidName(name: String): Boolean {
        return name.length >= Constants.Validation.MIN_NAME_LENGTH &&
                name.length <= Constants.Validation.MAX_NAME_LENGTH
    }
    
    /**
     * Validate age from date of birth
     */
    fun isValidAge(dobTimestamp: Long): Boolean {
        val ageInMillis = System.currentTimeMillis() - dobTimestamp
        val ageInYears = ageInMillis / (365.25 * 24 * 60 * 60 * 1000)
        return ageInYears >= Constants.Validation.MIN_AGE && 
                ageInYears <= Constants.Validation.MAX_AGE
    }
    
    /**
     * Format phone number for display
     */
    fun formatPhoneNumber(phone: String): String {
        val cleanPhone = phone.replace(Regex("[-\\s]"), "")
        
        return when {
            cleanPhone.startsWith("+92") -> {
                val number = cleanPhone.substring(3)
                "+92 ${number.substring(0, 3)} ${number.substring(3)}"
            }
            cleanPhone.startsWith("03") -> {
                "${cleanPhone.substring(0, 4)} ${cleanPhone.substring(4)}"
            }
            else -> phone
        }
    }
    
    /**
     * Normalize phone number for API
     */
    fun normalizePhoneNumber(phone: String): String {
        val cleanPhone = phone.replace(Regex("[-\\s]"), "")
        
        return when {
            cleanPhone.startsWith("+92") -> cleanPhone
            cleanPhone.startsWith("92") -> "+$cleanPhone"
            cleanPhone.startsWith("03") -> "+92${cleanPhone.substring(1)}"
            else -> cleanPhone
        }
    }
}
