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

import org.noordawod.kotlin.core.ASCII_LOCALE
import org.noordawod.kotlin.core.LOG_DATE_FORMAT

/**
 * A [Logger] that simply prints log messages to the console.
 *
 * @param environment the runtime environment of the logger
 */
abstract class BaseSimpleLogger protected constructor(
  environment: String,
) : BaseProxyLogger() {
  private val environmentTag: String = environment.uppercase(ASCII_LOCALE)

  /**
   * Implementation to log an error [message] to standard output and return it.
   *
   * @param tag a tag to identify the origin of the message
   * @param message the message to log
   */
  protected fun logMessage(
    type: LogType,
    tag: String,
    message: String,
  ): String {
    val dateFormatter = java.text.SimpleDateFormat(
      LOG_DATE_FORMAT,
      ASCII_LOCALE,
    )
    val dateTag = "[${dateFormatter.format(java.util.Date())}]"
    val envTag = "[$environmentTag/$tag]"
    val typeTag = "${"$type".uppercase(ASCII_LOCALE)}:"

    return arrayOf(dateTag, envTag, typeTag, message).joinToString(separator = " ")
  }
}
