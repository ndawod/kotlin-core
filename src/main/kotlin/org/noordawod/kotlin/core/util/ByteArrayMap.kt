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

@file:Suppress("unused")

package org.noordawod.kotlin.core.util

/**
 * Provides support for a [Map] where the keys are of type [ByteArray].
 *
 * @param V types of values that can be stored in this [ByteArrayMap]
 */
class ByteArrayMap<V> : java.util.TreeMap<ByteArray, V>(ByteArrayComparator) {
  /**
   * Associates the specified value with the specified key in this map. If the map previously
   * contained a mapping for the key, the old value is replaced.
   */
  fun put(key: ByteArrayWrapper, value: V): V? = put(key.bytes, value)

  /**
   * Returns the value to which the specified key is mapped, or `null` if this map contains
   * no mapping for the key.
   */
  operator fun get(key: ByteArrayWrapper): V? = get(key.bytes)

  /**
   * Returns `true` if this map contains a mapping for the specified key.
   */
  fun contains(value: ByteArrayWrapper): Boolean = containsKey(value.bytes)

  companion object {
    /**
     * Generate a unique set of values from the specified map.
     */
    fun <V> values(map: ByteArrayMap<V>): Collection<V> = HashSet<V>(map.size).apply {
      map.forEach { (_, v) ->
        this.add(v)
      }
    }
  }
}

/**
 * Provides wrapping of [ByteArray] values that can be compared using [Comparable].
 *
 * @param bytes the bytes to wrap
 */
class ByteArrayWrapper private constructor(val bytes: ByteArray) : Comparable<ByteArrayWrapper> {
  override operator fun compareTo(other: ByteArrayWrapper): Int =
    ByteArrayComparator.compare(bytes, other.bytes)

  override fun equals(other: Any?): Boolean =
    other is ByteArrayWrapper && bytes.contentEquals(other.bytes) ||
      other is ByteArray && bytes.contentEquals(other)

  override fun hashCode(): Int = bytes.contentHashCode()

  companion object {
    /**
     * Wraps the provided [bytes] in a [ByteArrayWrapper] instance.
     */
    fun wrap(bytes: ByteArray): ByteArrayWrapper = ByteArrayWrapper(bytes)

    /**
     * Unwraps the provided [entries] to their original [ByteArray] instances.
     */
    fun unwrap(entries: Collection<ByteArrayWrapper>?): Collection<ByteArray>? =
      if (true == entries?.isNotEmpty()) {
        ArrayList<ByteArray>(entries.size).apply {
          for (entry in entries) {
            add(entry.bytes)
          }
        }
      } else {
        null
      }
  }
}
