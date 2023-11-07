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

/**
 * Let the app know in which environment they're executing in.
 *
 * @param identifier a machine-friendly identifier with all characters lower-cased
 * @param label a human-friendly identifier
 * @param isLocal whether this is the [LOCAL] environment
 * @param isDevel whether this is the [DEVEL] environment
 * @param isBeta whether this is the [BETA] environment
 * @param isProduction whether this is the [PRODUCTION] environment
 *
 */
@Suppress("LongParameterList")
enum class Environment(
  val identifier: String,
  val label: String,
  val isLocal: Boolean,
  val isDevel: Boolean,
  val isBeta: Boolean,
  val isProduction: Boolean,
) {
  /**
   * Local-only environment.
   *
   * In this environment, applications are connecting to the localhost only as developers are
   * busy writing new features and fixing bugs.
   */
  LOCAL(
    identifier = "local",
    label = "Local-only",
    isLocal = true,
    isDevel = false,
    isBeta = false,
    isProduction = false,
  ),

  /**
   * Development environment.
   *
   * Much like the [LOCAL] environment, but the applications can be deployed remotely as well.
   */
  DEVEL(
    identifier = "devel",
    label = "Development",
    isLocal = false,
    isDevel = true,
    isBeta = false,
    isProduction = false,
  ),

  /**
   * Beta (Staging) environment.
   *
   * In this environment, applications are deployed on a staging, usually remote, server where
   * beta users are able to test it out before the final deployment to [PRODUCTION].
   */
  BETA(
    identifier = "beta",
    label = "Beta",
    isLocal = false,
    isDevel = false,
    isBeta = true,
    isProduction = false,
  ),

  /**
   * Production environment.
   *
   * In this environment, applications are considered rock-solid and are available for
   * consumption by the intended users.
   */
  PRODUCTION(
    identifier = "production",
    label = "Production",
    isLocal = false,
    isDevel = false,
    isBeta = false,
    isProduction = true,
  ),
  ;

  /**
   * Returns this [Environment]'s identifier.
   */
  override fun toString() = identifier

  companion object {
    /**
     * Returns an [Environment] matching the specified [identifier] on success,
     * null otherwise.
     *
     * @param identifier the environment's value to evaluate
     */
    fun from(identifier: String): Environment? {
      val identifierNormalized = identifier.lowercase(java.util.Locale.ENGLISH)
      for (environment in values()) {
        if (environment.identifier == identifierNormalized) {
          return environment
        }
      }
      return null
    }
  }
}
