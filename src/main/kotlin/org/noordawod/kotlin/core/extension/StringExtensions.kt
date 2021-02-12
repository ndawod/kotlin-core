/*
 * The MIT License
 *
 * Copyright 2020 Noor Dawod. All rights reserved.
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

package org.noordawod.kotlin.core.extension

import java.io.File
import java.util.Locale

/**
 * Strips any trailing slash characters (value of [File.separatorChar]) from this [String].
 */
fun String.withoutTrailingSlash(): String {
  var endIndex = length
  while (0 < endIndex && File.separatorChar == this[endIndex - 1]) {
    endIndex--
  }
  return substring(0, endIndex)
}

/**
 * Strips any leading slash characters (value of [File.separatorChar]) from this [String].
 */
fun String.withoutLeadingSlash(): String {
  var startIndex = 0
  val length = length
  while (length > startIndex && File.separatorChar == this[startIndex]) {
    startIndex++
  }
  return substring(startIndex)
}

/**
 * Strips any leading and trailing slash characters (value of [File.separatorChar]) from
 * this [String].
 */
fun String.withoutSlashes() = withoutLeadingSlash().withoutTrailingSlash()

/**
 * Returns a [String] where the specified [string] appears at its end.
 */
fun String.withTrailing(string: String): String {
  val length = this.length
  val stringLength = string.length
  return if (length < stringLength || string != substring(length - stringLength)) {
    "$this$string"
  } else {
    this
  }
}

/**
 * Returns a [String] where the specified [extension] appears at its end.
 */
fun String.withExtension(extension: String): String {
  val extensionWithDot = if ('.' == extension[0]) extension else ".$extension"
  return withTrailing(extensionWithDot)
}

/**
 * Converts a [String] to a [Locale] using [Locale.forLanguageTag].
 */
fun String.toLocale(): Locale = Locale.forLanguageTag(this)

/**
 * Returns parsed parts (account and domain) if this [String] is a valid email, null
 * if the email address is invalid.
 */
@Suppress("ComplexCondition", "MagicNumber")
fun String?.parseEmail(): Pair<String, String>? {
  if (!this.isNullOrBlank()) {
    val email = this.trim()
    val length = email.length
    val atPos = email.indexOf('@') + 1

    // Basic check to ensure that the email address has at least 6 characters ("a@b.co") and
    // that it doesn't contain invalid characters.
    if (
      1 < atPos && 5 < length && (
        -1 == email.indexOf(' ') ||
          -1 == email.indexOf(10.toChar()) ||
          -1 == email.indexOf(13.toChar()) ||
          -1 == email.indexOf(8.toChar()) ||
          -1 == email.indexOf(0.toChar()) ||
          -1 == email.indexOf(9.toChar())
        )
    ) {
      // Let's ensure that there is no @ after the first one, and there is at least a
      // dot within the email's domain.
      val dotPos = email.indexOf('.', atPos)

      // Ensure that there are no dots appearing at the end of the email, and that
      // the last dot appears at least 2 characters from the end.
      if (-1 == email.indexOf('@', atPos) && atPos < dotPos && length > 2 + dotPos) {
        val account = email.substring(0, atPos - 1)
        val domainName = email.substring(atPos)
        return account to domainName
      }
    }
  }
  return null
}

/**
 * Returns true if this [String] has a valid email address structure, false otherwise.
 */
@Suppress("ComplexCondition", "MagicNumber")
fun String?.isEmail(): Boolean = null != parseEmail()

/**
 * Returns true if this [String] has a valid email address and equals the provided
 * [email], false otherwise. Pay attention that email addresses are case-insensitive and
 * this function will satisfy that requirement.
 */
fun String.isSameEmail(email: String): Boolean =
  isEmail() && email.isEmail() && trim().equals(email.trim(), ignoreCase = true)
