/*
 * COPYRIGHT (C) PAPER KITE SYSTEMS. ALL RIGHTS RESERVED.
 * UNAUTHORIZED DUPLICATION, MODIFICATION OR PUBLICATION IS PROHIBITED.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF PAPER KITE SYSTEMS.
 * THE COPYRIGHT NOTICE ABOVE DOES NOT EVIDENCE ANY ACTUAL OR INTENDED
 * PUBLICATION OF SUCH SOURCE CODE.
 */

@file:Suppress("unused")

package org.noordawod.kotlin.core.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import org.noordawod.kotlin.core.repository.HashValue

/**
 * Ensures that a value is valid by invoking a callback function, throws [error] otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun <T> T?.ensureValidOrThrow(
  isValidFn: (T.() -> Boolean),
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): T {
  contract {
    returnsNotNull() implies (this@ensureValidOrThrow != null)
  }

  if (null == this) {
    loggerFn?.invoke("The value cannot be null.")
    throw errorFn()
  }

  if (!isValidFn()) {
    loggerFn?.invoke("The value is invalid.")
    throw errorFn()
  }

  return this
}

/**
 * Ensures that a value is non-null, throws [error] otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun <T> T?.ensureNonNullOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): T {
  contract {
    returnsNotNull() implies (this@ensureNonNullOrThrow != null)
  }

  if (null == this) {
    loggerFn?.invoke("The value cannot be null.")
    throw errorFn()
  }

  return this
}

/**
 * Ensures that a value is null, throws [error] otherwise.
 */
fun <T> T?.ensureNullOrThrow(
  errorFn: (T) -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
) {
  if (null != this) {
    loggerFn?.invoke("The value must be null.")
    throw errorFn(this)
  }
}

/**
 * Ensures that a value is non-null and non-empty if it's a [String],
 * an [Iterable] or an [Array], throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("ComplexCondition", "ThrowsCount")
fun <T> T?.ensureNonEmptyOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): T {
  contract {
    returnsNotNull() implies (this@ensureNonEmptyOrThrow != null)
  }

  if (null == this) {
    loggerFn?.invoke("Value of variable cannot be null.")
    throw errorFn()
  }

  if (this is HashValue && isEmpty()) {
    loggerFn?.invoke("Hash variable value cannot be empty.")
    throw errorFn()
  }

  if (this is CharSequence && isEmpty()) {
    loggerFn?.invoke("Character sequence variable value cannot be empty.")
    throw errorFn()
  }

  if (this is Iterable<*> && !iterator().hasNext()) {
    loggerFn?.invoke("Iterable variable value cannot be empty.")
    throw errorFn()
  }

  if (this is Array<*> && !iterator().hasNext()) {
    loggerFn?.invoke("Array variable value cannot be empty.")
    throw errorFn()
  }

  return this
}

/**
 * Ensures that a value is a valid email, throws [error] otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.ensureEmailOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): String {
  contract {
    returnsNotNull() implies (this@ensureEmailOrThrow != null)
  }

  if (null == this) {
    val message = "The email address cannot be null."
    loggerFn?.invoke(message)
    throw errorFn()
  }

  if (!isEmail()) {
    val message = "The email address is invalid."
    loggerFn?.invoke(message)
    throw errorFn()
  }

  return this
}

/**
 * Ensures that a numeric value is non-null and natural (0, 1, 2, 3, ...),
 * null otherwise.
 */
fun <T : Number> T?.ensureNaturalOrNull(loggerFn: ((String) -> Unit)? = null): T? {
  if (null == this || 0 > toDouble()) {
    loggerFn?.invoke("Value of number cannot be less than 0.")
    return null
  }

  return this
}

/**
 * Ensures that a numeric value is non-null and natural (0, 1, 2, 3, ...),
 * throws otherwise.
 */
fun <T : Number> T?.ensureNaturalOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): T = ensureNaturalOrNull(loggerFn) ?: throw errorFn()

/**
 * Ensures that a numeric value is non-null and positive (1, 2, 3, ...),
 * null otherwise.
 */
fun <T : Number> T?.ensurePositiveOrNull(loggerFn: ((String) -> Unit)? = null): T? {
  if (null == this || 0 >= toDouble()) {
    loggerFn?.invoke("Value of number cannot be 0 or less.")
    return null
  }

  return this
}

/**
 * Ensures that a numeric value is non-null and positive (1, 2, 3, ...),
 * throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun <T : Number> T?.ensurePositiveOrThrow(
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): T {
  contract {
    returnsNotNull() implies (this@ensurePositiveOrThrow != null)
  }

  return ensurePositiveOrNull(loggerFn) ?: throw errorFn()
}

/**
 * Ensures that a numeric value is non-null and falls within a specific range (inclusive),
 * null otherwise.
 */
fun <T : Number> T?.ensureRangeOrNull(
  start: T,
  end: T? = null,
  loggerFn: ((String) -> Unit)? = null,
): T? {
  if (null == this) {
    loggerFn?.invoke("Value of number is null.")
    return null
  }

  val number = toDouble()

  val startNumber = start.toDouble()
  if (startNumber > number) {
    loggerFn?.invoke("Value of number ($number) is less than ($startNumber).")
    return null
  }

  val endNumber = end?.toDouble()
  if (null != endNumber && endNumber < number) {
    loggerFn?.invoke("Value of number ($number) is greater than ($endNumber).")
    return null
  }

  return this
}

/**
 * Ensures that a numeric value is non-null and falls within a specific range (inclusive),
 * throws otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun <T : Number> T?.ensureRangeOrThrow(
  start: T,
  end: T? = null,
  errorFn: () -> Throwable,
  loggerFn: ((String) -> Unit)? = null,
): T {
  contract {
    returnsNotNull() implies (this@ensureRangeOrThrow != null)
  }

  return ensureRangeOrNull(
    start = start,
    end = end,
    loggerFn = loggerFn,
  ) ?: throw errorFn()
}
