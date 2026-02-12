/*
 * The MIT License
 *
 * Copyright 2026 Noor Dawod. All rights reserved.
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
 * Denotes the sorting direction which can be either [ascending][ASCENDING] or
 * [descending][DESCENDING].
 *
 * @param value a human-friendly value of this instance
 */
enum class SortingDirection(
  private val value: String,
) {
  /**
   * The sorting direction is ascending.
   */
  ASCENDING("ASC"),

  /**
   * The sorting direction is descending.
   */
  DESCENDING("DESC"),
  ;

  override fun toString(): String = value

  /**
   * Returns true if this [SortingDirection] is [ascending][ASCENDING], false otherwise.
   */
  val isAscending: Boolean
    get() = ASCENDING == this

  /**
   * Returns true if this [SortingDirection] is [descending][DESCENDING], false otherwise.
   */
  val isDescending: Boolean
    get() = DESCENDING == this
}
