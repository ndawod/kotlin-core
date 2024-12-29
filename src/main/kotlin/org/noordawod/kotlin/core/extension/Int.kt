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

/**
 * Converts an [Int] value to its [String] representation.
 *
 * @param opacity apply a constant opacity value (0..255) to the color
 * @param dash whether to add a '#' character in the beginning, defaults to false
 */
@OptIn(ExperimentalContracts::class)
@Suppress("MagicNumber")
fun Int?.toColorOrNull(
  opacity: Int? = null,
  dash: Boolean = false,
): String? {
  contract {
    returnsNotNull() implies (this@toColorOrNull != null)
  }

  return if (null == this) {
    null
  } else {
    val buffer = StringBuffer(9)
    if (dash) {
      buffer.append('#')
    }

    @Suppress("UnclearPrecedenceOfBinaryExpression")
    val opacityValue = opacity ?: this shr 24 and 0xff
    val redValue = this shr 16 and 0xff
    val greenValue = this shr 8 and 0xff
    val blueValue = this and 0xff

    // Only add opacity if it's not 255 (0xff).
    if (opacityValue in 0..254) {
      buffer.append(Integer.toHexString(opacityValue))
    }

    buffer.append(Integer.toHexString(redValue))
    buffer.append(Integer.toHexString(greenValue))
    buffer.append(Integer.toHexString(blueValue))

    buffer.toString().uppercase(java.util.Locale.ENGLISH)
  }
}

/**
 * Returns the start offset in pagination based on a page number and its capacity.
 *
 * Note: Starting page is `1`, and the returned offset value starts from `0`.
 */
internal fun Int.offset(page: Int): Int = ((page - 1) * this).coerceAtLeast(0)
