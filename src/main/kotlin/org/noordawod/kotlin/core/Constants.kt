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

@file:Suppress("unused", "MagicNumber", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core

import org.noordawod.kotlin.core.extension.MILLIS_IN_1_SECOND

/**
 * Common constants and values that can be reused in multiple scenarios.
 */
object Constants {
  /**
   * Default timeout when connecting to network services.
   */
  const val DEFAULT_CONNECTION_TIMEOUT: Long = 10 * MILLIS_IN_1_SECOND

  /**
   * Default number of items to initial an array or list with.
   */
  const val DEFAULT_LIST_CAPACITY: Int = 25

  /**
   * Capacity of a medium-sized byte buffer.
   */
  const val MEDIUM_BLOCK_SIZE: Int = 1024

  /**
   * Capacity of a large-sized byte buffer.
   */
  const val LARGE_BLOCK_SIZE: Int = 4096

  /**
   * Default width of a line of text.
   */
  const val DEFAULT_LINE_WIDTH: Int = 80

  /**
   * The [java.util.Locale] considered as default or fallback.
   */
  val DEFAULT_LOCALE: java.util.Locale = java.util.Locale.US

  /**
   * The default language code (`en`).
   */
  val DEFAULT_LANGUAGE_CODE: String = DEFAULT_LOCALE.language.lowercase(java.util.Locale.ENGLISH)

  /**
   * The default country code (`US`).
   */
  val DEFAULT_COUNTRY_CODE: String = DEFAULT_LOCALE.country.uppercase(java.util.Locale.ENGLISH)

  /**
   * A list of characters that includes white spaces.
   */
  val WHITE_SPACES: CharArray = charArrayOf(' ', '\r', '\n', '\t')

  /**
   * A list of characters that includes white spaces and the slash ("/") character.
   */
  val WHITE_SPACES_WITH_SLASH_CHARS: CharArray = WHITE_SPACES + charArrayOf('/')

  /**
   * Matches one or more white-space characters.
   */
  val WHITE_SPACES_PATTERN: java.util.regex.Pattern =
    java.util.regex.Pattern.compile("\\s+")
}
