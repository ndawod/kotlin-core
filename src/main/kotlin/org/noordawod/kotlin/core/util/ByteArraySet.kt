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
 * Provides support for a [Set] of [ByteArray] objects.
 */
class ByteArraySet : java.util.TreeSet<ByteArray>(ByteArrayComparator) {
  companion object {
    /**
     * Returns a [ByteArraySet] from a list of [ByteArray] objects.
     *
     * @param list list of [ByteArray] objects
     */
    fun from(list: Iterable<ByteArray>): ByteArraySet = ByteArraySet().apply {
      list.forEach(::add)
    }

    /**
     * Returns a [ByteArraySet] from a list of [T] by transforming each of its items through
     * a transformer function.
     *
     * @param list list of [ByteArray] objects
     * @param transform transformer function for the [list]'s items
     */
    inline fun <T> from(
      list: Iterable<T>,
      transform: (T) -> ByteArray,
    ): ByteArraySet = ByteArraySet().apply {
      list.forEach {
        add(transform(it))
      }
    }
  }
}
