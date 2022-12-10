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

/**
 * A [Map] of instances of [LocalizationRepository] indexed by a [java.util.Locale].
 */
typealias LocalizationsRepositoryMap =
  java.util.concurrent.ConcurrentMap<java.util.Locale, LocalizationRepository>

/**
 * Defines a repository that stores multiple [LocalizationRepository] instances,
 * one per [Locale][java.util.Locale], and allows clients to fetch instances from different
 * threads.
 *
 * This repository must be initialized once and then remain alive through the lifecycle of
 * the app.
 */
interface LocalizationsRepository {
  /**
   * The set of supported [Locales][java.util.Locale] in this repository.
   */
  val locales: Set<java.util.Locale>

  /**
   * The base [LocalizationRepository] in this repository.
   */
  val baseLocalization: LocalizationRepository

  /**
   * Retrieves the [LocalizationRepository] associated with the provided [locale], null
   * otherwise.
   *
   * @param locale the [Locale][java.util.Locale] to retrieve a [LocalizationRepository] for
   */
  fun get(locale: java.util.Locale): LocalizationRepository?

  /**
   * Retrieves the [LocalizationRepository] associated with the provided [locale], null
   * otherwise.
   *
   * @param locale the [Locale][java.util.Locale] to retrieve a [LocalizationRepository] for
   */
  fun get(locale: String): LocalizationRepository?

  /**
   * Retrieves the [LocalizationRepository] associated with the provided [locale],
   * [baseLocalization] otherwise.
   *
   * @param locale the [Locale][java.util.Locale] to retrieve a [LocalizationRepository] for
   */
  fun getOrBase(locale: java.util.Locale): LocalizationRepository

  /**
   * Retrieves the [LocalizationRepository] associated with the provided [locale],
   * [baseLocalization] otherwise.
   *
   * @param locale the [Locale][java.util.Locale] to retrieve a [LocalizationRepository] for
   */
  fun getOrBase(locale: String): LocalizationRepository
}
