/*
 * The MIT License
 *
 * Copyright 2021 Noor Dawod. All rights reserved.
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

@file:Suppress("unused", "TooManyFunctions")

package org.noordawod.kotlin.core.extension

/**
 * Converts an [Int] value to its [String] representation.
 *
 * @param opacity apply a constant opacity value (0..255) to the color
 * @param dash whether to add a '#' character in the beginning, defaults to false
 */
@Suppress("MagicNumber")
fun Int?.toColor(opacity: Int? = null, dash: Boolean = false): String? = this?.let { color ->
  val buffer = StringBuffer(9)
  if (dash) {
    buffer.append('#')
  }

  @Suppress("UnclearPrecedenceOfBinaryExpression")
  val opacityValue = opacity ?: color shr 24 and 0xff
  val redValue = color shr 16 and 0xff
  val greenValue = color shr 8 and 0xff
  val blueValue = color and 0xff

  // Only add opacity if it's not 255 (0xff).
  if (opacityValue in 0..254) {
    buffer.append(Integer.toHexString(opacityValue))
  }

  buffer.append(Integer.toHexString(redValue))
  buffer.append(Integer.toHexString(greenValue))
  buffer.append(Integer.toHexString(blueValue))

  buffer.toString().uppercase(java.util.Locale.ENGLISH)
}
