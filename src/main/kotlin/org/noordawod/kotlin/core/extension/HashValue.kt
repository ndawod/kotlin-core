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

@file:Suppress("unused", "ReplaceIsEmptyWithIfEmpty", "DuplicatedCode")

package org.noordawod.kotlin.core.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import org.noordawod.kotlin.core.repository.HashValue
import org.noordawod.kotlin.core.repository.PublicId
import org.noordawod.kotlin.core.security.ByteUtils
import org.noordawod.kotlin.core.security.base62

/**
 * Returns true if this [HashValue] is non-null and non-empty, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun HashValue?.isValid(): Boolean {
  contract {
    returns(true) implies (this@isValid != null)
  }
  return null != this && isNotEmpty()
}

/**
 * Returns the hexadecimal representation of this [HashValue] on success,
 * null otherwise.
 *
 * @param escape whether to escape the result for SQL, f.ex: `x'…'`
 */
fun HashValue?.toHex(escape: Boolean = false): String? = if (isValid()) {
  ByteUtils.toHex(
    bytes = this,
    escape = escape,
  )
} else {
  null
}

/**
 * Returns the hexadecimal representation of this [HashValue] on success,
 * [fallback] otherwise.
 *
 * @param fallback value to return if this [HashValue] is null or empty
 * @param escape whether to escape the result for SQL, f.ex: `x'…'`
 */
fun HashValue?.toHexOr(
  fallback: String,
  escape: Boolean = false,
): String = toHex(escape) ?: fallback

/**
 * Returns the hexadecimal representation of this [HashValue] on success,
 * an empty string otherwise.
 *
 * @param escape whether to escape the result for SQL, f.ex: `x'…'`
 */
fun HashValue?.toHexOrEmpty(escape: Boolean = false): String = toHexOr(
  fallback = "",
  escape = escape,
)

/**
 * Returns the corresponding [PublicId] for this [HashValue] on success,
 * empty string otherwise.
 */
fun HashValue?.publicId(): PublicId = publicIdOr("")

/**
 * Returns the corresponding [PublicId] for this [HashValue] on success,
 * [fallback] otherwise.
 *
 * @param fallback value to return if [this][HashValue] is invalid
 */
fun HashValue?.publicIdOr(fallback: PublicId): PublicId = if (isValid()) base62() else fallback

/**
 * Returns the corresponding [PublicId] for this [HashValue] on success,
 * null otherwise.
 */
fun HashValue?.publicIdOrNull(): PublicId? = if (isValid()) base62() else null

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s.
 */
fun Collection<HashValue?>?.filterNonEmpty(): Collection<HashValue> {
  val result = ArrayList<HashValue>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (hashValue in this) {
      if (!hashValue.isValid()) {
        continue
      }

      result.add(hashValue)
    }
  }

  return result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s
 * on success, null if the result empty.
 */
fun Collection<HashValue?>?.filterNonEmptyOrNull(): Collection<HashValue>? {
  val result = filterNonEmpty()

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [HashValue]s that
 * were obtained through [transform] callback.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [HashValue]
 */
fun <T> Collection<T?>?.filterNonEmpty(transform: (T) -> HashValue?): Collection<HashValue> {
  val result = ArrayList<HashValue>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      if (null == entry) {
        continue
      }

      val transformedValue = transform(entry)

      if (transformedValue.isValid()) {
        result.add(transformedValue)
      }
    }
  }

  return result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [HashValue]s that
 * were obtained through [transform] callback, null if the result empty.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [HashValue]
 */
fun <T> Collection<T?>?.filterNonEmptyOrNull(transform: (T) -> HashValue?): Collection<HashValue>? {
  val result = filterNonEmpty(transform)

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only non-empty [PublicId]s.
 */
fun Collection<HashValue?>?.publicIds(): Collection<PublicId> {
  val result = ArrayList<PublicId>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      if (!entry.isValid()) {
        continue
      }

      val publicId = entry.publicIdOrNull() ?: continue

      result.add(publicId)
    }
  }

  return result
}

/**
 * Returns a new [Collection] that contains only non-empty [PublicId]s on success,
 * null otherwise.
 */
fun Collection<HashValue?>?.publicIdsOrNull(): Collection<PublicId>? {
  val result = publicIds()

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only non-empty [PublicId]s corresponding with
 * this [Collection].
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [HashValue]
 */
fun <T> Collection<T?>?.publicIds(transform: (T) -> HashValue?): Collection<PublicId> {
  val result = ArrayList<PublicId>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      if (null == entry) {
        continue
      }

      val transformedValue = transform(entry) ?: continue

      val publicId = transformedValue.publicIdOrNull() ?: continue

      result.add(publicId)
    }
  }

  return result
}

/**
 * Returns a new [Collection] that contains only non-empty [PublicId]s corresponding with
 * this [Collection] on success, null otherwise.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [HashValue]
 */
fun <T> Collection<T?>?.publicIdsOrNull(transform: (T) -> HashValue?): Collection<PublicId>? {
  val result = publicIds(transform)

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Map], indexed by [PublicId], that contains only non-empty keys along
 * with their respective values.
 *
 * @param T the type of values contained within this Map
 */
fun <T> Map<HashValue, T>?.publicIds(): Map<PublicId, T> {
  val result = mutableMapWith<PublicId, T>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      val key = entry.key.publicIdOrNull() ?: continue

      result[key] = entry.value
    }
  }

  return result
}

/**
 * Returns a new [Map], indexed by [PublicId], that contains only non-empty keys along
 * with their respective values on success, null otherwise.
 *
 * @param T the type of values contained within this Map
 */
fun <T> Map<HashValue, T>?.publicIdsOrNull(): Map<PublicId, T>? {
  val result = publicIds()

  return if (result.isEmpty()) null else result
}
