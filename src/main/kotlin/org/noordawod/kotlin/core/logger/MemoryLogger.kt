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

import org.noordawod.kotlin.core.MEDIUM_BLOCK_SIZE
import org.noordawod.kotlin.core.extension.trimOr
import org.noordawod.kotlin.core.extension.trimOrBlank

/**
 * A [Logger] that stores all log messages in a memory buffer.
 *
 * @param environment the runtime environment of the logger
 * @param buffer the memory buffer to store log messages in
 * @param minimumLogType minimum message type to log, defaults to [LogType.INFO]
 */
class MemoryLogger(
  environment: String,
  private val buffer: StringBuffer,
  private val minimumLogType: LogType = LogType.INFO,
) : BaseSimpleLogger(environment) {
  /**
   * Creates a new [MemoryLogger] with a default [StringBuffer].
   *
   * @param environment the runtime environment of the logger
   */
  constructor(environment: String) : this(
    environment = environment,
    buffer = StringBuffer(MEDIUM_BLOCK_SIZE),
  )

  /**
   * Creates a new [MemoryLogger] with a default [StringBuffer].
   *
   * @param environment the runtime environment of the logger
   * @param minimumLogType minimum message type to log, defaults to [LogType.INFO]
   */
  constructor(
    environment: String,
    minimumLogType: LogType = LogType.INFO,
  ) : this(
    environment = environment,
    buffer = StringBuffer(MEDIUM_BLOCK_SIZE),
    minimumLogType = minimumLogType,
  )

  override fun log(
    type: LogType,
    tag: String,
    message: String,
    error: Throwable?,
  ) {
    if (type.lowerOrderThan(minimumLogType, orEqual = false)) {
      return
    }

    val logMessage = logMessage(
      type = type,
      tag = tag,
      message = message,
    )

    buffer.append(logMessage.trimOr("(empty message)"))
    buffer.append('\n')

    if (null != error) {
      buffer.append(stackTraceOf(error))
      buffer.append('\n')
    }
  }

  /**
   * Converts the provided [error] into a printable [String].
   *
   * @param error the error to "stringify"
   */
  @Suppress("MemberVisibilityCanBePrivate")
  fun stackTraceOf(error: Throwable): String = java.io.StringWriter(MEDIUM_BLOCK_SIZE).use {
    error.printStackTrace(java.io.PrintWriter(it))
    "$it".trimOrBlank()
  }

  override fun toString(): String = "$buffer".trimOrBlank()
}
