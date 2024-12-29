/*
 * The MIT License
 *
 * Copyright 2024 Noor Dawod. All rights reserved.
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

@file:Suppress("MagicNumber")

package org.noordawod.kotlin.core.extension

import kotlin.math.roundToLong

/**
 * Returns the exact value of this [Float] as a [Double] without any side effects, such
 * as converting the floating points to some imaginary value.
 */
fun Float.toExactDouble(): Double = "$this".toDouble()

/**
 * Returns a Float number trimmed to a certain number of floating points.
 *
 * @param floatingPoints how many floating-point numbers to include, defaults to 2
 */
fun Float.withFloatingPoints(floatingPoints: Int = 2): Float {
  var multiplier = 1f
  for (idx in 1..floatingPoints) {
    multiplier *= 10f
  }

  return times(multiplier).roundToLong().div(multiplier)
}

/**
 * Returns a string representation of this Float and trim any floating-point numbers
 * if they're equal to 0.
 *
 * This is normally called after [withFloatingPoints].
 */
@Suppress("MagicNumber")
fun Float.trimIfZero(): String {
  val stringified = "$this"

  return if (stringified.endsWith(".0")) {
    stringified.substring(0, stringified.length - 2)
  } else {
    stringified
  }
}
