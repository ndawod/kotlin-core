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

package org.noordawod.kotlin.core.logger

/**
 * A simple provider for logging purposes.
 */
interface Logger {
  /**
   * A general-purpose method to log a [message] of the specified [type], with an optional
   * [error].
   *
   * @param type the type of log message
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   * @param error optional error that accompanies the message
   */
  fun log(
    type: LogType,
    tag: String,
    message: String,
    error: Throwable? = null,
  )

  /**
   * Logs a [message] of [type INFO][LogType.INFO].
   *
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   */
  fun info(tag: String, message: String)

  /**
   * Logs a [message] of [type INFO][LogType.INFO] with an [error].
   *
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   * @param error an error that accompanies the message
   */
  fun info(tag: String, message: String, error: Throwable)

  /**
   * Logs a [message] of [type WARNING][LogType.WARNING].
   *
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   */
  fun warning(tag: String, message: String)

  /**
   * Logs a [message] of [type WARNING][LogType.WARNING] with an [error].
   *
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   * @param error an error that accompanies the message
   */
  fun warning(tag: String, message: String, error: Throwable)

  /**
   * Logs a message of [type ERROR][LogType.ERROR] with an [error].
   *
   * @param tag a tag to identify the origin of the message
   * @param error an error that accompanies the message
   */
  fun error(tag: String, error: Throwable) {
    error(tag, error.message ?: "Unknown error", error)
  }

  /**
   * Logs a [message] of [type ERROR][LogType.ERROR].
   *
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   */
  fun error(
    tag: String,
    message: String
  ) {
    error(tag, RuntimeException(message))
  }

  /**
   * Logs a [message] of [type ERROR][LogType.ERROR] with an [error].
   *
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   * @param error optional error that accompanies the message
   */
  fun error(tag: String, message: String, error: Throwable)
}
