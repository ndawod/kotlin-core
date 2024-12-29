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
fun CharSequence.withoutSearchModifiers(): String {
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

  return "$string".trim()
}

/**
 * Strips any trailing slash characters
 * (value of [File.separatorChar][java.io.File.separatorChar]) from this [String].
 */
fun CharSequence.withoutTrailingSlash(): String {
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
fun CharSequence.withoutLeadingSlash(): String {
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
fun CharSequence.withoutSlashes() = withoutLeadingSlash().withoutTrailingSlash()

/**
 * Returns a [String] where the specified [chars] appears at its end.
 *
 * @param chars the trailing value to add
 */
fun CharSequence.withTrailing(chars: CharSequence): String {
  val thisLength = length
  val charsLength = chars.length
  val thisString = toString()
  val charsString = "$chars"

  return if (thisLength < charsLength || substring(thisLength - charsLength) != charsString) {
    thisString + charsString
  } else {
    thisString
  }
}

/**
 * Returns a [String] where the specified [extension] appears at its end.
 *
 * @param extension the trailing extension to add
 */
fun CharSequence.withExtension(extension: CharSequence): String {
  val extensionWithDot = if ('.' == extension[0]) extension else ".$extension"

  return withTrailing(extensionWithDot)
}

/**
 * Returns the camelCase representation of this String.
 *
 * @param delimiters a list of delimiter characters, if any, between words
 */
fun CharSequence.camelCase(delimiters: CharSequence = ""): String {
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
fun CharSequence?.trimOrNull(): String? {
  contract {
    returnsNotNull() implies (this@trimOrNull != null)
  }

  val trimmed = this?.trim()

  return if (trimmed.isNullOrEmpty()) null else "$trimmed"
}

/**
 * Trims characters from this string matching [predicate]; returns the trimmed
 * string if non-empty on success, null otherwise.
 *
 * @param predicate the callback that checks whether to trim a character or not
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.trimOrNull(predicate: (Char) -> Boolean): String? {
  contract {
    returnsNotNull() implies (this@trimOrNull != null)
  }

  val trimmed = this?.trim(predicate)

  return if (trimmed.isNullOrEmpty()) null else "$trimmed"
}

/**
 * Trims white spaces from this string; returns the trimmed string if non-empty on
 * success, [fallback] otherwise.
 *
 * @param fallback the fallback value to return
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.trimOr(fallback: CharSequence): String {
  contract {
    returnsNotNull() implies (this@trimOr != null)
  }

  val trimmed = trimOrNull()

  return if (trimmed.isNullOrEmpty()) "$fallback" else trimmed
}

/**
 * Trims white spaces from this string and returns it on success, otherwise returns the
 * empty string.
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.trimOrBlank(): String {
  contract {
    returnsNotNull() implies (this@trimOrBlank != null)
  }

  return trimOr("")
}

/**
 * Returns [fallback] if this String is null, otherwise whether this String is equal to
 * either "1" or "true".
 *
 * @param fallback the fallback value to return
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.toBooleanOr(fallback: Boolean = false): Boolean {
  contract {
    returns(true) implies (this@toBooleanOr != null)
  }

  val value = this?.toString()?.lowercase(java.util.Locale.ENGLISH)

  return if (null == value) fallback else "1" == value || "true" == value
}

/**
 * Converts this String into a [Locale][java.util.Locale] if valid, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("MagicNumber")
fun CharSequence?.toLocaleOrNull(): java.util.Locale? {
  contract {
    returnsNotNull() implies (this@toLocaleOrNull != null)
  }

  return if (null != this && (2 == length || 5 == length)) toLocaleImpl() else null
}

/**
 * Converts this String into a [Locale][java.util.Locale] if valid, [fallback] otherwise.
 *
 * @param fallback the fallback value to return
 */
@OptIn(ExperimentalContracts::class)
@Suppress("MagicNumber")
fun CharSequence?.toLocaleOr(fallback: java.util.Locale): java.util.Locale {
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
fun Collection<CharSequence>?.toLocales(): Collection<java.util.Locale>? {
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
fun List<CharSequence>?.toLocales(): List<java.util.Locale>? {
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
fun Array<CharSequence>?.toLocales(): Array<java.util.Locale>? {
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
fun CharSequence?.toCountryCodeOrNull(): String? {
  contract {
    returnsNotNull() implies (this@toCountryCodeOrNull != null)
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
fun CharSequence.isDefaultLanguage(): Boolean =
  Constants.DEFAULT_LANGUAGE_CODE == toString().lowercase(java.util.Locale.ENGLISH)

/**
 * Given that this is a 2-character language code, returns the new language variation for an
 * old language code.
 */
fun CharSequence.getNewLanguage(): String {
  val language = toString().lowercase(java.util.Locale.ENGLISH)

  for (locale in NewLocaleLanguage.entries) {
    if (language == locale.oldCode) {
      return locale.newCode
    }
  }

  return language
}

/**
 * Normalizes and returns this [String] as a language code.
 */
@Suppress("StringLiteralDuplication")
fun CharSequence.toLanguageCode(): String = "$this".lowercase(java.util.Locale.ENGLISH)

/**
 * Returns parsed parts (account and domain) if this [String] is a valid email,
 * null otherwise.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("ComplexCondition", "MagicNumber")
fun CharSequence?.decodeEmail(): PairOfStrings? {
  contract {
    returnsNotNull() implies (this@decodeEmail != null)
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
fun PairOfStrings?.toEmailOrNull(): String? {
  contract {
    returnsNotNull() implies (this@toEmailOrNull != null)
  }

  val first = this?.first?.trim()
  val second = this?.second?.trim()

  return if (first.isNullOrEmpty() || second.isNullOrEmpty()) null else "$first@$second"
}

/**
 * Returns true if this [String] has a valid email address structure, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isEmail(): Boolean {
  contract {
    returns(true) implies (this@isEmail != null)
  }

  return null != decodeEmail()
}

/**
 * Returns true if this [String] has a valid email address and equals another email
 * address, false otherwise.
 *
 * Pay attention that email addresses are case-insensitive and this function will
 * satisfy that requirement.
 *
 * @param email the other email address
 */
fun CharSequence.isSameEmail(email: CharSequence): Boolean =
  isEmail() && email.isEmail() && toString().trim().equals("$email".trim(), ignoreCase = true)

/**
 * Returns this String as an obfuscated email address on success, null otherwise.
 *
 * @param obfuscateChar the character used to obfuscate the value
 */
fun CharSequence?.obfuscateEmailOrNull(obfuscateChar: Char = DEFAULT_OBFUSCATION_CHAR): String? {
  val emailParts = decodeEmail() ?: return null
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
 * Returns this String as an obfuscated email address on success, the same email otherwise.
 *
 * @param obfuscateChar the character used to obfuscate the value
 */
fun CharSequence.obfuscateEmail(obfuscateChar: Char = DEFAULT_OBFUSCATION_CHAR): String =
  obfuscateEmailOrNull(obfuscateChar) ?: toString()

/**
 * Returns this String as an obfuscated phone number on success, false otherwise.
 *
 * @param obfuscateChar the character used for the obfuscated parts,
 * defaults to [DEFAULT_OBFUSCATION_CHAR]
 * @param separator the character used to separate the phone code and number,
 * defaults to [DEFAULT_PHONE_SEPARATOR]
 */
fun CharSequence?.obfuscatePhoneOrNull(
  obfuscateChar: Char = DEFAULT_OBFUSCATION_CHAR,
  separator: Char = DEFAULT_PHONE_SEPARATOR,
): String? {
  val decodedPhone = this?.decodePhone(separator) ?: return null
  val callingCode = decodedPhone.first
  val phoneNumber = "${decodedPhone.second}"
  val obfuscatedPhoneNumber = phoneNumber.obfuscateStringOr(
    obfuscateChar = obfuscateChar,
    fallback = "$obfuscateChar".repeat(phoneNumber.length.coerceIn(2..6)),
  )

  return "$DEFAULT_PHONE_PREFIX$callingCode$separator$obfuscatedPhoneNumber"
}

/**
 * Returns this String as an obfuscated phone number on success, the same phone otherwise.
 *
 * @param obfuscateChar the character used to obfuscate the value
 */
fun CharSequence.obfuscatePhone(obfuscateChar: Char = DEFAULT_OBFUSCATION_CHAR): String =
  obfuscatePhoneOrNull(obfuscateChar) ?: toString()

/**
 * Returns this String as an obfuscated phone number on success, the same phone otherwise.
 *
 * @param obfuscateChar the character used for the obfuscated parts,
 * defaults to [DEFAULT_OBFUSCATION_CHAR]
 * @param separator the character used to separate the phone code and number,
 * defaults to [DEFAULT_PHONE_SEPARATOR]
 */
fun CharSequence.obfuscatePhone(
  obfuscateChar: Char = DEFAULT_OBFUSCATION_CHAR,
  separator: Char = DEFAULT_PHONE_SEPARATOR,
): String = obfuscatePhoneOrNull(
  obfuscateChar = obfuscateChar,
  separator = separator,
) ?: toString()

/**
 * Returns a [URL][java.net.URL] if this [String] is a valid URL, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.parseUrl(): java.net.URL? {
  contract {
    returnsNotNull() implies (this@parseUrl != null)
  }

  return try {
    java.net.URL(toString())
  } catch (ignored: java.net.MalformedURLException) {
    null
  }
}

/**
 * Returns true if this [String] is a valid URL, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isUrl(): Boolean {
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
fun CharSequence?.decodePhone(separator: Char = DEFAULT_PHONE_SEPARATOR): PairOfIntAndLong? {
  contract {
    returnsNotNull() implies (this@decodePhone != null)
  }

  // Remove any character that is a whitespace or a + sign. What remains are digits and
  // the separator character.
  val phone = trimOrNull {
    it.isWhitespace() || DEFAULT_PHONE_PREFIX == it
  } ?: return null

  // At least one digit for the international calling code, and one for the number, plus
  // one reserved for a separator character.
  if (3 > length) {
    return null
  }

  // A separator must exist.
  val separatorPos = phone.indexOf(separator)

  // The separator position must appear after at least one or more digits, which
  // corresponds with the international calling code (12.34567890).
  if (1 > separatorPos) {
    return null
  }

  // First part is the international calling code.
  val callingCode = phone.substring(0, separatorPos).toIntOrNull()
    ?: return null

  // Second part is the phone number.
  val phoneNumber = phone.substring(1 + separatorPos).toLongOrNull()
    ?: return null

  return callingCode to phoneNumber
}

/**
 * Returns this String after decoding it as a [PairOfIntAndLong] on success, null otherwise.
 *
 * The encoded phone contains a leading '+' sign always, then the international
 * calling code, then a single separator character, and ends with the phone number.
 *
 * For example: `+47.12345678`
 *
 * @param separator the character used to separate the international calling code and number
 */
fun CharSequence?.decodePhoneOrThrow(separator: Char = '.'): PairOfIntAndLong =
  decodePhone(separator)
    ?: error("Phone number format invalid for ($separator) separator: $this")

/**
 * Returns an international phone number for this [PairOfIntAndLong].
 *
 * The returned string contains a leading '+' sign always, then the international
 * calling code, then a single separator character, and ends with the phone number.
 *
 * For example: `+12.34567890`
 *
 * @param separator the character used to separate the international calling code and number
 */
fun PairOfIntAndLong.toPhone(separator: Char = DEFAULT_PHONE_SEPARATOR): String = toPhone(
  separator = separator,
  leadingPlus = true,
)

/**
 * Returns an international phone number for the provided [PairOfIntAndLong] on success,
 * null otherwise.
 *
 * @param separator the character used to separate the international calling code and number
 * @param leadingPlus add a leading `+` to returned phone number
 */
@OptIn(ExperimentalContracts::class)
fun PairOfIntAndLong?.toPhoneOrNull(
  separator: Char? = DEFAULT_PHONE_SEPARATOR,
  leadingPlus: Boolean = false,
): String? {
  val first = this?.first
  val second = this?.second
  val plusChar = if (leadingPlus) "$DEFAULT_PHONE_PREFIX" else ""

  return if (null == separator) "$plusChar$first$second" else "$plusChar$first$separator$second"
}

/**
 * Returns an international phone number for the provided [PairOfIntAndLong].
 *
 * @param separator the character used to separate the international calling code and number
 * @param leadingPlus add a leading `+` to returned phone number
 */
fun PairOfIntAndLong.toPhone(
  separator: Char? = DEFAULT_PHONE_SEPARATOR,
  leadingPlus: Boolean = false,
): String {
  val first = this.first
  val second = this.second
  val plusChar = if (leadingPlus) "$DEFAULT_PHONE_PREFIX" else ""

  return if (null == separator) "$plusChar$first$second" else "$plusChar$first$separator$second"
}

/**
 * Returns true if this [String] is a valid international phone number, false otherwise.
 *
 * @param separator the character used to separate the international calling code and number
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isPhone(separator: Char = DEFAULT_PHONE_SEPARATOR): Boolean {
  contract {
    returns(true) implies (this@isPhone != null)
  }

  return null != decodePhone(separator)
}

/**
 * Converts a [String] value to its [Int] representation.
 *
 * @param opacity apply a constant opacity value (0..255) to the color
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.toColorOrNull(opacity: Int? = null): Int? {
  contract {
    returnsNotNull() implies (this@toColorOrNull != null)
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
fun CharSequence?.normalizedHandle(
  dictionary: java.util.regex.Pattern,
  ignoreCase: Boolean = true,
): String? {
  contract {
    returnsNotNull() implies (this@normalizedHandle != null)
  }

  if (null == this) {
    return null
  }

  val handleTrimmed = if (ignoreCase) toString().trim().lowercase() else trim()

  val handleNormalized = StringBuilder(length.coerceAtLeast(1))
    .apply {
      handleTrimmed.forEach {
        if (dictionary.matcher("$it").matches()) {
          append(it)
        }
      }
    }
    .toString()

  return if (handleNormalized.isEmpty()) null else handleNormalized
}

/**
 * Normalized this string to act as a lowercase key; returns the normalized key on
 * success, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.normalizedKey(): String? {
  contract {
    returnsNotNull() implies (this@normalizedKey != null)
  }

  val keyNormalized = trimOrNull()?.lowercase()

  return if (isNullOrEmpty()) null else keyNormalized
}

/**
 * Returns an [ImageDimension] matching this string on success, null otherwise.
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.imageDimension(): ImageDimension? {
  contract {
    returnsNotNull() implies (this@imageDimension != null)
  }

  val stringNormalized = this?.trim()
  if (stringNormalized.isNullOrEmpty()) {
    return null
  }

  val xPosition = stringNormalized.indexOf('x')
  if (0 > xPosition) {
    return null
  }

  val width = stringNormalized
    .substring(0, xPosition)
    .toIntOrNull()
  if (null == width || 0 > width) {
    return null
  }

  val height = stringNormalized
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
fun CharSequence?.normalizeHostName(): String? {
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
fun CharSequence?.isHostName(): Boolean {
  contract {
    returns(true) implies (this@isHostName != null)
  }

  return null != this && HOST_NAME_PATTERN.matcher(this).matches()
}

/**
 * Normalizes and returns this String a domain name.
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.normalizeDomainName(): String? {
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
fun CharSequence?.isDomainName(): Boolean {
  contract {
    returns(true) implies (this@isDomainName != null)
  }

  return null != this && DOMAIN_NAME_PATTERN.matcher(this).matches()
}

/**
 * Returns the numerical (integer) value of this hexadecimal color on success, null otherwise.
 *
 * The color can be preceded by a `#` character, and can be either represented as a
 * 3-char string (RGB) or 6-char string (RRGGBB).
 *
 * If the string starts with a `#`, it will be stripped.
 */
fun CharSequence.toColor(): Int? {
  val color = trimOrNull { it.isWhitespace() || '#' == it } ?: return null
  val colorLength = color.length

  val colorNormalized = when (colorLength) {
    3 -> {
      val r = color[0]
      val g = color[1]
      val b = color[2]

      "$r$r$g$g$b$b"
    }

    6 -> color
    else -> return null
  }

  return try {
    colorNormalized.toInt(16)
  } catch (_: NumberFormatException) {
    null
  }
}

/**
 * Returns this numeric value as a color string (RRGGBB) value on success, null otherwise.
 *
 * @param withHash whether to prefix the color with a `#` character
 */
fun Int.toColor(withHash: Boolean = true): String? {
  val color = try {
    toString(16)
  } catch (_: NumberFormatException) {
    return null
  }

  return if (withHash) "#$color" else color
}

/**
 * Returns a one-liner from this character sequence, which may contain multiple lines.
 */
@Suppress("StringLiteralDuplication")
fun CharSequence.oneLiner(): String = "$this"
  .replace("\b", "\\b")
  .replace("\t", "\\t")
  .replace("\n", "\\n")
  .replace("\r", "\\r")

/**
 * Returns this character sequence enclosed inside double quotes.
 *
 * Note: If you pass `false` to [withBackslash], then double quotes characters will
 * be replaced with a 2 double quotes characters, which is what's needed for escaping
 * values in a CSV file.
 *
 * @param withBackslash whether to escape double quotes with a backslash, or not
 */
fun CharSequence.withDoubleQuotes(withBackslash: Boolean): String = '"' +
  "$this".replace("\"", if (withBackslash) "\\\"" else "\"\"") + '"'

/**
 * Returns this character sequence enclosed inside single quotes.
 */
fun CharSequence.withSingleQuotes(): String = "'" +
  "$this".replace("'", "\\'") + "'"

/**
 * Returns this [CharSequence] if its length is of a specific value, null otherwise.
 *
 * @param targetLength the requested length
 */
fun CharSequence.withLengthOrNull(targetLength: Int): CharSequence? =
  if (length == targetLength) this else null

private fun CharSequence.toLocaleImpl(): java.util.Locale? {
  val parts = toString().replace("-", "_").split('_')

  return try {
    if (1 < parts.size) java.util.Locale(parts[0], parts[1]) else java.util.Locale(parts[0])
  } catch (ignored: Throwable) {
    null
  }
}

private fun Iterator<CharSequence>.toLocalesImpl(size: Int): Collection<java.util.Locale>? {
  val result = mutableListWith<java.util.Locale>(size)

  for (string in this) {
    val stringNormalized = string.trimOrNull() ?: continue
    val stringParts = stringNormalized
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

@Suppress("KotlinConstantConditions")
private val Int.peekCharactersCount: Int
  get() = when {
    4 < this -> 2
    2 < this -> 1
    else -> 0
  }

private fun CharSequence?.obfuscateString(obfuscateChar: Char): String? {
  val peekCharactersCount = this?.length?.peekCharactersCount ?: return null
  if (0 == peekCharactersCount) {
    return null
  }

  val trailingIndex = length - peekCharactersCount

  return substring(0, peekCharactersCount) +
    "$obfuscateChar".repeat(trailingIndex) +
    substring(trailingIndex)
}

private fun CharSequence.obfuscateStringOr(
  obfuscateChar: Char,
  fallback: CharSequence? = null,
): String = obfuscateString(obfuscateChar) ?: fallback?.toString() ?: toString()

private val HOST_NAME_PATTERN: java.util.regex.Pattern = java.util.regex.Pattern
  .compile("^[a-z0-9-]\$")

private val DOMAIN_NAME_PATTERN: java.util.regex.Pattern = java.util.regex.Pattern
  .compile("^[a-z0-9.-]\$")

private val ISO_COUNTRIES: Array<String> = java.util.Locale.getISOCountries()

private const val DEFAULT_OBFUSCATION_CHAR: Char = '*'
private const val DEFAULT_PHONE_SEPARATOR: Char = '.'
private const val DEFAULT_PHONE_PREFIX: Char = '+'
