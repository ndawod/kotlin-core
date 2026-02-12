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

package org.noordawod.kotlin.core.logger

import org.noordawod.kotlin.core.DEFAULT_LIST_CAPACITY
import org.noordawod.kotlin.core.MEDIUM_BLOCK_SIZE
import org.noordawod.kotlin.core.extension.mutableListWith
import org.noordawod.kotlin.core.extension.trimOrBlank

/**
 * Records each log message in memory, and allows to retrieve as-is later on.
 *
 * @param environment the textual representation of the running environment
 */
class TransparentLogger(
  environment: String,
) : BaseSimpleLogger(environment) {
  private val mutableBuffer = mutableListWith<TransparentLogEntry>(DEFAULT_LIST_CAPACITY)

  /**
   * Returns the buffer list backing this instance.
   */
  val buffer: List<TransparentLogEntry>
    get() = mutableBuffer.toList()

  override fun log(
    type: LogType,
    tag: String,
    message: String,
    error: Throwable?,
  ) {
    mutableBuffer.add(
      TransparentLogEntry(
        type = type,
        tag = tag,
        message = message,
        error = error,
      ),
    )
  }

  override fun toString(): String {
    val buffer = StringBuffer(MEDIUM_BLOCK_SIZE)

    for (entry in mutableBuffer) {
      buffer.append("$entry\n")
    }

    return "$buffer".trimOrBlank()
  }

  /**
   * Clears the log from all entries.
   */
  fun clear() {
    mutableBuffer.clear()
  }
}

/**
 * The data model inserted into the logger buffer.
 *
 *
 * @param type the log type
 * @param tag the log tag
 * @param message the log message
 * @param error optional error to accompany the log message
 */
data class TransparentLogEntry(
  val type: LogType,
  val tag: String,
  val message: String,
  val error: Throwable?,
) {
  override fun toString(): String = "$type:$tag:$message"
}

fun Throwable.stackTraceOf(): String = java.io.StringWriter(MEDIUM_BLOCK_SIZE).use {
  printStackTrace(java.io.PrintWriter(it))
  "$it".trimOrBlank()
}
