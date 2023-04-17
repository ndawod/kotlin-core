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
 * Types of log message we support. To keep it simple, only 3 types are defined.
 *
 * @param value a human-friendly short description of the enum entry
 */
enum class LogType(val value: String) {
  /**
   * A log type suitable for information related to debugging. Most messages will use this
   * type.
   */
  INFO("info"),

  /**
   * A log type for warnings, or non-breaking errors. Such situations the app is able to
   * recover from and still produce a meaningful response.
   */
  WARNING("warning"),

  /**
   * A log type for breaking errors. Such situations cause the app to stop abruptly and report
   * an error to the client.
   */
  ERROR("error");

  override fun toString(): String = value
}
