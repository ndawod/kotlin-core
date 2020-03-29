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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "CanBeParameter")

package org.noordawod.kotlin.core.config

import net.moznion.uribuildertiny.URIBuilderTiny

/**
 * Generic database configuration suitable for most database drivers.
 */
open class DatabaseConfiguration constructor(
  val driver: String,
  val host: String,
  val port: Int,
  val user: String,
  val pass: String,
  val db: String
) {
  companion object {
    /**
     * The default encoding is UTF-8.
     */
    const val DEFAULT_ENCODING: String = "utf8"

    /**
     * Default collation is UTF-8, general and case-insensitive.
     */
    const val DEFAULT_COLLATION: String = "utf8_general_ci"

    /**
     * Extended collation is UTF-8 and up to 4 bytes in length, suitable for storing
     * emoticons and other recent additions to the Unicode universe.
     */
    const val EXTENDED_COLLATION: String = "utf8mb4_general_ci"
  }
}

/**
 * Database configuration suitable for MySQL server.
 */
open class MySqlDatabaseConfiguration constructor(
  driver: String,
  host: String,
  port: Int,
  user: String,
  pass: String,
  db: String
) : DatabaseConfiguration(driver, host, port, user, pass, db) {
  /**
   * Encoding to use for this connection (normally passed in
   * "SET NAMES [encoding]" MySQL command.
   */
  open val encoding: String = DEFAULT_ENCODING

  /**
   * Encoding to use for this connection (normally passed in
   * "SET NAMES [encoding] COLLATE [collation]" MySQL command.
   */
  open val collation: String = DEFAULT_COLLATION

  /**
   * How long in milliseconds until a client can connect to a server.
   */
  @Suppress("MagicNumber")
  open val connectTimeout: Long = 2_000L

  /**
   * How long in milliseconds until a socket can connect to a destination.
   */
  @Suppress("MagicNumber")
  open val socketTimeout: Long = 2_000L

  /**
   * Sets the timezone of the server.
   */
  open val serverTimezone: String = "UTC"

  /**
   * Returns the connection URI string for MySQL.
   *
   * @see <a href="https://tinyurl.com/yagm2clw">Connector/J Configuration Properties</a>
   */
  val uri: String by lazy {
    URIBuilderTiny()
      .setScheme("mysql")
      .setHost(host)
      .setPort(port)
      .setPaths(db)
      .setQueryParameters(mapOf<String, Any>(
        "user" to user,
        "password" to pass,
        "characterEncoding" to encoding,
        "connectionCollation" to collation,
        "useUnicode" to true.toString(),
        "useSSL" to false.toString(),
        "useCompression" to false.toString(),
        "autoReconnect" to true.toString(),
        "connectTimeout" to "$connectTimeout",
        "socketTimeout" to "$socketTimeout",
        "serverTimezone" to serverTimezone
      ))
      .build()
      .toString()
  }
}

/**
 * A special [MySqlDatabaseConfiguration] that uses the
 * [DatabaseConfiguration.DEFAULT_ENCODING] encoding and
 * [DatabaseConfiguration.EXTENDED_COLLATION] collation.
 */
open class Utf8Mb4DatabaseConfiguration constructor(
  driver: String,
  host: String,
  port: Int,
  user: String,
  pass: String,
  db: String
) : MySqlDatabaseConfiguration(driver, host, port, user, pass, db) {
  override val encoding: String = DEFAULT_ENCODING
  override val collation: String = EXTENDED_COLLATION
}
