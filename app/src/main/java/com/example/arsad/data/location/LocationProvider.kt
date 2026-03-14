package com.example.arsad.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

class LocationProvider(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(lang: String): LocationResult {
        val location = withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    continuation.resume(loc)
                }.addOnFailureListener {
                    continuation.resume(null)
                }
            }
        }
        if (location == null) return LocationResult.Failure
        val name = resolveLocationName(location.latitude, location.longitude, lang)
        return LocationResult.Success(location.latitude, location.longitude, name)
    }

    suspend fun getCoordinatesFromName(name: String, currentLang: String): LocationResult {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale(currentLang))
                val addresses = geocoder.getFromLocationName(name, 1)
                val address = addresses?.firstOrNull()

                if (address != null) {
                    val city = address.locality ?: address.adminArea ?: ""
                    val country = address.countryName ?: ""
                    val displayName = if (city.isNotEmpty() && country.isNotEmpty()) {
                        "$city, $country"
                    } else {
                        city.ifEmpty { country }.ifEmpty { name }
                    }
                    LocationResult.Success(
                        lat = address.latitude,
                        lon = address.longitude,
                        name = displayName
                    )
                } else {
                    LocationResult.Failure
                }
            } catch (e: Exception) {
                LocationResult.Failure
            }
        }
    }

    suspend fun resolveLocationName(lat: Double, lon: Double, lang: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale(lang))
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val address = addresses?.firstOrNull()

                if (address != null) {
                    val city = address.locality ?: address.adminArea ?: ""
                    val country = address.countryName ?: ""

                    if (city.isNotEmpty() && country.isNotEmpty()) {
                        "$city, $country"
                    } else {
                        city.ifEmpty { country }.ifEmpty { "Unknown Location" }
                    }
                } else {
                    "Unknown Location"
                }
            } catch (e: Exception) {
                "Unknown Location"
            }
        }
    }
}

sealed class LocationResult {
    data class Success(val lat: Double, val lon: Double, val name: String) : LocationResult()
    object Failure : LocationResult()
}