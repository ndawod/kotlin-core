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

import java.util.Locale

/**
 * Checks whether two [Locale]s have the same language regardless of countries.
 */
fun Locale.sameLanguageAs(other: Locale) = sameLanguageAs(other.language)

/**
 * Checks whether this [Locale]'s language is the same as the specified [other] language.
 */
fun Locale.sameLanguageAs(other: String) = language.equals(
  when (other.toLowerCase()) {
    "he" -> "iw"
    "yi" -> "ji"
    "id" -> "in"
    else -> other
  },
  ignoreCase = true
)

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
 * Returns the 2-character language code for this [Locale] with the old language codes converted
 * to their new variations. The language codes are:
 *
 * "iw": "he"
 * "ji": "yi"
 * "in": "id"
 */
fun Locale.getNewLanguage(): String = when (val language = language.toLowerCase()) {
  "iw" -> "he"
  "ji" -> "yi"
  "in" -> "id"
  else -> language
}

/**
 * List of right-to-left languages.
 */
private val rtlLanguages: Set<String> by lazy {
  val set = mutableSetOf<String>()
  set.add(Locale("ar").stripExtensions().language) // Arabic
  set.add(Locale("dv").stripExtensions().language) // Divehi
  set.add(Locale("fa").stripExtensions().language) // Persian
  set.add(Locale("ha").stripExtensions().language) // Hausa
  set.add(Locale("he").stripExtensions().language) // Hebrew
  set.add(Locale("iw").stripExtensions().language) // Hebrew
  set.add(Locale("ji").stripExtensions().language) // Yiddish
  set.add(Locale("ps").stripExtensions().language) // Pashto
  set.add(Locale("sd").stripExtensions().language) // Sindhi
  set.add(Locale("ug").stripExtensions().language) // Uighur
  set.add(Locale("ur").stripExtensions().language) // Urdu
  set.add(Locale("yi").stripExtensions().language) // Yiddish
  set
}
