/*
 * The MIT License
 *
 * Copyright 2024 Noor Dawod. All rights reserved.
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

package org.noordawod.kotlin.core.logger

import org.noordawod.kotlin.core.MEDIUM_BLOCK_SIZE
import org.noordawod.kotlin.core.util.getStackTraceAsString

private val lock = Any()

/**
 * A [Logger] that outputs log messages to a file.
 *
 * @param environment the runtime environment of the logger
 * @param baseDir the directory path where log files are to be stored
 * @param extension the extension (without a leading dot) to use when creating log files
 * @param minimumLogType minimum message type to log, defaults to [LogType.INFO]
 */
class FileLogger
@Throws(java.io.IOException::class)
constructor(
  environment: String,
  baseDir: java.io.File,
  private val extension: String = "log",
  private val minimumLogType: LogType = LogType.INFO,
) : BaseSimpleLogger(environment) {
  private val baseDir: java.io.File = ensureDir(baseDir)
  private val dirNameFormat = java.text.SimpleDateFormat("yyyy-MM-dd")

  @Throws(java.io.IOException::class)
  override fun log(
    type: LogType,
    tag: String,
    message: String,
    error: Throwable?,
  ) {
    if (type.lowerOrderThan(minimumLogType, orEqual = false)) {
      return
    }

    val buffer = StringBuffer(MEDIUM_BLOCK_SIZE)

    val logMessage = logMessage(
      type = type,
      tag = tag,
      message = message,
    )

    buffer.append(logMessage)
    buffer.append("\n")

    if (null != error) {
      buffer.append(error.getStackTraceAsString())
      buffer.append("\n")
    }

    synchronized(lock) {
      val logFile = ensureFile(
        baseDir = baseDir,
        name = "${dirNameFormat.format(java.util.Date())}.$extension",
      )

      java.io.FileWriter(
        logFile,
        java.nio.charset.StandardCharsets.UTF_8,
        true,
      ).use { writer ->
        writer.write("$buffer")
      }
    }
  }

  /**
   * Static functions, constants and other values.
   */
  companion object {
    @Throws(java.io.IOException::class)
    private fun ensureDir(dir: java.io.File): java.io.File = synchronized(lock) {
      val dirNormalized = dir.canonicalFile

      if (!dirNormalized.isDirectory) {
        dirNormalized.mkdirs()
        if (!dirNormalized.isDirectory) {
          throw java.io.IOException("Directory cannot be created: $dirNormalized")
        }
      }

      return dirNormalized
    }

    private fun ensureDir(
      baseDir: java.io.File,
      name: String,
    ): java.io.File = ensureDir(java.io.File(baseDir, name))

    @Throws(java.io.IOException::class, java.lang.SecurityException::class)
    private fun ensureFile(
      baseDir: java.io.File,
      name: String,
    ): java.io.File = synchronized(lock) {
      val fileNormalized = java.io.File(baseDir, name).canonicalFile

      if (fileNormalized.exists()) {
        if (fileNormalized.isFile) {
          return fileNormalized
        }

        if (fileNormalized.isDirectory) {
          throw java.io.IOException("Path exists and is a directory: $fileNormalized")
        }

        // The path is neither a directory nor a file, attempt to delete it.
        fileNormalized.delete()

        if (fileNormalized.exists()) {
          throw java.io.IOException("Path exists and cannot be repurposed: $fileNormalized")
        }
      }

      fileNormalized.createNewFile()

      if (!fileNormalized.isFile()) {
        throw java.io.IOException("Unable to create a new file: $fileNormalized")
      }

      return fileNormalized
    }
  }
}
