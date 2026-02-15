package com.example.sismosargentina.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Earthquake(
    val id: String,
    val magnitude: Double,
    val place: String,
    val latitude: Double,
    val longitude: Double,
    val depth: Double,
    val time: Long,
    val url: String?
) {
    fun getFormattedTime(): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }

    fun getFormattedDepth(): String {
        return "%.1f km".format(depth)
    }

    fun getFormattedMagnitude(): String {
        return "%.1f".format(magnitude)
    }

    companion object {
        fun fromFeature(feature: EarthquakeFeature): Earthquake? {
            val props = feature.properties
            val coords = feature.geometry.coordinates
            
            if (coords.size < 3 || props.magnitude == null) {
                return null
            }

            return Earthquake(
                id = feature.id,
                magnitude = props.magnitude,
                place = props.place ?: "Ubicación desconocida",
                longitude = coords[0],
                latitude = coords[1],
                depth = coords[2],
                time = props.time,
                url = props.url
            )
        }
    }
}
