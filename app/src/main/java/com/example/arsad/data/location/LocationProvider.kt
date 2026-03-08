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
    suspend fun getCurrentLocation(): LocationResult {
        return withContext(Dispatchers.IO) {
            // suspendCancellableCoroutine is my way of converting the Callback-based API to a
            // Sequential suspend function that returns data and handles the Cancellation safely.
            suspendCancellableCoroutine { continuation ->
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val name = resolveLocationName(location.latitude, location.longitude)
                        continuation.resume(
                            LocationResult.Success(
                                location.latitude, location.longitude, name
                            )
                        )
                    } else {
                        continuation.resume(LocationResult.Failure)
                    }
                }.addOnFailureListener {
                    continuation.resume(LocationResult.Failure)
                }
            }
        }
    }

    suspend fun getCoordinatesFromName(name: String): LocationResult {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocationName(name, 1)
                val address = addresses?.firstOrNull()

                if (address != null) {
                    LocationResult.Success(
                        lat = address.latitude,
                        lon = address.longitude,
                        name = address.locality ?: address.adminArea ?: name
                    )
                } else {
                    LocationResult.Failure
                }
            } catch (e: Exception) {
                LocationResult.Failure
            }
        }
    }

    fun resolveLocationName(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            val address = addresses?.firstOrNull()

            val city = address?.locality ?: address?.subAdminArea ?: address?.adminArea
            val country = address?.countryName

            if (city != null && country != null) "$city, $country"
            else city ?: "%.4f, %.4f".format(lat, lon)
        } catch (e: Exception) {
            "%.4f, %.4f".format(lat, lon)
        }
    }
}

sealed class LocationResult {
    data class Success(val lat: Double, val lon: Double, val name: String) : LocationResult()
    object Failure : LocationResult()
}
