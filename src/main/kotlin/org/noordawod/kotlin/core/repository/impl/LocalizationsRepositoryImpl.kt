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

package org.noordawod.kotlin.core.repository.impl

import org.noordawod.kotlin.core.repository.LocalizationRepository
import org.noordawod.kotlin.core.repository.LocalizationsRepository
import org.noordawod.kotlin.core.repository.LocalizationsRepositoryMap

internal class LocalizationsRepositoryImpl constructor(
  override val locales: Set<java.util.Locale>,
  override val baseLocalization: LocalizationRepository,
  private val otherLocalizations: LocalizationsRepositoryMap?
) : LocalizationsRepository {
  override fun get(locale: java.util.Locale): LocalizationRepository? =
    otherLocalizations?.get(locale)

  override fun get(locale: String): LocalizationRepository? =
    get(java.util.Locale.forLanguageTag(locale))

  override fun getOrBase(locale: java.util.Locale): LocalizationRepository =
    get(locale) ?: baseLocalization

  override fun getOrBase(locale: String): LocalizationRepository =
    getOrBase(java.util.Locale.forLanguageTag(locale))
}
