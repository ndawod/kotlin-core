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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core.util

import org.noordawod.kotlin.core.extension.sameLanguageAs

/**
 * Binds a [java.util.Locale] and [Properties], the latter holds translations for the
 * specified [locale].
 */
open class Localization(
  /**
   * The [java.util.Locale] associated with this [Localization] instance.
   */
  val locale: java.util.Locale,

  /**
   * The localized translations associated with [locale].
   */
  val translation: Properties
) {
  companion object {
    /**
     * Loads [Localization] from the specified [file].
     */
    @Throws(java.io.IOException::class)
    fun from(locale: java.util.Locale, file: String) =
      Localization(locale, Properties.from(file))

    /**
     * Loads [Localization] from the specified [file].
     */
    @Throws(java.io.IOException::class)
    fun from(locale: java.util.Locale, file: java.io.File) =
      Localization(locale, Properties.from(file))

    /**
     * Loads [Localization] from the list of [files] / [paths] by merging all together.
     * Entries appearing in later files override those in earlier positions.
     */
    @Throws(java.io.IOException::class)
    fun from(
      locale: java.util.Locale,
      paths: Iterable<String>?,
      files: Iterable<java.io.File>?
    ): Localization = Localization(locale, Properties.from(paths, files))
  }
}

/**
 * A [Map] of instances of [Localization] indexed by a [java.util.Locale].
 */
typealias TranslationsMap = Map<java.util.Locale, Localization>

/**
 * Checks whether a [TranslationsMap] contains a [java.util.Locale] with the specified [language].
 */
fun TranslationsMap.hasLanguage(language: String): Boolean =
  null != keys.firstOrNull { it.sameLanguageAs(language) }

/**
 * Returns a [Localization] for the specified [language] on success, null otherwise.
 */
fun TranslationsMap.localizationFor(language: String): Localization? {
  val locale: java.util.Locale? = keys.firstOrNull { it.sameLanguageAs(language) }
  return if (null != locale) get(locale) else null
}
