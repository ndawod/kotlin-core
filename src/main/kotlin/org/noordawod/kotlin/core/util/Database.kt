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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "HasPlatformType")

package org.noordawod.kotlin.core.util

import java.sql.DriverManager
import java.sql.SQLException
import java.util.regex.Pattern

/**
 * Utility functions for databases.
 */
object Database {
  /**
   * The character used to wrap field, table and database names in most databases.
   */
  const val FIELD_WRAPPER_CHAR: Char = '`'

  /**
   * The character used to escape values in most databases.
   */
  const val VALUE_WRAPPER_CHAR: Char = '\''

  /**
   * The double-quote character.
   */
  const val DOUBLE_QUOTE_CHAR: Char = '"'

  /**
   * The characters that most databases need to escape.
   */
  @Suppress("MagicNumber")
  val ESCAPE_CHARS: CharArray = charArrayOf(
    '\\',
    '\n',
    '\r',
    VALUE_WRAPPER_CHAR,
    DOUBLE_QUOTE_CHAR,
    0x00.toChar(),
    0x1a.toChar()
  )

  /**
   * Matches one or more white-space characters.
   */
  val WHITE_SPACES = Pattern.compile("\\s+")

  /**
   * Prints a list of database drivers currently loaded in the JVM. Used primarily for
   * debugging.
   */
  fun showDatabaseDrivers(uri: String) {
    val drivers = DriverManager.getDrivers()
    if (null == drivers || !drivers.hasMoreElements()) {
      System.err.println("No database drivers are registered!")
    } else {
      println("Checking registered database drivers against URI: $uri")
      while (drivers.hasMoreElements()) {
        val driver = drivers.nextElement()
        val acceptsURL = try {
          driver.acceptsURL(uri)
          true
        } catch (ignored: SQLException) {
          false
        }
        println("  Class: " + driver.javaClass.name)
        println("  Version: " + driver.majorVersion + "." + driver.minorVersion)
        println("  Accepts URL? $acceptsURL")
      }
    }
  }

  /**
   * Escapes the provided string value and returns the escaped value.
   */
  fun escape(value: String, wrapper: Char?): String {
    val allow = FIELD_WRAPPER_CHAR != wrapper
    return escape(
      value,
      wrapper,
      allow,
      allow
    )
  }

  /**
   * Escapes the provided string value and returns the escaped value.
   */
  @Suppress("MagicNumber", "ComplexMethod")
  fun escape(
    value: String,
    wrapper: Char?,
    alsoPercent: Boolean,
    alsoLowdash: Boolean
  ): String {
    val length = value.length
    val builder = StringBuilder(value.length + 10)
    var arrayLength = ESCAPE_CHARS.size
    if (alsoPercent) {
      arrayLength++
    }
    if (alsoLowdash) {
      arrayLength++
    }

    // Create new array containing the characters to escape.
    val chars = CharArray(arrayLength)
    System.arraycopy(ESCAPE_CHARS, 0, chars, 0, ESCAPE_CHARS.size)
    var idx: Int = -1
    if (alsoPercent) {
      chars[++idx + ESCAPE_CHARS.size] = '%'
    }
    if (alsoLowdash) {
      chars[++idx + ESCAPE_CHARS.size] = '_'
    }
    idx = -1
    while (length > ++idx) {
      val valueChar = value[idx]
      @Suppress("ComplexCondition")
      if (
        null == wrapper ||
        (VALUE_WRAPPER_CHAR != wrapper || DOUBLE_QUOTE_CHAR != valueChar) &&
        (DOUBLE_QUOTE_CHAR != wrapper || VALUE_WRAPPER_CHAR != valueChar)
      ) {
        var found = false
        var specialCharIdx = -1
        while (!found && chars.size > ++specialCharIdx) {
          val thisChar = chars[specialCharIdx]
          found = valueChar == thisChar
        }
        if (found) {
          builder.append('\\')
        }
      }
      builder.append(valueChar)
    }
    return if (null == wrapper) {
      builder.toString()
    } else {
      wrapper.toString() + builder.toString() + wrapper
    }
  }

  /**
   * Builds and returns a "LIKE" SQL command for most databases.
   */
  fun like(word: String, start: Boolean, end: Boolean): String {
    var likeQuery = escape(word, null)
    if (start) {
      likeQuery = "%$likeQuery"
    }
    if (end) {
      likeQuery = "$likeQuery%"
    }
    return likeQuery
  }

  /**
   * Splits [query] using a white-space separator and returns the individual parts as
   * a [Set] of [String]s. If [query] is null or contains no words, null is returned.
   */
  fun words(query: String?): Set<String>? {
    val strings = WHITE_SPACES.split(query)
    return if (true == strings?.isNotEmpty()) {
      LinkedHashSet<String>(strings.size).let {
        for (string in strings) {
          it.add(string)
        }
        it
      }
    } else {
      null
    }
  }
}
