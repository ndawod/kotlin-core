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

package org.noordawod.kotlin.core.extension

import org.noordawod.kotlin.core.repository.EmptyHashValue
import org.noordawod.kotlin.core.repository.HashValue
import org.noordawod.kotlin.core.repository.PublicId
import org.noordawod.kotlin.core.security.base62
import org.noordawod.kotlin.core.util.ByteArrayMap

/**
 * Returns the corresponding [HashValue] for this [PublicId] on success,
 * null otherwise.
 *
 * A valid HashValue is one that is non-null and that contains at least one byte. So a 0-byte
 * HashValue is considered invalid, and the function will return null in such cases.
 */
fun PublicId?.hashValue(): HashValue? = if (isNullOrBlank()) null else base62()

/**
 * Returns the corresponding [HashValue] for this [PublicId] on success,
 * [fallback] otherwise.
 *
 * A valid HashValue is one that is non-null and that contains at least one byte. So a 0-byte
 * HashValue is considered invalid, and the function will return null in such cases.
 *
 * @param fallback value to return if this [PublicId] is null or empty
 */
fun PublicId?.hashValueOr(fallback: HashValue): HashValue = hashValue() ?: fallback

/**
 * Returns the corresponding [HashValue] for this [PublicId] on success,
 * [EmptyHashValue] otherwise.
 *
 * A valid HashValue is one that is non-null and that contains at least one byte. So a 0-byte
 * HashValue is considered invalid, and the function will return null in such cases.
 *
 * It's worth noting that [EmptyHashValue] is, technically, invalid as it contains no bytes.
 */
fun PublicId?.hashValueOrEmpty(): HashValue = hashValueOr(EmptyHashValue)

/**
 * Returns the corresponding [HashValue] for this [PublicId] on success,
 * throws otherwise.
 *
 * A valid HashValue is one that is non-null and that contains at least one byte. So a 0-byte
 * HashValue is considered invalid.
 */
fun PublicId?.hashValueOrThrow(
  errorProvider: (() -> Throwable) = {
    IllegalStateException("Unable to calculate hash value.")
  },
): HashValue {
  if (isNullOrBlank()) {
    throw errorProvider()
  }
  return base62()
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s.
 */
fun Collection<PublicId?>?.filterNonEmpty(): Collection<PublicId>? = if (null == this) {
  null
} else {
  val result = ArrayList<PublicId>(size)
  forEach { entry ->
    if (!entry.isNullOrEmpty()) {
      result.add(entry)
    }
  }
  if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s that
 * are obtained through transforming them via [transform].
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [PublicId]
 */
fun <T> Collection<T?>?.filterNonEmpty(transform: ((T) -> PublicId?)): Collection<PublicId>? =
  if (null == this) {
    null
  } else {
    val result = ArrayList<PublicId>(size)
    forEach { entry ->
      val hashValue = if (null == entry) null else transform(entry)
      if (!hashValue.isNullOrEmpty()) {
        result.add(hashValue)
      }
    }
    if (result.isEmpty()) null else result
  }

/**
 * Returns a new [Collection] that contains only non-empty [HashValue]s corresponding with
 * this [PublicId] [Collection], null otherwise.
 */
fun Collection<PublicId?>?.hashValue(): Collection<HashValue>? {
  val nonEmptyPublicIds = filterNonEmpty() ?: return null
  val result = ArrayList<HashValue>(nonEmptyPublicIds.size)

  nonEmptyPublicIds.map { publicId ->
    val hashValue = publicId.hashValue()
    if (null != hashValue && hashValue.isNotEmpty()) {
      result.add(hashValue)
    }
  }

  return if (result.isEmpty()) null else result
}

/**
 * Returns the corresponding collection of [HashValue]s for these [PublicId]s on success,
 * throws otherwise.
 *
 * A valid HashValue is one that is non-null and that contains at least one byte. So a 0-byte
 * HashValue is considered invalid.
 */
fun Collection<PublicId?>?.hashValuesOrThrow(
  errorProvider: (() -> Throwable) = {
    IllegalStateException("Unable to calculate hash value.")
  },
): Collection<HashValue> {
  if (null == this) {
    throw errorProvider()
  }

  val nonEmptyPublicIds = filterNonEmpty() ?: return emptyList()
  val result = ArrayList<HashValue>(nonEmptyPublicIds.size)

  nonEmptyPublicIds.map { publicId ->
    val hashValue = publicId.hashValue()
    if (null != hashValue && hashValue.isNotEmpty()) {
      result.add(hashValue)
    }
  }

  if (result.isEmpty()) {
    throw errorProvider()
  }

  return result
}

/**
 * Returns a new [Collection] that contains only non-empty [HashValue]s corresponding with
 * this [Collection], null otherwise.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [PublicId]
 */
fun <T> Collection<T?>?.hashValue(transform: ((T) -> PublicId?)): Collection<HashValue>? =
  filterNonEmpty(transform)?.hashValue()

/**
 * Returns a new [Map], indexed by [HashValue], that contains only non-empty keys along
 * with their respective values on success, null otherwise.
 */
fun <T> Map<PublicId, T>?.hashValue(): ByteArrayMap<T>? {
  if (null == this) {
    return null
  }

  val result = ByteArrayMap<T>()

  for (entry in this) {
    val key = entry.key.hashValue() ?: continue
    result[key] = entry.value
  }

  return if (result.isEmpty()) null else result
}
