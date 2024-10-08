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

@file:Suppress("unused", "MagicNumber")

package org.noordawod.kotlin.core.extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import org.noordawod.kotlin.core.Constants
import org.noordawod.kotlin.core.util.ImageDimension

/**
 * A signature of a [Pair] of two [String]s.
 */
typealias PairOfStrings = Pair<String, String>

/**
 * A signature of a [Pair] of an [Int] and a [Long].
 */
typealias PairOfIntAndLong = Pair<Int, Long>

/**
 * Strips search modifiers (`+`, `-`, `<`, `>`, `"`) from this string, and returns it.
 */
@Suppress("ComplexCondition")
fun String.withoutSearchModifiers(): String {
  var string = trim()

  if (
    string.startsWith('+') ||
    string.startsWith('-') ||
    string.startsWith('<') ||
    string.startsWith('>')
  ) {
    string = string.substring(1)
  }

  val length = string.length
  if (
    '"' == string[0] && '"' == string[length - 1] ||
    '\'' == string[0] && '\'' == string[length - 1]
  ) {
    string = string.substring(1, length - 1)
  }

  return string.trim()
}

/**
 * Strips any trailing slash characters
 * (value of [File.separatorChar][java.io.File.separatorChar]) from this [String].
 */
fun String.withoutTrailingSlash(): String {
  var endIndex = length
  while (0 < endIndex && java.io.File.separatorChar == this[endIndex - 1]) {
    endIndex--
  }

  return substring(0, endIndex)
}

/**
 * Strips any leading slash characters
 * (value of [File.separatorChar][java.io.File.separatorChar]) from this [String].
 */
fun String.withoutLeadingSlash(): String {
  var startIndex = 0
  val length = length
  while (length > startIndex && java.io.File.separatorChar == this[startIndex]) {
    startIndex++
  }

  return substring(startIndex)
}

/**
 * Strips any leading and trailing slash characters
 * (value of [File.separatorChar][java.io.File.separatorChar]) from this [String].
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
 * Returns the camelCase representation of this String.
 *
 * @param delimiters a list of delimiter characters, if any, between words
 */
fun String.camelCase(delimiters: String = ""): String {
  val length = this.length
  var shouldConvertNextCharToLower = true
  var idx = -1

  val builder = StringBuilder(length)

  while (length > ++idx) {
    val currentChar = this[idx]

    if (-1 < delimiters.indexOf("$currentChar")) {
      shouldConvertNextCharToLower = false
    } else if (shouldConvertNextCharToLower) {
      builder.append(Character.toLowerCase(currentChar))
    } else {
      builder.append(Character.toUpperCase(currentChar))
      shouldConvertNextCharToLower = true
    }
  }

  return builder.toString()
}

/**
 * Trims white spaces from this string; returns the trimmed string if non-empty on
 * success, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.trimOrNull(): String? {
  contract {
    returnsNotNull() implies (this@trimOrNull != null)
  }

  val trimmed = this?.trim()

  return if (trimmed.isNullOrEmpty()) null else trimmed
}

/**
 * Trims characters from this string matching [predicate]; returns the trimmed
 * string if non-empty on success, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.trimOrNull(predicate: (Char) -> Boolean): String? {
  contract {
    returnsNotNull() implies (this@trimOrNull != null)
  }

  val trimmed = this?.trim(predicate)

  return if (trimmed.isNullOrEmpty()) null else trimmed
}

/**
 * Trims white spaces from this string; returns the trimmed string if non-empty on
 * success, [fallback] otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.trimOr(fallback: String): String {
  contract {
    returnsNotNull() implies (this@trimOr != null)
  }

  val trimmed = trimOrNull()

  return if (trimmed.isNullOrEmpty()) fallback else trimmed
}

/**
 * Trims white spaces from this string and returns it on success, otherwise returns the
 * empty string.
 */
@OptIn(ExperimentalContracts::class)
fun String?.trimOrBlank(): String {
  contract {
    returnsNotNull() implies (this@trimOrBlank != null)
  }

  return trimOr("")
}

/**
 * Returns [fallback] if this String is null, otherwise whether this String is equal to
 * either "1" or "true".
 *
 * @param fallback default value when this String is null
 */
@OptIn(ExperimentalContracts::class)
fun String?.toBooleanOr(fallback: Boolean = false): Boolean {
  contract {
    returns(true) implies (this@toBooleanOr != null)
  }

  val value = this?.lowercase(java.util.Locale.ENGLISH)

  return if (null == value) fallback else "1" == value || "true" == value
}

/**
 * Converts this String into a [Locale][java.util.Locale] if valid, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("MagicNumber")
fun String?.toLocaleOrNull(): java.util.Locale? {
  contract {
    returnsNotNull() implies (this@toLocaleOrNull != null)
  }

  return if (null != this && (2 == length || 5 == length)) toLocaleImpl() else null
}

/**
 * Converts this String into a [Locale][java.util.Locale] if valid, [fallback] otherwise.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("MagicNumber")
fun String?.toLocaleOr(fallback: java.util.Locale): java.util.Locale {
  contract {
    returnsNotNull() implies (this@toLocaleOr != null)
  }

  return toLocaleOrNull() ?: fallback
}

/**
 * Returns a collection of [Locale][java.util.Locale]s matching these strings on success,
 * null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Collection<String>?.toLocales(): Collection<java.util.Locale>? {
  contract {
    returnsNotNull() implies (this@toLocales != null)
  }

  return if (isNullOrEmpty()) null else iterator().toLocalesImpl(size)
}

/**
 * Returns a collection of [Locale][java.util.Locale]s matching these strings on success,
 * null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun List<String>?.toLocales(): List<java.util.Locale>? {
  contract {
    returnsNotNull() implies (this@toLocales != null)
  }

  return if (isNullOrEmpty()) {
    null
  } else {
    iterator().toLocalesImpl(size) as List<java.util.Locale>
  }
}

/**
 * Returns an array of [Locale][java.util.Locale]s matching these strings on success,
 * null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun Array<String>?.toLocales(): Array<java.util.Locale>? {
  contract {
    returnsNotNull() implies (this@toLocales != null)
  }

  return if (isNullOrEmpty()) null else iterator().toLocalesImpl(size)?.toTypedArray()
}

/**
 * Returns this String if it's a valid 2-letter country code, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("NestedBlockDepth")
fun String?.ensureCountryCode(): String? {
  contract {
    returnsNotNull() implies (this@ensureCountryCode != null)
  }

  val country = trimOrNull()?.uppercase(java.util.Locale.ENGLISH)

  if (null != country) {
    for (isoCountry in ISO_COUNTRIES) {
      if (isoCountry.equals(country, ignoreCase = true)) {
        return country
      }
    }
  }

  return null
}

/**
 * Returns true if this [String] is the [default language][Constants.DEFAULT_LANGUAGE_CODE],
 * false otherwise.
 */
fun String.isDefaultLanguage(): Boolean =
  Constants.DEFAULT_LANGUAGE_CODE == lowercase(java.util.Locale.ENGLISH)

/**
 * Given that this is a 2-character language code, returns the new language variation for an
 * old language code.
 */
fun String.getNewLanguage(): String {
  val language = lowercase(java.util.Locale.ENGLISH)

  for (locale in NewLocaleLanguage.entries) {
    if (language == locale.oldCode) {
      return locale.newCode
    }
  }

  return language
}

/**
 * Returns parsed parts (account and domain) if this [String] is a valid email,
 * null otherwise.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("ComplexCondition", "MagicNumber")
fun String?.parseEmail(): PairOfStrings? {
  contract {
    returnsNotNull() implies (this@parseEmail != null)
  }

  val email = trimOrNull() ?: return null

  val length = email.length
  val atPlus1Pos = 1 + email.indexOf('@')

  // Check that the email address has at least 6 characters ("a@b.co").
  if (2 > atPlus1Pos || 6 > length) {
    return null
  }

  // Only one '@' in the email address.
  if (-1 != email.indexOf('@', atPlus1Pos)) {
    return null
  }

  // Check that the email address doesn't contain invalid characters.
  if (
    -1 != email.indexOf(' ') ||
    -1 != email.indexOf(0.toChar()) ||
    -1 != email.indexOf(8.toChar()) ||
    -1 != email.indexOf(9.toChar()) ||
    -1 != email.indexOf(10.toChar()) ||
    -1 != email.indexOf(13.toChar())
  ) {
    return null
  }

  // At least one dot must exist after, but not adjacent to, the "@" sign, and it must be
  // 2 characters away from the end.
  val dotPos = email.lastIndexOf(".")
  if (1 > dotPos || length < dotPos + 3) {
    return null
  }

  val account = email.substring(0, atPlus1Pos - 1)
  val domainName = email.substring(atPlus1Pos)

  return account to domainName
}

/**
 * Returns an email address for the provided [PairOfStrings] of strings on success,
 * null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun PairOfStrings?.asEmail(): String? {
  contract {
    returnsNotNull() implies (this@asEmail != null)
  }

  val first = this?.first?.trim()
  val second = this?.second?.trim()

  return if (first.isNullOrEmpty() || second.isNullOrEmpty()) null else "$first@$second"
}

/**
 * Returns true if this [String] has a valid email address structure, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.isEmail(): Boolean {
  contract {
    returns(true) implies (this@isEmail != null)
  }

  return null != parseEmail()
}

/**
 * Returns true if this [String] has a valid email address and equals the provided
 * [email], false otherwise. Pay attention that email addresses are case-insensitive and
 * this function will satisfy that requirement.
 */
fun String.isSameEmail(email: String): Boolean =
  isEmail() && email.isEmail() && trim().equals(email.trim(), ignoreCase = true)

/**
 * Returns this String as an obfuscated email address.
 */
fun String?.obfuscateEmail(obfuscateChar: Char = '*'): String? {
  val emailParts = parseEmail() ?: return null
  val account = emailParts.first.obfuscateString(obfuscateChar)
  val domain = emailParts.second
  val domainParts = domain.split(".", ignoreCase = true)
  val obfuscatedDomain = mutableListWith<String>(domainParts.size)

  if (1 < domainParts.size) {
    var idx = -1

    while (domainParts.size - 1 > ++idx) {
      val part = domainParts[idx]
      val obfuscatedPart = part.obfuscateStringOr(
        obfuscateChar = obfuscateChar,
        fallback = "*".repeat(part.length.coerceIn(2..4)),
      )

      obfuscatedDomain.add(obfuscatedPart)
    }

    obfuscatedDomain.add(domainParts[idx])
  }

  return "$account@${obfuscatedDomain.joinToString(".")}"
}

/**
 * Returns this String as an obfuscated phone number.
 *
 * @param obfuscateChar the character used for the obfuscated parts, defaults to '*'
 * @param separator the character used to separate the phone code and number, defaults to '.'
 */
fun String?.obfuscatePhone(
  obfuscateChar: Char = '*',
  separator: Char = '.',
): String? {
  val decodedPhone = this?.parsePhone(separator) ?: return null
  val callingCode = decodedPhone.first
  val phoneNumber = "${decodedPhone.second}"
  val obfuscatedPhoneNumber = phoneNumber.obfuscateStringOr(
    obfuscateChar = obfuscateChar,
    fallback = "$obfuscateChar".repeat(phoneNumber.length.coerceIn(2..6)),
  )

  return "+$callingCode$separator$obfuscatedPhoneNumber"
}

/**
 * Returns a [URL][java.net.URL] if this [String] is a valid URL, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.parseUrl(): java.net.URL? {
  contract {
    returnsNotNull() implies (this@parseUrl != null)
  }

  return try {
    java.net.URL(this)
  } catch (ignored: java.net.MalformedURLException) {
    null
  }
}

/**
 * Returns true if this [String] is a valid URL, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.isUrl(): Boolean {
  contract {
    returns(true) implies (this@isUrl != null)
  }

  return null != parseUrl()
}

/**
 * Returns parsed parts (international calling code and phone number) if this [String]
 * is a valid phone number, null otherwise.
 *
 * Note: The function can treat strings with or without a leading '+' sign.
 *
 * @param separator the character used to separate the international calling code and number
 */
@OptIn(ExperimentalContracts::class)
@Suppress("ComplexCondition", "MagicNumber")
fun String?.parsePhone(separator: Char = '.'): PairOfIntAndLong? {
  contract {
    returnsNotNull() implies (this@parsePhone != null)
  }

  val phone = trimOrNull {
    it.isWhitespace() || '+' == it
  } ?: return null

  // A separator must exist.
  val separatorPos = phone.indexOf(separator)

  // The separator position must appear after at least 2 digits (international calling code).
  if (2 > separatorPos) {
    return null
  }

  // First part is the international calling code.
  val callingCode = phone.substring(
    startIndex = 0,
    endIndex = separatorPos,
  ).toIntOrNull() ?: return null

  // Second part is the phone number.
  val phoneNumber = phone.substring(1 + separatorPos).toLongOrNull() ?: return null

  return callingCode to phoneNumber
}

/**
 * Returns an international phone number for the provided [PairOfIntAndLong] on success,
 * null otherwise.
 *
 * @param separator the character used to separate the international calling code and number
 * @param leadingPlus add a leading `+` to returned phone number
 */
@OptIn(ExperimentalContracts::class)
fun PairOfIntAndLong?.asPhone(
  separator: Char? = '.',
  leadingPlus: Boolean = false,
): String {
  contract {
    returnsNotNull() implies (this@asPhone != null)
  }

  val first = this?.first
  val second = this?.second
  val plusChar = if (leadingPlus) "+" else ""

  return if (null == separator) "$plusChar$first$second" else "$plusChar$first$separator$second"
}

/**
 * Returns true if this [String] is a valid international phone number, false otherwise.
 *
 * @param separator the character used to separate the international calling code and number
 */
@OptIn(ExperimentalContracts::class)
fun String?.isPhone(separator: Char = '.'): Boolean {
  contract {
    returns(true) implies (this@isPhone != null)
  }

  return null != parsePhone(separator)
}

/**
 * Converts a [String] value to its [Int] representation.
 *
 * @param opacity apply a constant opacity value (0..255) to the color
 */
@OptIn(ExperimentalContracts::class)
fun String?.toColor(opacity: Int? = null): Int? {
  contract {
    returnsNotNull() implies (this@toColor != null)
  }

  var color = trimOrNull() ?: return null

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

/**
 * Normalized this string to act as a handle having the allowed dictionary characters; returns
 * the normalized handle on success, null otherwise.
 *
 * If the string includes other characters, they'll be removed.
 *
 * @param dictionary allowed list of characters expressed as a regexp pattern
 * @param ignoreCase whether to ignore the string's case, defaults to lower casing it
 */
@OptIn(ExperimentalContracts::class)
fun String?.normalizedHandle(
  dictionary: java.util.regex.Pattern,
  ignoreCase: Boolean = true,
): String? {
  contract {
    returnsNotNull() implies (this@normalizedHandle != null)
  }

  if (null == this) {
    return null
  }

  var normalizedHandle = if (ignoreCase) trim().lowercase() else trim()

  normalizedHandle = StringBuilder(length.coerceAtLeast(1))
    .apply {
      normalizedHandle.forEach {
        if (dictionary.matcher("$it").matches()) {
          append(it)
        }
      }
    }
    .toString()

  return normalizedHandle.ifEmpty { null }
}

/**
 * Normalized this string to act as a lowercase key; returns the normalized key on
 * success, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.normalizedKey(): String? {
  contract {
    returnsNotNull() implies (this@normalizedKey != null)
  }

  val normalizedKey = trimOrNull()?.lowercase()

  return if (isNullOrEmpty()) null else normalizedKey
}

/**
 * Returns an [ImageDimension] matching this string on success, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.imageDimension(): ImageDimension? {
  contract {
    returnsNotNull() implies (this@imageDimension != null)
  }

  val normalizedString = this?.trim()
  if (normalizedString.isNullOrEmpty()) {
    return null
  }

  val xPosition = normalizedString.indexOf('x')
  if (0 > xPosition) {
    return null
  }

  val width = normalizedString
    .substring(0, xPosition)
    .toIntOrNull()
  if (null == width || 0 > width) {
    return null
  }

  val height = normalizedString
    .substring(1 + xPosition)
    .toIntOrNull()
  if (null == height || 0 > height) {
    return null
  }

  return ImageDimension(width, height)
}

/**
 * Normalizes and returns this String a host name.
 */
@OptIn(ExperimentalContracts::class)
fun String?.normalizeHostName(): String? {
  contract {
    returnsNotNull() implies (this@normalizeHostName != null)
  }

  return normalizedHandle(
    dictionary = HOST_NAME_PATTERN,
    ignoreCase = true,
  )
}

/**
 * Returns true if this String is a valid host name, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.isHostName(): Boolean {
  contract {
    returns(true) implies (this@isHostName != null)
  }

  return null != this && HOST_NAME_PATTERN.matcher(this).matches()
}

/**
 * Normalizes and returns this String a domain name.
 */
@OptIn(ExperimentalContracts::class)
fun String?.normalizeDomainName(): String? {
  contract {
    returnsNotNull() implies (this@normalizeDomainName != null)
  }

  return normalizedHandle(
    dictionary = DOMAIN_NAME_PATTERN,
    ignoreCase = true,
  )
}

/**
 * Returns true if this String is a valid domain name, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun String?.isDomainName(): Boolean {
  contract {
    returns(true) implies (this@isDomainName != null)
  }

  return null != this && DOMAIN_NAME_PATTERN.matcher(this).matches()
}

private fun String.toLocaleImpl(): java.util.Locale? {
  val parts = replace("-", "_").split('_')

  return try {
    if (1 < parts.size) java.util.Locale(parts[0], parts[1]) else java.util.Locale(parts[0])
  } catch (ignored: Throwable) {
    null
  }
}

private fun Iterator<String>.toLocalesImpl(size: Int): Collection<java.util.Locale>? {
  val result = mutableListWith<java.util.Locale>(size)

  for (string in this) {
    val normalizedString = string.trimOrNull() ?: continue
    val stringParts = normalizedString
      .replace('_', '-')
      .split('-')
      .ifEmpty { null } ?: continue

    result.add(
      java.util.Locale(
        stringParts[0],
        if (1 < stringParts.size) stringParts[1] else "",
      ),
    )
  }

  return if (result.isEmpty()) null else result
}

private val Int.peekCharactersCount: Int
  get() = when {
    4 < this -> 2
    2 < this -> 1
    else -> 0
  }

private fun String?.obfuscateString(obfuscateChar: Char): String? {
  val peekCharactersCount = this?.length?.peekCharactersCount ?: return null
  if (0 == peekCharactersCount) {
    return null
  }

  val trailingIndex = length - peekCharactersCount

  return substring(0, peekCharactersCount) +
    "$obfuscateChar".repeat(trailingIndex) +
    substring(trailingIndex)
}

private fun String.obfuscateStringOr(
  obfuscateChar: Char,
  fallback: String? = null,
): String = obfuscateString(obfuscateChar) ?: fallback ?: this

private val HOST_NAME_PATTERN: java.util.regex.Pattern =
  java.util.regex.Pattern.compile("^[a-z0-9-]\$")

private val DOMAIN_NAME_PATTERN: java.util.regex.Pattern =
  java.util.regex.Pattern.compile("^[a-z0-9.-]\$")

private val ISO_COUNTRIES: Array<String> =
  java.util.Locale.getISOCountries()
