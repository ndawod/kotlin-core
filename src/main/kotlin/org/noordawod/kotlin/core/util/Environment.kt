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

import org.noordawod.kotlin.core.extension.withExtension
import org.noordawod.kotlin.core.extension.withoutTrailingSlash

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
enum class Environment constructor(
  val identifier: String,
  val label: String,
  val isLocal: Boolean,
  val isDevel: Boolean,
  val isBeta: Boolean,
  val isProduction: Boolean
) {
  /**
   * Local-only environment.
   *
   * In this environment, applications are connecting to the localhost only as developers are
   * busy writing new features and fixing bugs.
   */
  LOCAL(
    "local",
    "Local-only",
    true,
    false,
    false,
    false
  ),

  /**
   * Development environment.
   *
   * Much like the [LOCAL] environment, but the applications can be deployed remotely as well.
   */
  DEVEL(
    "devel",
    "Development",
    false,
    true,
    false,
    false
  ),

  /**
   * Beta (Staging) environment.
   *
   * In this environment, applications are deployed on a staging, usually remote, server where
   * beta users are able to test it out before the final deployment to [PRODUCTION].
   */
  BETA(
    "beta",
    "Beta",
    false,
    false,
    true,
    false
  ),

  /**
   * Production environment.
   *
   * In this environment, applications are considered rock-solid and are available for
   * consumption by the intended users.
   */
  PRODUCTION(
    "production",
    "Production",
    false,
    false,
    false,
    true
  );

  /**
   * Returns this [Environment]'s identifier.
   */
  override fun toString() = identifier

  /**
   * Returns a relative path to Yaml configuration file associated with this environment.
   */
  fun yaml(baseDir: String, file: String, extension: String = "yaml"): String {
    val separatorChar = java.io.File.separatorChar
    val normalizedBaseDir = baseDir.withoutTrailingSlash()
    val normalizedFile = file.withExtension(extension)

    return "$normalizedBaseDir$separatorChar$this$separatorChar$normalizedFile"
  }

  companion object {
    /**
     * Returns a new [Environment] matching the specified [identifier] on success, null otherwise.
     */
    fun from(identifier: String): Environment? {
      val identifierLowerCased = identifier.lowercase(java.util.Locale.ENGLISH)
      for (environment in values()) {
        if (environment.identifier == identifierLowerCased) {
          return environment
        }
      }
      return null
    }
  }
}

/**
 * Returns the base directory for this [Environment] where localization files are stored.
 */
fun Environment.l10nDirectory() =
  "l10n${java.io.File.separatorChar}$this"

/**
 * Returns the path to a localization [file] based on this [Environment].
 */
fun Environment.l10nFile(file: String) =
  "${l10nDirectory()}${java.io.File.separatorChar}$file"

/**
 * Returns a [Throwable] that describes a wrong environment value.
 *
 * @param message error message to use
 */
fun invalidEnvironmentThrowable(message: String = "First argument must be one of"): Throwable {
  val environments = Environment.values()
  val errorArray = Array(environments.size) { environments[it].identifier }

  return IllegalArgumentException(
    errorArray.joinToString(separator = ", ", prefix = "$message: ")
  )
}

/**
 * Returns a [Throwable] that describes an invalid file path.
 *
 * @param file path to the file or directory
 * @param cause explains why the file is invalid
 */
fun invalidFileThrowable(file: java.io.File, cause: String): Throwable =
  IllegalArgumentException("File path is invalid: ${file.canonicalPath} ($cause)")
