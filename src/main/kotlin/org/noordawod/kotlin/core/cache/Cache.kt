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

package org.noordawod.kotlin.core.cache

import org.noordawod.kotlin.core.extension.SECONDS_IN_1_DAY
import org.noordawod.kotlin.core.extension.SECONDS_IN_1_HOUR
import org.noordawod.kotlin.core.extension.SECONDS_IN_1_MINUTE

/**
 * The type of cache value's key.
 */
typealias CacheId = String

/**
 * A contract that defines a cache.
 *
 * A cache is a simple mechanism where values (of type [V]) are stored in it for a while until
 * the value is considered expired. The storage mechanism can be memory-based, but others may
 * be used as well -- for example, disk-based storage.
 *
 * Each value in the cache must have a unique [CacheId] key, which is simply a String. If the
 * unique value identifier is not a String, then it's the implementation's responsibility to
 * convert it to String before placing it in the cache.
 *
 * @param V type of values stored in the cache
 */
interface Cache<V> {
  /**
   * Maximum number of entries to keep in the cache at any given time.
   *
   * If this value is non-positive, then no maximum is imposed.
   */
  val maxEntries: Int

  /**
   * The default number of seconds to cache each value.
   *
   * This value is used whenever no seconds are given for a new cache value.
   */
  val seconds: Int

  /**
   * Returns a map of all cached keys and values.
   *
   * Note: the map is a copy of the existing values.
   */
  val map: Map<CacheId, V>

  /**
   * Returns a cached value identified by its unique [key] on success, null otherwise.
   *
   * @param key the value's unique key
   */
  operator fun get(key: CacheId): V?

  /**
   * Stores a new [value], or updates an existing one, identified by its unique [key], and
   * returns its expiration [date and time][java.util.Date].
   *
   * The value will have a [seconds] as expiration.
   *
   * @param key the value's unique key
   * @param value the value to cache
   */
  operator fun set(key: CacheId, value: V): V

  /**
   * Removes a cached value identified by its unique [key], returns the removed value on
   * success, null otherwise.
   *
   * @param key the value's unique key
   */
  fun remove(key: CacheId): V?

  /**
   * Removes all cached values.
   */
  fun clear()

  companion object {
    /**
     * Default maximum number of entries to keep in the cache at any given time.
     */
    const val MAX_ENTRIES: Int = 500

    /**
     * Short number of seconds to cache a value in the cache.
     *
     * Value is set to 5 minutes.
     */
    const val SHORT_TIMEOUT_IN_SECONDS: Int = (5 * SECONDS_IN_1_MINUTE).toInt()

    /**
     * Normal number of seconds to cache a value in the cache.
     *
     * Value is set to 1 hour.
     */
    const val NORMAL_TIMEOUT_IN_SECONDS: Int = SECONDS_IN_1_HOUR.toInt()

    /**
     * Long number of seconds to cache a value in the cache.
     *
     * Value is set to 1 day.
     */
    const val LONG_TIMEOUT_IN_SECONDS: Int = SECONDS_IN_1_DAY.toInt()
  }
}

/**
 * A handy getter-setter extension function to return a cached value inside a [Cache],
 * or create one anew using the provided [block] function.
 *
 * The new value is kept in the cache for up to [Cache.seconds].
 *
 * @param V type of value to cache
 * @param key the value's unique key
 * @param block a function that creates a new value
 */
fun <V> Cache<V>.getOrSet(key: CacheId, block: (CacheId) -> V?): V? {
  var data = get(key)
  if (null == data) {
    val value = block(key)
    if (null != value) {
      data = set(key, value)
    }
  }
  return data
}

/**
 * A handy updater extension function to update and return a cached value inside a [Cache]
 * via the provided [block] function.
 *
 * The new value is kept in the cache for up to [Cache.seconds].
 *
 * @param V type of value to cache
 * @param key the value's unique key
 * @param block a function that returns a new value
 */
fun <V> Cache<V>.update(key: CacheId, block: (V) -> V): V? {
  val data = get(key)
  return if (null == data) null else set(key, block(data))
}
