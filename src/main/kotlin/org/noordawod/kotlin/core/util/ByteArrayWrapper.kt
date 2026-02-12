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

package org.noordawod.kotlin.core.util

/**
 * Wraps of [ByteArray] value, so it can be compared using a [Comparable].
 *
 * @param bytes the bytes to wrap
 */
class ByteArrayWrapper private constructor(
  val bytes: ByteArray,
) : Comparable<ByteArrayWrapper> {
  override operator fun compareTo(other: ByteArrayWrapper): Int =
    ByteArrayComparator.compare(bytes, other.bytes)

  @Suppress("ktlint:standard:condition-wrapping")
  override fun equals(other: Any?): Boolean =
    other is ByteArrayWrapper && bytes.contentEquals(other.bytes) ||
      other is ByteArray && bytes.contentEquals(other)

  override fun hashCode(): Int = bytes.contentHashCode()

  /**
   * Static functions, constants and other values.
   */
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
