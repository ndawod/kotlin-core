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

package org.noordawod.kotlin.core.util

import org.noordawod.kotlin.core.Constants

/**
 * A signature of a simple function that returns nothing.
 */
typealias CodeBlock = () -> Unit

/**
 * A signature of a function that accepts a [Throwable].
 */
typealias ThrowableHandler = (Throwable) -> Unit

/**
 * A default instance implementing a [ThrowableHandler].
 */
val defaultThrowableHandler: ThrowableHandler = { error ->
  System.err.println("*".repeat(Constants.DEFAULT_LINE_WIDTH))
  System.err.println("FATAL: UNEXPECTED ERROR DETECTED! A STACK TRACE FOLLOWS:")
  System.err.println("-".repeat(Constants.DEFAULT_LINE_WIDTH))
  error.printStackTrace()
  System.err.println("*".repeat(Constants.DEFAULT_LINE_WIDTH))
}

/**
 * A general-purpose try-catch handler with a default error handler.
 *
 * @param block the block to run
 */
fun tryCatch(block: CodeBlock) {
  tryCatch(block, defaultThrowableHandler)
}

/**
 * A general-purpose try-catch handler with a callback to handle errors.
 *
 * @param block the block to run
 * @param onError callback to handle errors
 */
fun tryCatch(
  block: CodeBlock,
  onError: ThrowableHandler,
) {
  tryCatch(block, onError) { }
}

/**
 * A general-purpose try-catch handler with callbacks to handle errors and completion.
 *
 * @param block the block to run
 * @param onError callback to handle errors
 * @param onComplete callback when [block] completes
 */
fun tryCatch(
  block: CodeBlock,
  onError: ThrowableHandler,
  onComplete: CodeBlock,
) {
  try {
    block.invoke()
  } catch (
    @Suppress("TooGenericExceptionCaught")
    error: Throwable,
  ) {
    onError.invoke(error)
  } finally {
    onComplete.invoke()
  }
}

/**
 * Returns a [Throwable] that describes a wrong environment value.
 *
 * @param message error message to use
 */
fun invalidEnvironmentThrowable(message: String = "First argument must be one of"): Throwable {
  val environments = Environment.values()
  val errorArray = Array(environments.size) { environments[it].identifier }

  return IllegalArgumentException(
    errorArray.joinToString(separator = ", ", prefix = "$message: "),
  )
}

/**
 * Returns a [Throwable] that describes an invalid file path.
 *
 * @param file path to the file or directory
 * @param cause explains why the file is invalid
 */
fun invalidFileThrowable(
  file: java.io.File,
  cause: String,
): Throwable = IllegalArgumentException("File path is invalid: ${file.canonicalPath} ($cause)")
