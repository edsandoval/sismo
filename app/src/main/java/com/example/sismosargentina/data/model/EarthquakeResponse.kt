package com.example.sismosargentina.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EarthquakeResponse(
    @Json(name = "type")
    val type: String,
    @Json(name = "features")
    val features: List<EarthquakeFeature>,
    @Json(name = "metadata")
    val metadata: Metadata
)

@JsonClass(generateAdapter = true)
data class EarthquakeFeature(
    @Json(name = "type")
    val type: String,
    @Json(name = "properties")
    val properties: EarthquakeProperties,
    @Json(name = "geometry")
    val geometry: Geometry,
    @Json(name = "id")
    val id: String
)

@JsonClass(generateAdapter = true)
data class EarthquakeProperties(
    @Json(name = "mag")
    val magnitude: Double?,
    @Json(name = "place")
    val place: String?,
    @Json(name = "time")
    val time: Long,
    @Json(name = "updated")
    val updated: Long?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "detail")
    val detail: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "type")
    val type: String?
)

@JsonClass(generateAdapter = true)
data class Geometry(
    @Json(name = "type")
    val type: String,
    @Json(name = "coordinates")
    val coordinates: List<Double>
)

@JsonClass(generateAdapter = true)
data class Metadata(
    @Json(name = "generated")
    val generated: Long,
    @Json(name = "count")
    val count: Int,
    @Json(name = "status")
    val status: Int,
    @Json(name = "title")
    val title: String
)
