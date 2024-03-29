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
    returns(true) implies (null != this@isValid)
  }
  return null != this && isNotEmpty()
}

/**
 * Returns the corresponding [PublicId] for this [HashValue] on success,
 * null otherwise.
 */
fun HashValue?.publicId(): PublicId? = if (isValid()) base62() else null

/**
 * Returns the corresponding [PublicId] for this [HashValue] on success,
 * [fallback] otherwise.
 *
 * @param fallback value to return if this [HashValue] is null or empty
 */
fun HashValue?.publicIdOr(fallback: PublicId): PublicId = publicId() ?: fallback

/**
 * Returns the corresponding [PublicId] for this [HashValue] on success,
 * an empty PublicId otherwise.
 */
fun HashValue?.publicIdOrEmpty(): PublicId = publicIdOr("")

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
 * Returns a new [Collection] that contains only non-null and non-empty [HashValue]s.
 */
fun Collection<HashValue?>?.filterNonEmpty(): Collection<HashValue>? = if (null == this) {
  null
} else {
  val result = ArrayList<HashValue>(size)
  forEach { hashValue ->
    if (hashValue.isValid()) {
      result.add(hashValue)
    }
  }
  if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [HashValue]s that
 * are obtained through transforming them via [transform].
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [HashValue]
 */
fun <T> Collection<T?>?.filterNonEmpty(transform: ((T) -> HashValue?)): Collection<HashValue>? =
  if (null == this) {
    null
  } else {
    val result = ArrayList<HashValue>(size)
    forEach { entry ->
      val hashValue = if (null == entry) null else transform(entry)
      if (hashValue.isValid()) {
        result.add(hashValue)
      }
    }
    if (result.isEmpty()) null else result
  }

/**
 * Returns a new [Collection] that contains only non-empty [PublicId]s corresponding with
 * this [HashValue] [Collection], null otherwise.
 */
fun Collection<HashValue?>?.publicId(): Collection<PublicId>? {
  val nonEmptyHashValues = filterNonEmpty() ?: return null
  val result = ArrayList<PublicId>(nonEmptyHashValues.size)

  nonEmptyHashValues.map { hashValue ->
    val publicId = hashValue.publicId()
    if (null != publicId) {
      result.add(publicId)
    }
  }

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only non-empty [PublicId]s corresponding with
 * this [Collection], null otherwise.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [HashValue]
 */
fun <T> Collection<T?>?.publicId(transform: ((T) -> HashValue?)): Collection<PublicId>? =
  filterNonEmpty(transform)?.publicId()

/**
 * Returns a new [Map], indexed by [PublicId], that contains only non-empty keys along
 * with their respective values on success, null otherwise.
 */
fun <T> Map<HashValue, T>?.publicId(): Map<PublicId, T>? {
  if (null == this) {
    return null
  }

  val result = mutableMapWith<PublicId, T>(size)

  for (entry in this) {
    val key = entry.key.publicId() ?: continue
    result[key] = entry.value
  }

  return if (result.isEmpty()) null else result
}
