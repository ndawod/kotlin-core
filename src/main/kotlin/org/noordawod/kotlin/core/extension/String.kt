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

@file:Suppress("unused", "TooManyFunctions", "MagicNumber")

package org.noordawod.kotlin.core.extension

import java.io.File
import java.net.MalformedURLException
import java.util.Locale

/**
 * A signature of a [Pair] of two [strings][String].
 */
typealias PairOfStrings = Pair<String, String>

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
 * Trims white spaces from this string; returns the trimmed string if non-empty on
 * success, null otherwise.
 */
fun String?.trimOrNull(): String? {
  val trimmed = this?.trim()

  return if (trimmed.isNullOrEmpty()) null else trimmed
}

/**
 * Trims white spaces from this string; returns the trimmed string if non-empty on
 * success, [fallback] otherwise.
 */
fun String?.trimOr(fallback: String): String {
  val trimmed = trimOrNull()

  return if (trimmed.isNullOrEmpty()) fallback else trimmed
}

/**
 * Trims white spaces from this string and returns it on success, otherwise returns the
 * empty string.
 */
fun String?.trimOrBlank(): String = trimOr("")

/**
 * Converts this String into a [Locale] if valid, null otherwise.
 */
@Suppress("MagicNumber")
fun String?.toLocaleOrNull(): Locale? =
  if (null != this && (2 == length || 5 == length)) toLocaleImpl() else null

/**
 * Converts this String into a [Locale] if valid, [fallback] otherwise.
 */
@Suppress("MagicNumber")
fun String?.toLocaleOr(fallback: Locale): Locale {
  if (null != this && (2 == length || 5 == length)) {
    val locale = toLocaleImpl()
    if (null != locale) {
      return locale
    }
  }
  return fallback
}

/**
 * Converts a [String] to a [Locale] using [Locale.forLanguageTag].
 */
fun String.toLocale(): Locale = Locale.forLanguageTag(this)

/**
 * Returns this String if it's a valid 2-letter country code, null otherwise.
 *
 * @param uppercase whether to return an upper- or lower-case country code
 */
@Suppress("NestedBlockDepth")
fun String?.ensureCountryCode(uppercase: Boolean = true): String? {
  val country = this?.trimOrNull()?.uppercase()

  if (null != country) {
    val isoCountries = Locale.getISOCountries()
    for (isoCountry in isoCountries) {
      if (isoCountry == country) {
        return if (uppercase) country else country.lowercase()
      }
    }
  }

  return null
}

/**
 * Given that this is a 2-character language code, returns the new language variation for an
 * old language code.
 */
fun String.getNewLanguage(): String {
  val language = lowercase(Locale.ENGLISH)
  for (locale in NewLocaleLanguage.values()) {
    if (language == locale.oldCode) {
      return locale.newCode
    }
  }
  return language
}

/**
 * Returns parsed parts (account and domain) if this [String] is a valid email, null
 * if the email address is invalid.
 */
@Suppress("ComplexCondition", "MagicNumber")
fun String?.parseEmail(): PairOfStrings? {
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
 * Returns an email address for the provided [Pair] of strings on success, null otherwise.
 */
fun PairOfStrings?.asEmail(): String? {
  val first = this?.first?.trim()
  val second = this?.second?.trim()

  return if (first.isNullOrEmpty() || second.isNullOrEmpty()) null else "$first@$second"
}

/**
 * Returns true if this [String] has a valid email address structure, false otherwise.
 */
fun String?.isEmail(): Boolean = null != parseEmail()

/**
 * Returns true if this [String] has a valid email address and equals the provided
 * [email], false otherwise. Pay attention that email addresses are case-insensitive and
 * this function will satisfy that requirement.
 */
fun String.isSameEmail(email: String): Boolean =
  isEmail() && email.isEmail() && trim().equals(email.trim(), ignoreCase = true)

/**
 * Returns a [URL][java.net.URL] if this [String] is a valid URL, null otherwise.
 */
fun String?.parseUrl(): java.net.URL? = try {
  java.net.URL(this)
} catch (ignored: MalformedURLException) {
  null
}

/**
 * Returns true if this [String] is a valid URL, false otherwise.
 */
fun String?.isUrl(): Boolean = null != parseUrl()

/**
 * Converts a [String] value to its [Int] representation.
 *
 * @param opacity apply a constant opacity value (0..255) to the color
 */
fun String?.toColor(opacity: Int? = null): Int? {
  var color = this?.trimOrNull() ?: return null

  if ('#' == color[0]) {
    color = color.substring(1)
  }

  var opacityValue: Int
  val redValue: Int
  val greenValue: Int
  val blueValue: Int

  when (color.length) {
    3 -> {
      opacityValue = 0xff
      redValue = Integer.parseInt("${color[0]}${color[0]}", 16)
      greenValue = Integer.parseInt("${color[1]}${color[1]}", 16)
      blueValue = Integer.parseInt("${color[2]}${color[2]}", 16)
    }
    6 -> {
      opacityValue = 0xff
      redValue = Integer.parseInt(color.substring(0, 2), 16)
      greenValue = Integer.parseInt(color.substring(2, 2), 16)
      blueValue = Integer.parseInt(color.substring(4, 2), 16)
    }
    8 -> {
      opacityValue = Integer.parseInt(color.substring(0, 2), 16)
      redValue = Integer.parseInt(color.substring(2, 2), 16)
      greenValue = Integer.parseInt(color.substring(4, 2), 16)
      blueValue = Integer.parseInt(color.substring(6, 2), 16)
    }
    else -> return null
  }

  if (null != opacity && opacity in 0..255) {
    opacityValue = opacity
  }

  return (opacityValue shl 24) + (redValue shl 16) + (greenValue shl 8) + blueValue
}

private fun String.toLocaleImpl(): Locale? {
  val parts = replace("-", "_").split('_')
  return try {
    if (1 < parts.size) Locale(parts[0], parts[1]) else Locale(parts[0])
  } catch (ignored: Throwable) {
    null
  }
}
