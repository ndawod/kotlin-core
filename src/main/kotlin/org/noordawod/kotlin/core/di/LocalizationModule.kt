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

package org.noordawod.kotlin.core.di

import dagger.Module
import dagger.Provides
import org.noordawod.kotlin.core.Constants
import org.noordawod.kotlin.core.config.LocalizationConfiguration
import org.noordawod.kotlin.core.config.TranslationConfiguration
import org.noordawod.kotlin.core.extension.mutableMapWith
import org.noordawod.kotlin.core.repository.LocalizationRepository
import org.noordawod.kotlin.core.repository.LocalizationsRepository
import org.noordawod.kotlin.core.repository.LocalizationsRepositoryMap
import org.noordawod.kotlin.core.repository.impl.LocalizationRepositoryImpl
import org.noordawod.kotlin.core.repository.impl.LocalizationsRepositoryImpl
import org.noordawod.kotlin.core.util.Localization

/**
 * Runtime (read-only) configuration accessible via dependency injection for modules that wish
 * to be aware of multiple localizations.
 *
 * @param baseDirectory base directory where localization files reside
 * @param l10n configuration for localized texts
 */
@Module
class LocalizationModule constructor(
  baseDirectory: java.io.File,
  l10n: LocalizationConfiguration
) {
  private val baseLocalization: Localization
  private val otherLocalizations: LocalizationsRepositoryMap?
  private val locales: Set<java.util.Locale>

  /**
   * Base [Localization] at runtime for this app.
   */
  @javax.inject.Singleton
  @Provides
  fun baseLocalization(): Localization = baseLocalization

  /**
   * Returns the singleton [LocalizationsRepository] instance.
   */
  @javax.inject.Singleton
  @Provides
  fun localizationsRepository(): LocalizationsRepository = LocalizationsRepositoryImpl(
    locales = locales,
    baseLocalization = LocalizationRepositoryImpl(baseLocalization),
    otherLocalizations = otherLocalizations
  )

  init {
    val locationRepositories =
      java.util.concurrent.ConcurrentHashMap<java.util.Locale, LocalizationRepository>(
        Constants.DEFAULT_LIST_CAPACITY
      )

    val translations = mutableMapWith<java.util.Locale, Localization>(
      Constants.DEFAULT_LIST_CAPACITY
    )

    fun load(translation: TranslationConfiguration): Localization {
      val locale = java.util.Locale.forLanguageTag(translation.locale)
      val file = java.io.File(baseDirectory, translation.file)

      return Localization.from(locale, file).apply {
        translations[locale] = this
        locationRepositories[locale] = LocalizationRepositoryImpl(this)
      }
    }

    // Read base l10n first.
    baseLocalization = load(l10n.base)

    // Read the rest of the configured translations.
    l10n.translations?.forEach(::load)

    otherLocalizations = if (locationRepositories.isEmpty()) null else locationRepositories

    locales = translations.keys
  }
}
