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

package org.noordawod.kotlin.core.extension

import org.noordawod.kotlin.core.util.Environment

/**
 * Resolves to true if this [Environment] instance is [Environment.LOCAL] or
 * [Environment.REMOTE], false otherwise.
 */
val Environment.isLocalOrRemote: Boolean
  get() = isLocal || isRemote

/**
 * Resolves to true if this [Environment] instance is [Environment.LOCAL] or
 * [Environment.DEVEL], false otherwise.
 */
val Environment.isLocalOrDevel: Boolean
  get() = isLocal || isDevel

/**
 * Resolves to true if this [Environment] instance is [Environment.LOCAL],
 * [Environment.DEVEL] or [Environment.DEVEL], false otherwise.
 */
val Environment.isLocalOrRemoteOrDevel: Boolean
  get() = isLocal || isRemote || isDevel

/**
 * Returns the appropriate log level for this [Environment] instance.
 */
@Suppress("StringLiteralDuplication")
fun Environment.logLevel(): String = when (this) {
  Environment.LOCAL -> "DEBUG"
  Environment.REMOTE -> "DEBUG"
  Environment.DEVEL -> "DEBUG"
  Environment.BETA -> "WARNING"
  Environment.PRODUCTION -> "ERROR"
}

/**
 * Returns the default timeout, in seconds, based on this [Environment].
 */
@Suppress("MagicNumber")
fun Environment?.timeoutInSeconds(): Int = when (this) {
  Environment.LOCAL, Environment.REMOTE -> 5
  Environment.DEVEL, null -> 10
  Environment.BETA -> 20
  Environment.PRODUCTION -> 30
}

/**
 * Returns the default timeout, in milliseconds, based on this [Environment].
 */
fun Environment?.timeoutInMilliseconds(): Long = timeoutInSeconds() * MILLIS_IN_1_SECOND

/**
 * Returns the base directory for this [Environment] where ".well-known" files are stored.
 */
fun Environment.wellKnownDirectory() = "well-known${java.io.File.separatorChar}$this"

/**
 * Returns the path to a ".well-known" [file] based on this [Environment].
 */
fun Environment.wellKnownFile(file: String) =
  "${wellKnownDirectory()}${java.io.File.separatorChar}$file"
