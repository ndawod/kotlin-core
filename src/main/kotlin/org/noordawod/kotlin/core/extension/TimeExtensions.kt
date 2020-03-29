/*
 * The MIT License
 *
 * Copyright 2020 Noor Dawod. All rights reserved.
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

@file:Suppress("unused", "TooManyFunctions")

package org.noordawod.kotlin.core.extension

import kotlin.math.absoluteValue

/**
 * How many milliseconds in one second.
 */
const val MILLIS_IN_1_SECOND: Long = 1_000L

/**
 * How many seconds in one hour.
 */
const val SECONDS_IN_1_HOUR: Long = 3_600L

/**
 * How many seconds in one day.
 */
const val SECONDS_IN_1_DAY: Long = 86_400L

/**
 * Converts an optional [Int] into a [java.util.Date] on success, null otherwise.
 */
fun Int?.toDate(): java.util.Date? =
  if (null == this) null else java.util.Date(this * MILLIS_IN_1_SECOND)

/**
 * Converts an optional [Int] into a [java.util.Date] on success, current time otherwise.
 */
fun Int?.toDateOr(fallback: java.util.Date = java.util.Date()): java.util.Date =
  if (null == this) fallback else java.util.Date(this * MILLIS_IN_1_SECOND)

/**
 * Converts an optional [Long] into a [java.util.Date] on success, null otherwise.
 */
fun Long?.toDate(): java.util.Date? =
  if (null == this) null else java.util.Date(this)

/**
 * Converts an optional [Long] into a [java.util.Date] on success, current time otherwise.
 */
fun Long?.toDateOr(fallback: java.util.Date = java.util.Date()): java.util.Date =
  if (null == this) fallback else java.util.Date(this)

/**
 * Converts an optional [java.util.Date] into an [Int] representing the seconds that passed
 * since UNIX epoch on success, null otherwise.
 */
fun java.util.Date?.epoch(): Int? =
  if (null == this) null else (this.time / MILLIS_IN_1_SECOND).toInt()

/**
 * Converts an optional [java.util.Date] into an [Int] representing the seconds that passed
 * since UNIX epoch on success, current time otherwise.
 */
fun java.util.Date?.epochOr(fallback: java.util.Date = java.util.Date()): Int =
  ((this ?: fallback).time / MILLIS_IN_1_SECOND).toInt()

/**
 * Adds the amount of [millis] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusMillis(millis: Long): java.util.Date =
  java.util.Date(this.time + millis.absoluteValue)

/**
 * Adds the amount of [millis] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusMillis(millis: Int): java.util.Date = plusMillis(millis.toLong())

/**
 * Adds the amount of [seconds] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusSeconds(seconds: Long): java.util.Date =
  plusMillis(seconds * MILLIS_IN_1_SECOND)

/**
 * Adds the amount of [seconds] to this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.plusSeconds(seconds: Int): java.util.Date = plusSeconds(seconds.toLong())

/**
 * Subtracts the amount of [millis] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusMillis(millis: Long): java.util.Date =
  java.util.Date(this.time - millis.absoluteValue)

/**
 * Subtracts the amount of [millis] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusMillis(millis: Int): java.util.Date = minusMillis(millis.toLong())

/**
 * Subtracts the amount of [seconds] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusSeconds(seconds: Long): java.util.Date =
  minusMillis(seconds * MILLIS_IN_1_SECOND)

/**
 * Subtracts the amount of [seconds] from this [java.util.Date] instance, and returns it.
 */
fun java.util.Date.minusSeconds(seconds: Int): java.util.Date = minusSeconds(seconds.toLong())
