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
import org.noordawod.kotlin.core.logger.Logger
import org.noordawod.kotlin.core.repository.ExecutorRepository
import org.noordawod.kotlin.core.repository.impl.ExecutorRepositoryImpl
import org.noordawod.kotlin.core.util.Environment

/**
 * Core singleton instances accessible via dependency injection.
 *
 * @param environment the [Environment] this app is running in
 * @param logger the current [Logger] to use
 * @param executorThreads number of threads to use for [ExecutorRepository]
 */
@Module
class RepositoryModule(
  private val environment: Environment,
  private val logger: Logger,
  private val executorThreads: Int,
) {
  /**
   * Loaded configuration at runtime for this app.
   */
  @javax.inject.Singleton
  @Provides
  fun environment(): Environment = environment

  /**
   * Returns the singleton [Logger] instance.
   */
  @javax.inject.Singleton
  @Provides
  fun logger(): Logger = logger

  /**
   * Singleton [ExecutorRepository] for this app.
   */
  @javax.inject.Singleton
  @Provides
  fun executorRepository(): ExecutorRepository = ExecutorRepositoryImpl().apply {
    start(executorThreads)
  }
}
