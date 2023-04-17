/*
 * The MIT License
 *
 * Copyright 2022 Noor Dawod. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

@file:Suppress("unused")

package org.noordawod.kotlin.core.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Represents a geographical location on earth using a[latitude] and [longitude].
 *
 * @param latitude geographical latitude location
 * @param longitude geographical longitude location
 */
@Suppress("MemberVisibilityCanBePrivate")
class GeoPoint(val latitude: Double, val longitude: Double) {
  /**
   * A constructor to instantiate a new [GeoPoint] from String [latitude] and [longitude].
   *
   * @param latitude geographical latitude location
   * @param longitude geographical longitude location
   */
  constructor(latitude: String, longitude: String) :
    this(latitude.toDouble(), longitude.toDouble())

  init {
    check(isValid(latitude, longitude)) {
      "Geographical latitude/longitude value is invalid: $latitude/$longitude"
    }
  }

  /**
   * Returns the distance, in meters, from this geographical location to the [geoPoint] using
   * the Vincenty formula. If [geoPoint] is null, -1 is returned.
   *
   * @param geoPoint geo point to calculate distance to
   */
  fun distanceTo(geoPoint: GeoPoint?): Int {
    // If destination point is null, return -1.
    if (null == geoPoint) {
      return -1
    }

    // Quick and dirty.
    if (equals(geoPoint)) {
      return 0
    }

    val fromRadianLatitude = deg2rad(latitude)
    val sinFromRadianLatitude = sin(fromRadianLatitude)
    val cosFromRadianLatitude = cos(fromRadianLatitude)
    val fromRadianLongitude = deg2rad(longitude)
    val toRadianLatitude = deg2rad(geoPoint.latitude)
    val sinToRadianLatitude = sin(toRadianLatitude)
    val cosToRadianLatitude = cos(toRadianLatitude)
    val toRadianLongitude = deg2rad(geoPoint.longitude)

    val deltaRadianLatitude = toRadianLongitude - fromRadianLongitude
    val sinDeltaRadianLatitude = sin(deltaRadianLatitude)
    val cosDeltaRadianLatitude = cos(deltaRadianLatitude)

    val val1 = cosFromRadianLatitude * sinToRadianLatitude
    val val2 = cosToRadianLatitude * cosDeltaRadianLatitude
    val val3 = sinFromRadianLatitude * val2
    val val4 = cosToRadianLatitude * sinDeltaRadianLatitude
    val val5 = val4.pow(2.0) + (val1 - val3).pow(2.0)
    val val6 = sinFromRadianLatitude * sinToRadianLatitude + cosFromRadianLatitude * val2

    return (atan2(sqrt(val5), val6) * EARTH_RADIUS).toInt()
  }

  override fun toString(): String = toString(' ')

  /**
   * Generates a human-readable representation of this
   * [geographical location][GeoPoint], where the latitude and longitude
   * are separated by the provided [character][separator].
   *
   * If [highAccuracy] is true, then the returned number representation may contain up to 17
   * digits after the floating point. Otherwise, it will only contain up to 7 digits after the
   * floating point.
   */
  fun toString(separator: Char, highAccuracy: Boolean = false): String {
    val format = if (highAccuracy) "%.17f" else "%.7f"
    val latitudeValue = String.format(java.util.Locale.ENGLISH, format, latitude)
    val longitudeValue = String.format(java.util.Locale.ENGLISH, format, longitude)
    return "$latitudeValue$separator$longitudeValue"
  }

  override fun hashCode(): Int =
    java.lang.Double.hashCode(latitude) + java.lang.Double.hashCode(longitude)

  override fun equals(other: Any?): Boolean =
    other is GeoPoint && latitude == other.latitude && longitude == other.longitude

  companion object {
    /**
     * A default and unrealistic geographic location to serve as "null".
     */
    val FALLBACK = GeoPoint(-47.47474747, -8.88888888)

    /**
     * Minimal value for a geographical latitude location.
     */
    const val LATITUDE_MIN: Double = -90.0

    /**
     * Maximal value for a geographical latitude location.
     */
    const val LATITUDE_MAX: Double = 90.0

    /**
     * Minimal value for a geographical longitude location.
     */
    const val LONGITUDE_MIN: Double = -180.0

    /**
     * Maximal value for a geographical longitude location.
     */
    const val LONGITUDE_MAX: Double = 180.0

    /**
     * How many meters in one degree.
     */
    const val DEGREE_TO_METER: Float = 111_045.0f

    /**
     * The value of PI divided by 180.
     */
    const val VAL_PI_OVER_180: Double = Math.PI / 180.0

    /**
     * The value of 180 divided by PI.
     */
    const val VAL_180_OVER_PI: Double = 180.0 / Math.PI

    /**
     * Earth's radius measured in meters.
     */
    const val EARTH_RADIUS: Int = 6_371_000

    /**
     * Converts degrees to radians.
     */
    fun deg2rad(deg: Double): Double = deg * VAL_PI_OVER_180

    /**
     * Converts radians to degrees.
     */
    fun rad2deg(rad: Double): Double = rad * VAL_180_OVER_PI

    /**
     * Returns true if the provided [longitude] falls between [LATITUDE_MIN] and [LATITUDE_MAX],
     * false otherwise.
     */
    fun isValidLatitude(latitude: Double): Boolean = latitude in LATITUDE_MIN..LATITUDE_MAX

    /**
     * Returns true if the provided [longitude] falls between [LONGITUDE_MIN] and [LONGITUDE_MAX],
     * false otherwise.
     */
    fun isValidLongitude(longitude: Double): Boolean = longitude in LONGITUDE_MIN..LONGITUDE_MAX

    /**
     * Returns true if the provided [longitude] and [longitude] are both valid, false otherwise.
     */
    fun isValid(latitude: Double, longitude: Double): Boolean =
      isValidLatitude(latitude) && isValidLongitude(longitude)

    /**
     * Returns a textual representation of [value] as formatted as floating-point number.
     *
     * If [highAccuracy] is true, then the returned number representation may contain up to 17
     * digits after the floating point. Otherwise, it will only contain up to 7 digits after the
     * floating point.
     *
     * @param value a value to represent textually
     * @param highAccuracy whether to produce a very high accurate representation, or not
     */
    @JvmOverloads
    fun toString(value: Double, highAccuracy: Boolean = false): String =
      String.format(java.util.Locale.ENGLISH, if (highAccuracy) "%.17f" else "%.7f", value)
  }
}

/**
 * Type of unit when referring to a [GeoPoint].
 */
enum class GeoUnit(private val value: String) {
  /**
   * The meter unit.
   */
  METER("meter"),

  /**
   * One kilometer is 1000 [meters][METER].
   */
  KILOMETER("km"),

  /**
   * One mile is approximately 1609.344f [meters][METER].
   */
  MILE("mile");

  override fun toString(): String = value

  /**
   * Converts the [distance] value from this [GeoUnit] to the [meter unit][METER].
   *
   * @param distance value to convert
   */
  fun toMeters(distance: Float): Float = when (this) {
    KILOMETER -> (distance * KILOMETER_TO_METERS).toFloat()
    MILE -> (distance * MILE_TO_METERS).toFloat()
    else -> distance
  }

  /**
   * Converts the [distance] value from this [GeoUnit] to the [kilometer unit][KILOMETER].
   *
   * @param distance value to convert
   */
  fun toKilometers(distance: Float): Float = when (this) {
    METER -> (distance / KILOMETER_TO_METERS).toFloat()
    MILE -> (distance / KILOMETER_TO_MILE).toFloat()
    else -> distance
  }

  /**
   * Converts the [distance] value from this [GeoUnit] to the [mile unit][MILE].
   *
   * @param distance value to convert
   */
  fun toMiles(distance: Float): Float = when (this) {
    METER -> (distance / MILE_TO_METERS).toFloat()
    KILOMETER -> (distance * KILOMETER_TO_MILE).toFloat()
    else -> distance
  }

  companion object {
    /**
     * How many [meters][METER] in one [mile][MILE].
     */
    private const val MILE_TO_METERS: Double = 1609.3399999755

    /**
     * How many [meters][METER] in one [kilometer][KILOMETER].
     */
    private const val KILOMETER_TO_METERS: Double = 1000.0

    /**
     * How many [miles][MILE] in one [kilometer][KILOMETER].
     */
    private const val KILOMETER_TO_MILE: Double = 0.621371

    /**
     * Tries to decode [value] to a [GeoPoint], returns null otherwise.
     *
     * @param value value to decode
     */
    fun decode(value: String?): GeoUnit? = value?.let { nonNullValue ->
      val lowerCaseValue = nonNullValue.lowercase(java.util.Locale.ENGLISH)
      values().firstOrNull { it.value == lowerCaseValue }
    }
  }
}

/**
 * Calculates the min/max latitude and longitude values for a circular geographical area that
 * surrounds a given [geographical location][geoPoint] having [radius] distance. Unit of
 * measurement for [radius] is [unit].
 *
 * The computed values are needed to calculate distance between two geographical locations
 * in SQL using the Spherical Cosine Law formula (known also as Haversine formula).
 *
 * @see <a href="https://tinyurl.com/y7xm7w65">initial implementation and idea</a>
 *
 * @param geoPoint center of the geographical location
 * @param radius radius of the circle surrounding [geoPoint]
 * @param unit which unit of measurement
 */
@Suppress("MemberVisibilityCanBePrivate")
data class GeoPointArea(
  val geoPoint: GeoPoint,
  val radius: Float,
  val unit: GeoUnit
) {
  /**
   * Calculated value for the minimal latitude of this [GeoPoint].
   */
  val latitudeMin: Double

  /**
   * Calculated value for the maximal latitude of this [GeoPoint].
   */
  val latitudeMax: Double

  /**
   * Calculated value for the minimal longitude of this [GeoPoint].
   */
  val longitudeMin: Double

  /**
   * Calculated value for the maximal longitude of this [GeoPoint].
   */
  val longitudeMax: Double

  init {
    val latitudeInRadians = Math.toRadians(geoPoint.latitude)
    val latitudeCos = cos(latitudeInRadians)

    // The delta is in degrees even if its value is deduced from calculations in meters.
    val deltaDegrees = unit.toMeters(radius) / GeoPoint.DEGREE_TO_METER

    // Rest of the values.
    val longitudeDelta = deltaDegrees * latitudeCos
    latitudeMin = geoPoint.latitude - deltaDegrees
    latitudeMax = geoPoint.latitude + deltaDegrees
    longitudeMin = geoPoint.longitude - longitudeDelta
    longitudeMax = geoPoint.longitude + longitudeDelta
  }
}
