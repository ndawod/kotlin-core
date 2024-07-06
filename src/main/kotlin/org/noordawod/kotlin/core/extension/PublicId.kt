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
@file:OptIn(ExperimentalContracts::class)

package org.noordawod.kotlin.core.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import org.noordawod.kotlin.core.repository.EmptyHashValue
import org.noordawod.kotlin.core.repository.HashValue
import org.noordawod.kotlin.core.repository.PublicId
import org.noordawod.kotlin.core.security.base62
import org.noordawod.kotlin.core.util.ByteArrayMap

/**
 * Returns the corresponding [HashValue] when this [PublicId] is [valid][isValid],
 * [EmptyHashValue] otherwise.
 */
fun PublicId?.hashValue(): HashValue {
  contract {
    returnsNotNull() implies (this@hashValue != null)
  }

  return hashValueOr(EmptyHashValue)
}

/**
 * Returns the corresponding [HashValue] when this [PublicId] is [valid][isValid],
 * [fallback] otherwise.
 *
 * @param fallback value to return if this [PublicId] is null or empty
 */
fun PublicId?.hashValueOr(fallback: HashValue): HashValue {
  contract {
    returnsNotNull() implies (this@hashValueOr != null)
  }

  val normalized = this?.trim()

  return if (normalized.isNullOrEmpty()) fallback else normalized.base62()
}

/**
 * Returns the corresponding [HashValue] when this [PublicId] is [valid][isValid],
 * null otherwise.
 */
fun PublicId?.hashValueOrNull(): HashValue? {
  contract {
    returnsNotNull() implies (this@hashValueOrNull != null)
  }

  val normalized = this?.trim()

  return if (normalized.isNullOrEmpty()) null else normalized.base62()
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s.
 */
fun Collection<PublicId?>?.filterNonEmpty(): Collection<PublicId> {
  contract {
    returnsNotNull() implies (this@filterNonEmpty != null)
  }

  val result = ArrayList<PublicId>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      if (entry.isNullOrEmpty()) {
        continue
      }

      result.add(entry)
    }
  }

  return result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s
 * on success, null if the result empty.
 */
fun Collection<PublicId?>?.filterNonEmptyOrNull(): Collection<PublicId>? {
  contract {
    returnsNotNull() implies (this@filterNonEmptyOrNull != null)
  }

  val result = filterNonEmpty()

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s that
 * were obtained through [transform] callback.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that optionally transforms a [T] value to [PublicId]
 */
fun <T> Collection<T?>?.filterNonEmpty(transform: (T) -> PublicId?): Collection<PublicId> {
  contract {
    returnsNotNull() implies (this@filterNonEmpty != null)
  }

  val result = ArrayList<PublicId>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      if (null == entry) {
        continue
      }

      val transformedValue = transform(entry)

      if (!transformedValue.isNullOrEmpty()) {
        result.add(transformedValue)
      }
    }
  }

  return result
}

/**
 * Returns a new [Collection] that contains only non-null and non-empty [PublicId]s that
 * were obtained through [transform] callback, null if the result empty.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that optionally transforms a [T] value to [PublicId]
 */
fun <T> Collection<T?>?.filterNonEmptyOrNull(transform: (T) -> PublicId?): Collection<PublicId>? {
  contract {
    returnsNotNull() implies (this@filterNonEmptyOrNull != null)
  }

  val result = filterNonEmpty(transform)

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only [valid][isValid] [HashValue]s.
 */
fun Collection<PublicId?>?.hashValues(): Collection<HashValue> {
  contract {
    returnsNotNull() implies (this@hashValues != null)
  }

  val result = ArrayList<HashValue>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      val hashValue = entry.hashValueOrNull() ?: continue

      result.add(hashValue)
    }
  }

  return result
}

/**
 * Returns a new non-empty [Collection] that contains only [valid][isValid] [HashValue]s,
 * null if the new collection is empty.
 */
fun Collection<PublicId?>?.hashValuesOrNull(): Collection<HashValue>? {
  contract {
    returnsNotNull() implies (this@hashValuesOrNull != null)
  }

  val result = hashValues()

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Collection] that contains only [valid][isValid] [HashValue]s.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [PublicId]
 */
fun <T> Collection<T?>?.hashValues(transform: (T) -> PublicId?): Collection<HashValue> {
  contract {
    returnsNotNull() implies (this@hashValues != null)
  }

  val result = ArrayList<HashValue>(this?.size ?: 0)

  if (!isNullOrEmpty()) {
    for (entry in this) {
      if (null == entry) {
        continue
      }

      val transformedValue = transform(entry) ?: continue

      val hashValue = transformedValue.hashValueOrNull() ?: continue

      result.add(hashValue)
    }
  }

  return result
}

/**
 * Returns a new [Collection] that contains only [valid][isValid] [HashValue]s,
 * null otherwise.
 *
 * @param T the type of values contained within this Collection
 * @param transform a function that transforms a [T] value to [PublicId]
 */
fun <T> Collection<T?>?.hashValuesOrNull(transform: (T) -> PublicId?): Collection<HashValue>? {
  contract {
    returnsNotNull() implies (this@hashValuesOrNull != null)
  }

  val result = hashValues(transform)

  return if (result.isEmpty()) null else result
}

/**
 * Returns a new [Map], indexed by [HashValue], that contains only non-empty keys along
 * with their respective values.
 *
 * @param T the type of values contained within this Map
 */
fun <T> Map<PublicId, T>?.hashValues(): ByteArrayMap<T> {
  contract {
    returnsNotNull() implies (this@hashValues != null)
  }

  val result = ByteArrayMap<T>()

  if (!isNullOrEmpty()) {
    for (entry in this) {
      val key = entry.key.hashValueOrNull() ?: continue

      result[key] = entry.value
    }
  }

  return result
}

/**
 * Returns a new [Map], indexed by [HashValue], that contains only non-empty keys along
 * with their respective values on success, null otherwise.
 *
 * @param T the type of values contained within this Map
 */
fun <T> Map<PublicId, T>?.hashValuesOrNull(): ByteArrayMap<T>? {
  contract {
    returnsNotNull() implies (this@hashValuesOrNull != null)
  }

  val result = hashValues()

  return if (result.isEmpty()) null else result
}
