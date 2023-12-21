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

@file:Suppress("unused")

package org.noordawod.kotlin.core.repository

import org.noordawod.kotlin.core.util.Localization

/**
 * Defines a repository that caches and manages multiple [LocalizationRepository] instances,
 * one per [Locale][java.util.Locale].
 *
 * This repository must be initialized once and then remain alive through the lifecycle of
 * the app.
 */
interface LocalizationRepository {
  /**
   * The [Locale][java.util.Locale] associated with this [LocalizationRepository].
   */
  val locale: java.util.Locale

  /**
   * The [Localization] associated with this [LocalizationRepository].
   */
  val l10n: Localization

  /**
   * Localizes a text identified by its [key].
   *
   * @param key the localization key to localize
   */
  fun translate(key: String): String

  /**
   * Localizes a text identified by its [key] with the specified arguments to replace
   * any placeholders in the translation (%1$d, %2$s, …).
   *
   * @param key the localization key to localize
   * @param args list of arguments to substitute in the localized text
   */
  fun translate(
    key: String,
    args: Iterable<Any>,
  ): String

  /**
   * Localizes a plural text identified by its [key].
   *
   * @param key the localization key to localize
   * @param count the count of items to pluralize
   */
  fun pluralize(
    key: String,
    count: Int,
  ): String

  /**
   * Localizes a plural text identified by its [key] with the specified arguments to replace
   * any placeholders in the translation (%1$d, %2$s, …).
   *
   * @param key the localization key to localize
   * @param args list of arguments to substitute in the localized text
   * @param count the count of items to pluralize
   */
  fun pluralize(
    key: String,
    count: Int,
    args: Iterable<Any>,
  ): String

  /**
   * Localizes a quantity text identified by its [key] using only rules for zero, one, two
   * and other.
   *
   * @param key the localization key to localize
   * @param quantity the quantity value
   */
  fun quantify(
    key: String,
    quantity: Int,
  ): String

  /**
   * Localizes a quantity text identified by its [key] using only rules for zero, one, two
   * and other and with the specified arguments to replace any placeholders in the
   * translation (%1$d, %2$s, …).
   *
   * @param key the localization key to localize
   * @param args list of arguments to substitute in the localized text
   * @param quantity the quantity value
   */
  fun quantify(
    key: String,
    quantity: Int,
    args: Iterable<Any>,
  ): String
}
