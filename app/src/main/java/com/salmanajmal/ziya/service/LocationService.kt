package com.salmanajmal.ziya.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Result of a location fetch operation
 */
sealed class LocationFetchResult {
    data class Success(
        val city: String,
        val country: String,
        val latitude: Double,
        val longitude: Double
    ) : LocationFetchResult()
    
    data class Error(val message: String) : LocationFetchResult()
}

/**
 * OpenCage API response models
 */
data class OpenCageResponse(
    @SerializedName("results") val results: List<OpenCageResult>?,
    @SerializedName("status") val status: OpenCageStatus?
)

data class OpenCageResult(
    @SerializedName("components") val components: OpenCageComponents?,
    @SerializedName("formatted") val formatted: String?
)

data class OpenCageComponents(
    @SerializedName("city") val city: String?,
    @SerializedName("town") val town: String?,
    @SerializedName("village") val village: String?,
    @SerializedName("municipality") val municipality: String?,
    @SerializedName("county") val county: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?
)

data class OpenCageStatus(
    @SerializedName("code") val code: Int?,
    @SerializedName("message") val message: String?
)

/**
 * Service for fetching user's location and reverse geocoding to city/country
 * using OpenCage Geocoding API
 */
@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    private val httpClient = OkHttpClient()
    private val gson = Gson()
    
    companion object {
        // Replace with your OpenCage API key
        private const val OPENCAGE_API_KEY = "4bdacb8d75c3421a8a5b232234a4953c"
        private const val OPENCAGE_BASE_URL = "https://api.opencagedata.com/geocode/v1/json"
        private const val LOCATION_TIMEOUT_MS = 10000L // 10 seconds timeout
    }
    
    /**
     * Check if location permission is granted
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Get current location and reverse geocode to city/country
     * Times out after 10 seconds if location cannot be obtained
     */
    suspend fun getCurrentLocation(): LocationFetchResult {
        if (!hasLocationPermission()) {
            return LocationFetchResult.Error("Location permission not granted")
        }
        
        return try {
            // Try to get last known location first (fast)
            val lastKnown = getLastKnownLocation()
            
            val location = if (lastKnown != null) {
                lastKnown
            } else {
                // Request fresh location with timeout
                withTimeoutOrNull(LOCATION_TIMEOUT_MS) {
                    requestFreshLocation()
                }
            }
            
            if (location == null) {
                return LocationFetchResult.Error("Unable to get location. Please enable GPS and try again.")
            }
            
            // Reverse geocode to get city and country
            reverseGeocode(location.latitude, location.longitude)
        } catch (e: SecurityException) {
            LocationFetchResult.Error("Location permission denied")
        } catch (e: Exception) {
            LocationFetchResult.Error("Failed to get location: ${e.message}")
        }
    }
    
    @SuppressWarnings("MissingPermission")
    private suspend fun getLastKnownLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    continuation.resume(location)
                }
                .addOnFailureListener { exception ->
                    continuation.resume(null)
                }
        }
    }
    
    @SuppressWarnings("MissingPermission")
    private suspend fun requestFreshLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000L
            ).setMaxUpdates(1).build()
            
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)
                    continuation.resume(result.lastLocation)
                }
            }
            
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            
            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
    
    /**
     * Reverse geocode coordinates to city and country using OpenCage API
     */
    private suspend fun reverseGeocode(latitude: Double, longitude: Double): LocationFetchResult {
        return withContext(Dispatchers.IO) {
            try {
                val url = "$OPENCAGE_BASE_URL?q=$latitude+$longitude&key=$OPENCAGE_API_KEY&language=en&pretty=1"
                
                val request = Request.Builder()
                    .url(url)
                    .build()
                
                val response = httpClient.newCall(request).execute()
                
                if (!response.isSuccessful) {
                    return@withContext LocationFetchResult.Error("Geocoding failed: ${response.code}")
                }
                
                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty()) {
                    return@withContext LocationFetchResult.Error("Empty response from geocoding service")
                }
                
                val openCageResponse = gson.fromJson(responseBody, OpenCageResponse::class.java)
                
                if (openCageResponse.results.isNullOrEmpty()) {
                    return@withContext LocationFetchResult.Error("No results found for this location")
                }
                
                val components = openCageResponse.results.first().components
                
                // Try to get the most specific city name available
                val rawCity = components?.city 
                    ?: components?.town 
                    ?: components?.village 
                    ?: components?.municipality
                    ?: components?.county
                    ?: components?.state
                    ?: "Unknown"
                
                // Clean up city name by removing common suffixes
                val city = cleanupCityName(rawCity)
                
                val country = components?.country ?: "Unknown"
                
                LocationFetchResult.Success(
                    city = city,
                    country = country,
                    latitude = latitude,
                    longitude = longitude
                )
            } catch (e: Exception) {
                LocationFetchResult.Error("Geocoding error: ${e.message}")
            }
        }
    }
    
    /**
     * Clean up city name by removing common administrative suffixes
     * e.g., "Lodhran Tehsil" → "Lodhran"
     */
    private fun cleanupCityName(rawCity: String): String {
        // Common suffixes to remove (case insensitive)
        val suffixPattern = Regex(
            "\\s*(tehsil|district|division|city|town|municipality|county|province|region|sub-division|subdivision)\\s*$",
            RegexOption.IGNORE_CASE
        )
        
        val cleaned = rawCity.replace(suffixPattern, "").trim()
        
        // Capitalize first letter of each word
        return cleaned.split(" ")
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercase() }
            }
    }
}
