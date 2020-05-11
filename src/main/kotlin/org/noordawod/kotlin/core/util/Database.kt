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

import net.moznion.uribuildertiny.URIBuilderTiny

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
   * Default collation is wide and even includes emoticons.
   */
  const val DEFAULT_COLLATION: String = "utf8mb4_general_ci"

  /**
   * How long in milliseconds until a client can connect to a server.
   */
  const val DEFAULT_CONNECT_TIMEOUT: Long = 2000L

  /**
   * How long in milliseconds until a socket can connect to a destination.
   */
  const val DEFAULT_SOCKET_TIMEOUT: Long = 2000L

  /**
   * Sets the timezone of the server.
   */
  const val DEFAULT_TIMEZONE: String = "UTC"

  /**
   * Returns the database connection URI string based on input parameters.
   *
   * @param protocol the associated URI protocol (scheme) for this JDBC driver
   * @param host host name of the database server
   * @param port database server's connection port
   * @param user username to authenticate against the database server
   * @param pass password to authenticate against the database server
   * @param schema main database schema name to attach to
   * @param collation collation to choose after connecting to database server
   * @param connectTimeout timeout in seconds to wait for a connection
   * @param socketTimeout timeout in seconds for the socket to connect
   * @param serverTimezone timezone to use in the server after connection
   * @return the final URI to connect to the JDBC database server
   *
   * @see <a href="https://tinyurl.com/yagm2clw">Connector/J Configuration Properties</a>
   */
  @Suppress("LongParameterList")
  fun uri(
    protocol: String,
    host: String,
    port: Int,
    user: String,
    pass: String,
    schema: String,
    collation: String = DEFAULT_COLLATION,
    connectTimeout: Long = DEFAULT_CONNECT_TIMEOUT,
    socketTimeout: Long = DEFAULT_SOCKET_TIMEOUT,
    serverTimezone: String = DEFAULT_TIMEZONE
  ): String = URIBuilderTiny()
    .setScheme(protocol)
    .setHost(host)
    .setPort(port)
    .setPaths(schema)
    .setQueryParameters(
      mapOf<String, Any>(
        "user" to user,
        "password" to pass,
        "connectionCollation" to collation,
        "useUnicode" to true.toString(),
        "useSSL" to false.toString(),
        "useCompression" to false.toString(),
        "autoReconnect" to true.toString(),
        "connectTimeout" to "$connectTimeout",
        "socketTimeout" to "$socketTimeout",
        "serverTimezone" to serverTimezone
      )
    )
    .build()
    .toString()

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
  val WHITE_SPACES = java.util.regex.Pattern.compile("\\s+")

  /**
   * Prints a list of database drivers currently loaded in the JVM. Used primarily for
   * debugging.
   */
  fun showDatabaseDrivers(uri: String) {
    val drivers = java.sql.DriverManager.getDrivers()
    if (null == drivers || !drivers.hasMoreElements()) {
      System.err.println("No database drivers are registered!")
    } else {
      println("Checking registered database drivers against URI: $uri")
      while (drivers.hasMoreElements()) {
        val driver = drivers.nextElement()
        val acceptsURL = try {
          driver.acceptsURL(uri)
          true
        } catch (ignored: java.sql.SQLException) {
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
