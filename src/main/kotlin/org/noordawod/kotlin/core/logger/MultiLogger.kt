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

@file:Suppress("unused")

package org.noordawod.kotlin.core.logger

/**
 * A [Logger] that proxies each log message to a list of loggers.
 *
 * @param loggers the list of loggers
 * @param minimumLogType minimum message type to log, defaults to [LogType.INFO]
 */
class MultiLogger(
  private val loggers: Iterable<Logger>,
  private val minimumLogType: LogType = LogType.INFO,
) : BaseProxyLogger() {
  /**
   * A convenient constructor using a vararg instead of an [Iterable].
   *
   * @param loggers the list of loggers
   */
  constructor(vararg loggers: Logger) : this(loggers.toList())

  override fun log(
    type: LogType,
    tag: String,
    message: String,
    error: Throwable?,
  ) {
    if (type.lowerOrderThan(minimumLogType, orEqual = false)) {
      return
    }

    for (logger in loggers) {
      logger.log(
        type = type,
        tag = tag,
        message = message,
        error = error,
      )
    }
  }
}
