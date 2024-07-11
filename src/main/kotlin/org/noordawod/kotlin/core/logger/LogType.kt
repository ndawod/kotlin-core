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

package org.noordawod.kotlin.core.logger

/**
 * Types of log message we support. To keep it simple, only 3 types are defined.
 *
 * @param value a human-friendly short description of the enum entry
 * @param order the order of importance of this type
 */
enum class LogType(
  val value: String,
  val order: Int,
) {
  /**
   * A log type suitable for information related to debugging. Most messages will use this
   * type.
   */
  INFO(
    value = "info",
    order = 1,
  ),

  /**
   * A log type for warnings, or non-breaking errors. Such situations the app is able to
   * recover from and still produce a meaningful response.
   */
  WARNING(
    value = "warning",
    order = 50,
  ),

  /**
   * A log type for breaking errors. Such situations cause the app to stop abruptly and report
   * an error to the client.
   */
  ERROR(
    value = "error",
    order = 90,
  ),
  ;

  override fun toString(): String = value
}

/**
 * Returns true if this [LogType] has a higher [order][LogType.order] than [another][other].
 *
 * @param other the other [LogType] value
 * @param orEqual yields true also if orders are equal
 */
fun LogType.higherOrderThan(
  other: Any?,
  orEqual: Boolean = true,
): Boolean = other is LogType && (value > other.value || orEqual && value == other.value)

/**
 * Returns true if this [LogType] has a lower [order][LogType.order] than [another][other].
 *
 * @param other the other [LogType] value
 * @param orEqual yields true also if orders are equal
 */
fun LogType.lowerOrderThan(
  other: Any?,
  orEqual: Boolean = true,
): Boolean = other is LogType && (value < other.value || orEqual && value == other.value)
