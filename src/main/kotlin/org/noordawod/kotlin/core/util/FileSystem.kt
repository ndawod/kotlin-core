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

import org.noordawod.kotlin.core.extension.trimOrNull

/**
 * A bunch of functions dealing with the file system.
 */
@Suppress("MemberVisibilityCanBePrivate")
object FileSystem {
  /**
   * Copies the [source] file to a [target] location, overwriting the latter if it exists.
   *
   * @param source the file to copy
   * @param target destination file
   */
  fun copy(
    source: java.io.File,
    target: java.io.File,
  ) {
    source.copyTo(
      target = target,
      overwrite = true,
    )
  }

  /**
   * Copies the [source] file to a [target] location, overwriting the latter if it exists.
   *
   * @param source the file to copy
   * @param target destination file
   */
  fun copy(
    source: String,
    target: String,
  ) {
    copy(
      source = java.io.File(source),
      target = java.io.File(target),
    )
  }

  /**
   * Creates a temporary file with the provided [extension]. The [extension] is used as-is,
   * so include a leading "." if you need it.
   *
   * @param extension the file extension to use, defaults to ""
   */
  fun createTempFile(extension: String = ""): java.io.File = java.nio.file.Files.createTempFile(
    "",
    extension,
  ).toFile()

  /**
   * Generates a random file name from the provided [alphabet] with the specified [length].
   * The [extension] is used as-is, so include a leading "." if you need it.
   *
   * @param alphabet the alphabet to use when constructing the file name
   * @param length maximum length of the resulting file name
   * @param extension the file extension to use, defaults to ""
   */
  fun getRandomFileName(
    alphabet: String,
    length: Int,
    extension: String = "",
  ): String {
    val buffer = StringBuilder(length)
    val alphabetLength = alphabet.length

    for (idx in 0..length) {
      val nextPos = secureRandom.nextInt(alphabetLength)
      buffer.append(alphabet[nextPos])
    }

    if (extension.isNotEmpty()) {
      buffer.append(extension)
    }

    return buffer.toString()
  }

  /**
   * Generates a new file path where the provided [fileName] is hosted in a subdirectory
   * that's [depth] deep under [a parent directory][directory], returns the eventual file path.
   *
   * The directory structure will be automatically created.
   *
   * @param directory path to directory to host the file
   * @param fileName name of the file to situate under [directory]
   * @param depth how many subdirectories should [fileName] be situated
   */
  fun getEventualFile(
    directory: java.io.File,
    fileName: String,
    depth: Int,
  ): java.io.File = getEventualFile(
    directory = directory.canonicalPath,
    fileName = fileName,
    depth = depth,
  )

  /**
   * Generates a new file path where the provided [fileName] is hosted in a subdirectory
   * that's [depth] deep under [a parent directory][directory], returns the eventual file path.
   *
   * The directory structure will be automatically created.
   *
   * @param directory path to directory to host the file
   * @param fileName name of the file to situate under [directory]
   * @param depth how many subdirectories should [fileName] be situated
   */
  fun getEventualFile(
    directory: String,
    fileName: String,
    depth: Int,
  ): java.io.File {
    val normalizedDirectory = directory.trimOrNull()
      ?: error("Directory to host the file is empty.")

    if (depth > fileName.length) {
      error("java.io.File name's length is less than the requested depth.")
    }

    val buffer = StringBuilder(normalizedDirectory.length + 2 * depth + fileName.length + 1)

    val absoluteDirectory = java.io.File(normalizedDirectory).canonicalPath
    buffer.append(absoluteDirectory.trimEnd(java.io.File.separatorChar))

    for (idx in 0..depth) {
      buffer.append(java.io.File.separatorChar)
      buffer.append(fileName[idx])
    }

    buffer.append(java.io.File.separatorChar)
    buffer.append(fileName)

    return java.io.File(buffer.toString())
  }

  /**
   * Creates the directory structure leading up to the provided [directory], returns true if
   * the directory is created successfully, false otherwise.
   *
   * @param directory the directory structure to create
   */
  fun mkdirs(directory: java.io.File): Boolean {
    if (!directory.exists()) {
      directory.mkdirs()
    }

    return directory.isDirectory
  }

  /**
   * Spawns a new process and executes the provided [program], optionally having [args], and
   * returns the output of the process.
   * If [includeErrors] is true, then the errors will also be included in the result.
   *
   * @param program absolute path to the program to run
   * @param args optional arguments to pass along to the [program]
   * @param includeErrors whether to include errors in the returned result, defaults to false
   */
  fun execute(
    program: String,
    args: Collection<String>?,
    includeErrors: Boolean = false,
  ): String {
    val buffer = java.lang.StringBuilder(DEFAULT_BUFFER_SIZE)
    val programArgs = args ?: listOf()

    // The final execution arguments list includes the program itself.
    val commandArray: List<String> = listOf(program) + programArgs

    // Build the program and its arguments.
    val processBuilder = ProcessBuilder(commandArray)
    processBuilder.redirectErrorStream(includeErrors)

    // Run the program and read its output.
    try {
      val process = processBuilder.start()
      java.io.BufferedReader(java.io.InputStreamReader(process.inputStream)).use { reader ->
        val lineSeparator = System.lineSeparator()
        do {
          val readLine = reader.readLine() ?: break
          buffer.append(readLine)
          buffer.append(lineSeparator)
        } while (true)
        process.waitFor()
      }
    } catch (error: InterruptedException) {
      throw java.io.IOException(error)
    }

    return buffer.toString().trim { it <= ' ' }
  }
}
