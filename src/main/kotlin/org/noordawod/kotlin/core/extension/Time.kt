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
@file:OptIn(ExperimentalContracts::class)

package org.noordawod.kotlin.core.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.absoluteValue

private val JAVA_UTIL_DURATION_REGEX: Regex = """([0-9]+)(\.[0-9]+)?([HMS])""".toRegex()

/**
 * How many hours in one day.
 */
const val HOURS_IN_1_DAY: Long = 24L

/**
 * How many minutes in one hour.
 */
const val MINUTES_IN_1_HOUR: Long = 60L

/**
 * How many seconds in one minute.
 */
const val SECONDS_IN_1_MINUTE: Long = 60L

/**
 * How many milliseconds in one second.
 */
const val MILLIS_IN_1_SECOND: Long = 1000L

/**
 * How many milliseconds in one minute.
 */
const val MILLIS_IN_1_MINUTE: Long = MILLIS_IN_1_SECOND * SECONDS_IN_1_MINUTE

/**
 * How many seconds in one hour.
 */
const val SECONDS_IN_1_HOUR: Long = SECONDS_IN_1_MINUTE * MINUTES_IN_1_HOUR

/**
 * How many milliseconds in one hour.
 */
const val MILLIS_IN_1_HOUR: Long = MILLIS_IN_1_SECOND * SECONDS_IN_1_HOUR

/**
 * How many seconds in one day.
 */
const val SECONDS_IN_1_DAY: Long = SECONDS_IN_1_HOUR * HOURS_IN_1_DAY

/**
 * How many milliseconds in one day.
 */
const val MILLIS_IN_1_DAY: Long = MILLIS_IN_1_SECOND * SECONDS_IN_1_DAY

/**
 * Converts an optional [Int] into a [java.util.Date] on success, null otherwise.
 */
fun Int?.toDate(): java.util.Date? {
  contract {
    returnsNotNull() implies (this@toDate != null)
  }

  return if (null == this) null else java.util.Date(this * MILLIS_IN_1_SECOND)
}

/**
 * Converts an optional [Int] into a [java.util.Date] on success, current time otherwise.
 */
fun Int?.toDateOr(fallback: java.util.Date = java.util.Date()): java.util.Date {
  contract {
    returnsNotNull() implies (this@toDateOr != null)
  }

  return if (null == this) fallback else java.util.Date(this * MILLIS_IN_1_SECOND)
}

/**
 * Converts an optional [Long] into a [java.util.Date] on success, null otherwise.
 */
fun Long?.toDate(): java.util.Date? {
  contract {
    returnsNotNull() implies (this@toDate != null)
  }

  return if (null == this) null else java.util.Date(this)
}

/**
 * Converts an optional [Long] into a [java.util.Date] on success, current time otherwise.
 */
fun Long?.toDateOr(fallback: java.util.Date = java.util.Date()): java.util.Date {
  contract {
    returnsNotNull() implies (this@toDateOr != null)
  }

  return if (null == this) fallback else java.util.Date(this)
}

/**
 * Returns a [java.util.Date] if this Date is non-null and [java.util.Date.getTime] is
 * positive, null otherwise.
 */
fun java.util.Date?.normalized(): java.util.Date? {
  contract {
    returnsNotNull() implies (this@normalized != null)
  }

  return if (null == this || 1L > time) null else this
}

/**
 * Converts this optional [java.util.Date] into a [Long] representing the milliseconds that
 * passed since UNIX epoch on success, null otherwise.
 */
fun java.util.Date?.millisecondsSinceEpoch(): Long? {
  contract {
    returnsNotNull() implies (this@millisecondsSinceEpoch != null)
  }

  return this?.time
}

/**
 * Converts this optional [java.util.Date], or [fallback] if null, into a [Long] representing
 * the milliseconds that passed since UNIX epoch on success.
 */
fun java.util.Date?.millisecondsSinceEpochOr(fallback: java.util.Date = java.util.Date()): Long {
  contract {
    returnsNotNull() implies (this@millisecondsSinceEpochOr != null)
  }

  return (this ?: fallback).time
}

/**
 * Converts this optional [java.util.Date] into a [Long] representing the seconds that
 * passed since UNIX epoch on success, null otherwise.
 */
fun java.util.Date?.secondsSinceEpoch(): Int? {
  contract {
    returnsNotNull() implies (this@secondsSinceEpoch != null)
  }

  val millis = millisecondsSinceEpoch()

  return if (null == millis) null else (millis / MILLIS_IN_1_SECOND).toInt()
}

/**
 * Converts this optional [java.util.Date], or [fallback] if null, into a [Long] representing
 * the seconds that passed since UNIX epoch on success.
 */
fun java.util.Date?.secondsSinceEpochOr(fallback: java.util.Date = java.util.Date()): Int {
  contract {
    returnsNotNull() implies (this@secondsSinceEpochOr != null)
  }

  return (millisecondsSinceEpochOr(fallback) / MILLIS_IN_1_SECOND).toInt()
}

/**
 * Converts this optional [java.util.Date] into a [java.time.OffsetDateTime], null otherwise.
 */
fun java.util.Date?.offsetDateTime(): java.time.OffsetDateTime? {
  contract {
    returnsNotNull() implies (this@offsetDateTime != null)
  }

  return if (null == this) null else toInstant().atOffset(java.time.ZoneOffset.UTC)
}

/**
 * Converts this [java.util.Date], or [fallback] if null, into a [java.time.OffsetDateTime].
 */
fun java.util.Date?.offsetDateTimeOr(
  fallback: java.util.Date = java.util.Date(),
): java.time.OffsetDateTime {
  contract {
    returnsNotNull() implies (this@offsetDateTimeOr != null)
  }

  return (this ?: fallback)
    .toInstant()
    .atOffset(java.time.ZoneOffset.UTC)
}

/**
 * Converts this optional [java.util.Date] into a [java.time.OffsetDateTime], null otherwise.
 */
fun java.time.OffsetDateTime?.date(): java.util.Date? {
  contract {
    returnsNotNull() implies (this@date != null)
  }

  return if (null == this) null else java.util.Date(toInstant().toEpochMilli())
}

/**
 * Converts this [java.util.Date], or [fallback] if null, into a [java.time.OffsetDateTime].
 */
fun java.time.OffsetDateTime?.dateOr(
  fallback: java.time.OffsetDateTime = java.time.OffsetDateTime.now(),
): java.util.Date {
  contract {
    returnsNotNull() implies (this@dateOr != null)
  }

  return java.util.Date(
    (this ?: fallback)
      .toInstant()
      .toEpochMilli(),
  )
}

/**
 * Adds the amount of [millis] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusMillis(millis: Long): java.util.Date = java.util.Date(
  this.time + millis.absoluteValue,
)

/**
 * Adds the amount of [millis] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusMillis(millis: Int): java.util.Date = plusMillis(millis.toLong())

/**
 * Adds the amount of [seconds] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusSeconds(seconds: Long): java.util.Date = plusMillis(
  seconds * MILLIS_IN_1_SECOND,
)

/**
 * Adds the amount of [seconds] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusSeconds(seconds: Int): java.util.Date = plusSeconds(
  seconds.toLong(),
)

/**
 * Adds the amount of [minutes] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusMinutes(minutes: Long): java.util.Date = plusMillis(
  minutes * MILLIS_IN_1_MINUTE,
)

/**
 * Adds the amount of [minutes] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusMinutes(minutes: Int): java.util.Date = plusMinutes(
  minutes.toLong(),
)

/**
 * Adds the amount of [hours] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusHours(hours: Long): java.util.Date = plusMillis(
  hours * MILLIS_IN_1_HOUR,
)

/**
 * Adds the amount of [hours] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusHours(hours: Int): java.util.Date = plusHours(
  hours.toLong(),
)

/**
 * Adds the amount of [days] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusDays(days: Long): java.util.Date = plusMillis(
  days * MILLIS_IN_1_DAY,
)

/**
 * Adds the amount of [days] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusDays(days: Int): java.util.Date = plusDays(
  days.toLong(),
)

/**
 * Subtracts the amount of [millis] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusMillis(millis: Long): java.util.Date = java.util.Date(
  this.time - millis.absoluteValue,
)

/**
 * Subtracts the amount of [millis] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusMillis(millis: Int): java.util.Date = minusMillis(
  millis.toLong(),
)

/**
 * Subtracts the amount of [seconds] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusSeconds(seconds: Long): java.util.Date = minusMillis(
  seconds * MILLIS_IN_1_SECOND,
)

/**
 * Subtracts the amount of [seconds] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusSeconds(seconds: Int): java.util.Date = minusSeconds(
  seconds.toLong(),
)

/**
 * Subtracts the amount of [minutes] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusMinutes(minutes: Long): java.util.Date = minusMillis(
  minutes * MILLIS_IN_1_MINUTE,
)

/**
 * Subtracts the amount of [minutes] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusMinutes(minutes: Int): java.util.Date = minusMinutes(
  minutes.toLong(),
)

/**
 * Subtracts the amount of [hours] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusHours(hours: Long): java.util.Date = minusMillis(
  hours * MILLIS_IN_1_HOUR,
)

/**
 * Subtracts the amount of [hours] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusHours(hours: Int): java.util.Date = minusHours(
  hours.toLong(),
)

/**
 * Subtracts the amount of [days] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusDays(days: Long): java.util.Date = minusMillis(
  days * MILLIS_IN_1_DAY,
)

/**
 * Subtracts the amount of [days] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusDays(days: Int): java.util.Date = minusDays(
  days.toLong(),
)

/**
 * Returns a human-readable representation of this [Duration][java.time.Duration].
 */
fun java.time.Duration.humanReadable(): String = "$this"
  .substring(2)
  .replace(JAVA_UTIL_DURATION_REGEX, "$1$3 ")
  .lowercase()

/**
 * Returns a human-readable representation of this [Duration][kotlin.time.Duration].
 */
fun kotlin.time.Duration.humanReadable(): String =
  java.time.Duration.ofMillis(inWholeMilliseconds).humanReadable()

/**
 * Returns a [GregorianCalendar][java.util.GregorianCalendar] for this [Date][java.util.Date].
 */
fun java.util.Date.gregorianCalendar(): java.util.Calendar {
  val calendar: java.util.Calendar = java.util.GregorianCalendar()

  calendar.setTime(java.util.Date())

  return calendar
}
