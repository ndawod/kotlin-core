/*
 * The MIT License
 *
 * Copyright 2024 Noor Dawod. All rights reserved.
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

@file:Suppress("unused", "MagicNumber")

package org.noordawod.kotlin.core.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.roundToLong

/**
 * Returns the exact value of this [Float] as a [Double] without any side effects, such
 * as converting the floating points to some imaginary value.
 */
fun Float.toExactDouble(): Double = "$this".toDouble()

/**
 * Returns a [Float] number trimmed to a certain number of floating points.
 *
 * @param floatingPoints how many floating-point numbers to include, defaults to 2
 */
fun Float.withFloatingPoints(floatingPoints: Int = 2): Float {
  var multiplier = 1f
  for (idx in 1..floatingPoints) {
    multiplier *= 10f
  }

  return times(multiplier).roundToLong().div(multiplier)
}

/**
 * Returns a string representation of this [Float] and trim any floating-point numbers
 * if they're equal to 0.
 *
 * This is normally called after [withFloatingPoints].
 */
@Suppress("MagicNumber")
fun Float.trimIfZero(): String {
  val stringified = "$this"

  return if (stringified.endsWith(".0")) {
    stringified.substring(0, stringified.length - 2)
  } else {
    stringified
  }
}

/**
 * Ensures that a [Long] value is non-null and signifies an hour (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Long?.ensureHourOrNull(loggerFn: ((String) -> Unit)? = null): Long? {
  contract {
    returnsNotNull() implies (this@ensureHourOrNull != null)
  }

  return ensureRangeOrNull(
    start = 0L,
    end = 23L,
    loggerFn = loggerFn,
  )
}

/**
 * Ensures that a [Long] value is non-null and signifies an hour (0..23), throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Long?.ensureHourOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Long {
  contract {
    returnsNotNull() implies (this@ensureHourOrThrow != null)
  }

  return ensureHourOrNull(loggerFn) ?: throw errorFn()
}

/**
 * Ensures that an [Int] value is non-null and signifies an hour (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Int?.ensureHourOrNull(loggerFn: ((String) -> Unit)? = null): Int? =
  this?.toLong().ensureHourOrNull(loggerFn)?.toInt()

/**
 * Ensures that an [Int] value is non-null and signifies an hour (0..23), throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Int?.ensureHourOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Int = this?.toLong().ensureHourOrNull(loggerFn)?.toInt() ?: throw errorFn()

/**
 * Ensures that a [Short] value is non-null and signifies an hour (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Short?.ensureHourOrNull(loggerFn: ((String) -> Unit)? = null): Short? =
  this?.toLong().ensureHourOrNull(loggerFn)?.toShort()

/**
 * Ensures that a [Short] value is non-null and signifies an hour (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Short?.ensureHourOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Short = this?.toLong().ensureHourOrNull(loggerFn)?.toShort() ?: throw errorFn()

/**
 * Ensures that a [Long] value is non-null and signifies an minute (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Long?.ensureMinuteOrNull(loggerFn: ((String) -> Unit)? = null): Long? {
  contract {
    returnsNotNull() implies (this@ensureMinuteOrNull != null)
  }

  return ensureRangeOrNull(
    start = 0L,
    end = 23L,
    loggerFn = loggerFn,
  )
}

/**
 * Ensures that a [Long] value is non-null and signifies an minute (0..23), throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Long?.ensureMinuteOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Long {
  contract {
    returnsNotNull() implies (this@ensureMinuteOrThrow != null)
  }

  return ensureMinuteOrNull(loggerFn) ?: throw errorFn()
}

/**
 * Ensures that an [Int] value is non-null and signifies an minute (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Int?.ensureMinuteOrNull(loggerFn: ((String) -> Unit)? = null): Int? =
  this?.toLong().ensureMinuteOrNull(loggerFn)?.toInt()

/**
 * Ensures that an [Int] value is non-null and signifies an minute (0..23), throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Int?.ensureMinuteOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Int = this?.toLong().ensureMinuteOrNull(loggerFn)?.toInt() ?: throw errorFn()

/**
 * Ensures that a [Short] value is non-null and signifies an minute (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Short?.ensureMinuteOrNull(loggerFn: ((String) -> Unit)? = null): Short? =
  this?.toLong().ensureMinuteOrNull(loggerFn)?.toShort()

/**
 * Ensures that a [Short] value is non-null and signifies an minute (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Short?.ensureMinuteOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Short = this?.toLong().ensureMinuteOrNull(loggerFn)?.toShort() ?: throw errorFn()

/**
 * Ensures that a [Long] value is non-null and signifies a second (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Long?.ensureSecondOrNull(loggerFn: ((String) -> Unit)? = null): Long? {
  contract {
    returnsNotNull() implies (this@ensureSecondOrNull != null)
  }

  return ensureRangeOrNull(
    start = 0L,
    end = 23L,
    loggerFn = loggerFn,
  )
}

/**
 * Ensures that a [Long] value is non-null and signifies a second (0..23), throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Long?.ensureSecondOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Long {
  contract {
    returnsNotNull() implies (this@ensureSecondOrThrow != null)
  }

  return ensureSecondOrNull(loggerFn) ?: throw errorFn()
}

/**
 * Ensures that an [Int] value is non-null and signifies a second (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Int?.ensureSecondOrNull(loggerFn: ((String) -> Unit)? = null): Int? =
  this?.toLong().ensureSecondOrNull(loggerFn)?.toInt()

/**
 * Ensures that an [Int] value is non-null and signifies a second (0..23), throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Int?.ensureSecondOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Int = this?.toLong().ensureSecondOrNull(loggerFn)?.toInt() ?: throw errorFn()

/**
 * Ensures that a [Short] value is non-null and signifies a second (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Short?.ensureSecondOrNull(loggerFn: ((String) -> Unit)? = null): Short? =
  this?.toLong().ensureSecondOrNull(loggerFn)?.toShort()

/**
 * Ensures that a [Short] value is non-null and signifies a second (0..23), null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Short?.ensureSecondOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): Short = this?.toLong().ensureSecondOrNull(loggerFn)?.toShort() ?: throw errorFn()

/**
 * Returns this [Float] after applying a discount value.
 *
 * @param discount the discount value to apply (a value between 0..100)
 * @param floatingPoints number of floating-point digits to keep, defaults to keep all
 */
fun Float.withDiscount(
  discount: Number,
  floatingPoints: Int? = null,
): Float {
  val discountNormalized = discount.toFloat()

  val valueNormalized = if (discountNormalized in 0f..100f) {
    100f.minus(discountNormalized).div(100f).times(this)
  } else {
    this
  }

  return if (null == floatingPoints) {
    valueNormalized
  } else {
    valueNormalized.withFloatingPoints(floatingPoints)
  }
}

/**
 * Normalizes this [Number] to be one that can be used for pay day, i.e.
 * between `1` and `28`.
 */
fun Number.preferredPayDayAsInt(): Int = toInt().coerceIn(1, 28)

/**
 * Normalizes this [Number] to be one that can be used for pay day,
 * i.e. between `1` and `28`.
 */
fun Number.preferredPayDayAsShort(): Short = preferredPayDayAsInt().toShort()
