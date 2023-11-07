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

@file:Suppress("unused", "MatchingDeclarationName")

package org.noordawod.kotlin.core.extension

import java.util.Locale

/**
 * Returns the String representation of this [Locale].
 */
fun Locale.normalizedLocale(): String = toLanguageTag().lowercase()

/**
 * Returns the String representation of this [Locale].
 */
fun Locale.stringify(): String = toLanguageTag()

/**
 * Returns the lowercase String representation of this [Locale].
 */
fun Locale.lowercase(): String = stringify().lowercase()

/**
 * Returns the uppercase String representation of this [Locale].
 */
fun Locale.uppercase(): String = stringify().uppercase()

/**
 * Returns the 2-character language code for this [Locale] with the old language codes converted
 * to their new variations.
 */
fun Locale.getNewLanguage(): String = language.getNewLanguage()

/**
 * Returns the value of [Locale.toString] where the old language codes are converted to their
 * new variations.
 */
fun Locale.toNewString(): String {
  val localeString = toString()
  val lowerCaseLocaleString = localeString.lowercase(Locale.ENGLISH)
  for (locale in NewLocaleLanguage.values()) {
    if (lowerCaseLocaleString == locale.oldCode) {
      return locale.newCode
    }
    if (
      lowerCaseLocaleString.startsWith("${locale.oldCode}_") ||
      lowerCaseLocaleString.startsWith("${locale.oldCode}-")
    ) {
      return "${locale.newCode}${localeString.substring(locale.oldCode.length)}"
    }
  }
  return localeString
}

/**
 * Checks whether two [Locale]s have the same language regardless of countries.
 */
fun Locale.sameLanguageAs(other: Locale) = sameLanguageAs(other.language)

/**
 * Checks whether this [Locale]'s language is the same as the specified [other] language.
 */
fun Locale.sameLanguageAs(other: String) =
  language.getNewLanguage().equals(other.getNewLanguage(), ignoreCase = true)

/**
 * Checks whether this [Locale]'s country is the same as the specified [other] country.
 */
fun Locale.sameCountryAs(other: String) = country.equals(other, ignoreCase = true)

/**
 * Checks whether the writing direction of this [Locale]'s language is right-to-left.
 */
fun Locale.isRightToLeft(): Boolean = rtlLanguages.contains(stripExtensions().language)

/**
 * Checks whether the writing direction of this [Locale]'s language is left-to-right.
 */
fun Locale.isLeftToRight(): Boolean = !isRightToLeft()

/**
 * Returns the writing direction for this [Locale]'s language ("rtl", "ltr").
 */
fun Locale.direction(): String = if (isRightToLeft()) "rtl" else "ltr"

/**
 * Returns the writing direction's starting alignment for this [Locale]'s
 * language ("right", "left").
 */
fun Locale.startAlignment(): String = if (isRightToLeft()) "right" else "left"

/**
 * Returns the writing direction's ending alignment for this [Locale]'s
 * language ("right", "left").
 */
fun Locale.endAlignment(): String = if (isRightToLeft()) "left" else "right"

/**
 * Attempts to guess the country code based on the language (this String).
 */
fun String?.toCountryCode(): String? {
  val language = trimOrNull()?.lowercase(Locale.ENGLISH) ?: return null

  return LANGUAGES_IN_IMPERIAL_COUNTRIES[language]
}

/**
 * Maps old language codes to new ones. This follows the ISO standard for 2-letter languages.
 *
 * @param oldCode the old language code
 * @param newCode the new language code
 */
@Suppress("MemberVisibilityCanBePrivate")
internal enum class NewLocaleLanguage(val oldCode: String, val newCode: String) {
  HEBREW(
    oldCode = "iw",
    newCode = "he",
  ),
  INDONESIAN(
    oldCode = "in",
    newCode = "id",
  ),
  YIDDISH(
    oldCode = "ji",
    newCode = "yi",
  ),
  ;

  companion object {
    /**
     * Attempts to decode a language [code] and match it against [oldCode] or [newCode],
     * returns the matching [NewLocaleLanguage] on success, null otherwise.
     *
     * @param code the code value to match
     */
    fun decode(code: Any?): NewLocaleLanguage? {
      if (null != code) {
        val normalizedCode = "$code".lowercase(Locale.ENGLISH)
        for (locale in values()) {
          if (locale.oldCode == normalizedCode || locale.newCode == normalizedCode) {
            return locale
          }
        }
      }
      return null
    }
  }
}

// The list of languages used in countries that use the imperial measurement system.
// This is obtained from: https://stackoverflow.com/a/76145806
private val IMPERIAL_COUNTRIES = listOf(
  "US",
  "LR",
  "MM",
  "BS",
  "BZ",
  "KY",
  "PW",
  "GB",
  "UK",
)

// The list of languages used in countries that use the imperial measurement system.
// This is obtained from: https://stackoverflow.com/a/76145806
private val LANGUAGES_IN_IMPERIAL_COUNTRIES = mapOf(
  // Burmese, Myanmar (Burma)
  "my" to "MM",
  "mya" to "MM",
  "bur" to "MM",

  // Palauan, Palau
  "pau" to "PW",
)

/**
 * List of right-to-left languages.
 */
private val rtlLanguages: Set<String> by lazy {
  mutableSetOf<String>().apply {
    add(Locale("ar").stripExtensions().language) // Arabic
    add(Locale("dv").stripExtensions().language) // Divehi
    add(Locale("fa").stripExtensions().language) // Persian
    add(Locale("ha").stripExtensions().language) // Hausa
    add(Locale("he").stripExtensions().language) // Hebrew
    add(Locale("ji").stripExtensions().language) // Yiddish
    add(Locale("ps").stripExtensions().language) // Pashto
    add(Locale("sd").stripExtensions().language) // Sindhi
    add(Locale("ug").stripExtensions().language) // Uighur
    add(Locale("ur").stripExtensions().language) // Urdu
  }
}
