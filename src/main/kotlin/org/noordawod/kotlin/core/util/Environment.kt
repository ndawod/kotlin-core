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

@file:Suppress("unused")

package org.noordawod.kotlin.core.util

import org.noordawod.kotlin.core.extension.withExtension
import org.noordawod.kotlin.core.extension.withoutTrailingSlash

/**
 * Let the app know in which environment they're executing in.
 */
enum class Environment constructor(val identifier: String, val label: String) {
  DEVEL("devel", "Development"),
  BETA("beta", "Beta"),
  PRODUCTION("production", "Production");

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
    fun of(identifier: String): Environment? {
      val identifierLowerCased = identifier.toLowerCase()
      for (environment in values()) {
        if (environment.identifier == identifierLowerCased) {
          return environment
        }
      }
      return null
    }
  }
}
