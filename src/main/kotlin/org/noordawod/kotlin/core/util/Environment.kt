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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core.util

import org.noordawod.kotlin.core.ASCII_LOCALE

/**
 * Let the app know in which environment they're executing in.
 *
 * @param identifier a machine-friendly identifier with all characters lower-cased
 * @param description a human-friendly description of the environment
 *
 */
@Suppress("LongParameterList")
enum class Environment(
  val identifier: String,
  val description: String,
) {
  /**
   * Local-only development environment.
   *
   * In this environment, applications are connecting to the localhost only for both
   * frontend and backend.
   */
  LOCAL(
    identifier = "local",
    description = "Local-only development environment",
  ),

  /**
   * Local frontend development with remote backend.
   *
   * In this environment, applications are connecting to localhost for frontend, and
   * a remote host for backend.
   */
  REMOTE(
    identifier = "remote",
    description = "Local frontend development with remote backend",
  ),

  /**
   * Development (Testing) environment.
   *
   * In this environment, applications are deployed on a remote server where
   * developers are able to test it out before the next deployment to [BETA].
   */
  DEVEL(
    identifier = "devel",
    description = "Remote-only testing environment for developers",
  ),

  /**
   * Beta (a.k.a. Staging) environment.
   *
   * In this environment, applications are deployed on a remote server where
   * developers are able to test it out before the final deployment to [PRODUCTION].
   */
  BETA(
    identifier = "beta",
    description = "Remote-only testing environment for users",
  ),

  /**
   * Production environment.
   *
   * In this environment, applications are considered rock-solid and are available for
   * consumption by the intended users.
   */
  PRODUCTION(
    identifier = "production",
    description = "Final deployment environment for rock-solid applications",
  ),
  ;

  /**
   * Resolves to `true` when this is the [LOCAL] environment, `false` otherwise.
   */
  val isLocal: Boolean get() = this == LOCAL

  /**
   * Resolves to `true` when this is the [REMOTE] environment, `false` otherwise.
   */
  val isRemote: Boolean get() = this == REMOTE

  /**
   * Resolves to `true` when this is the [DEVEL] environment, `false` otherwise.
   */
  val isDevel: Boolean get() = this == DEVEL

  /**
   * Resolves to `true` when this is the [BETA] environment, `false` otherwise.
   */
  val isBeta: Boolean get() = this == BETA

  /**
   * Resolves to `true` when this is the [PRODUCTION] environment, `false` otherwise.
   */
  val isProduction: Boolean get() = this == PRODUCTION

  /**
   * Returns this [Environment]'s identifier.
   */
  override fun toString() = identifier

  /**
   * Static functions, constants and other values.
   */
  companion object {
    /**
     * Returns an [Environment] matching the specified [identifier] on success,
     * null otherwise.
     *
     * @param identifier the environment's value to evaluate
     */
    fun from(identifier: String): Environment? {
      val identifierNormalized = identifier.lowercase(ASCII_LOCALE)
      for (environment in entries) {
        if (environment.identifier == identifierNormalized) {
          return environment
        }
      }
      return null
    }
  }
}
