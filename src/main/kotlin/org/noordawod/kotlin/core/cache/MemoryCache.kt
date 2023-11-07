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

import org.noordawod.kotlin.core.extension.mutableMapWith

/**
 * Creates a new [MemoryCache] instance having both its maximum entries and timeout set to
 * custom values.
 *
 * @param maxEntries maximum number of entries to keep in the cache at any given time
 * @param seconds number of seconds to cache each value
 */
class MemoryCache<V>(
  override val maxEntries: Int,
  override val seconds: Int,
) : Cache<V> {
  /**
   * Creates a new [MemoryCache] instance having a default timeout set to
   * [Cache.SHORT_TIMEOUT_IN_SECONDS].
   *
   * @param maxEntries maximum number of entries to keep in the cache at any given time
   */
  constructor(maxEntries: Int) : this(maxEntries, Cache.SHORT_TIMEOUT_IN_SECONDS)

  /**
   * Creates a new [MemoryCache] instance having a default timeout set to
   * [Cache.SHORT_TIMEOUT_IN_SECONDS] and a default max entries set to [Cache.MAX_ENTRIES].
   */
  constructor() : this(Cache.MAX_ENTRIES)

  private val cache: com.google.common.cache.Cache<CacheId, V>

  init {
    check(maxEntries > 0) {
      "Max entries must be a positive value, but $maxEntries is given."
    }
    check(seconds > 0) {
      "Timeout (in seconds) must be a positive value, but $seconds is given."
    }
    cache = com.google.common.cache.CacheBuilder
      .newBuilder()
      .maximumSize(maxEntries.toLong())
      .expireAfterWrite(seconds.toLong(), java.util.concurrent.TimeUnit.SECONDS)
      .build()
  }

  override val map: Map<CacheId, V>
    get() = mutableMapWith<CacheId, V>(cache.size().toInt()).apply {
      cache.asMap().forEach { (key, value) ->
        put(key, value)
      }
    }

  override operator fun get(key: CacheId): V? = cache.getIfPresent(key)

  override operator fun set(key: CacheId, value: V): V {
    cache.put(key, value)
    return value
  }

  override fun remove(key: CacheId): V? {
    val value = get(key)
    cache.invalidate(key)
    return value
  }

  override fun clear() {
    cache.invalidateAll()
  }
}
