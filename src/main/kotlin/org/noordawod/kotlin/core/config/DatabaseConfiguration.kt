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
)

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
  open val encoding: String = "utf8"

  /**
   * Encoding to use for this connection (normally passed in
   * "SET NAMES [encoding] COLLATE [collation]" MySQL command.
   */
  open val collation: String = "utf8_general_ci"

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
      .setQueryParameter("user", user)
      .setQueryParameter("password", pass)
      .setQueryParameter("characterEncoding", encoding)
      .setQueryParameter("connectionCollation", collation)
      .setQueryParameter("useUnicode", "true")
      .setQueryParameter("useSSL", "false")
      .setQueryParameter("useCompression", "false")
      .setQueryParameter("autoReconnect", "true")
      .setQueryParameter("connectTimeout", "$connectTimeout")
      .setQueryParameter("socketTimeout", "$socketTimeout")
      .setQueryParameter("serverTimezone", serverTimezone)
      .build()
      .toString()
  }
}

/**
 * A special [MySqlDatabaseConfiguration] that uses the "utf8" encoding and "utf8mb4"
 * character set.
 */
open class Utf8Mb4DatabaseConfiguration constructor(
  driver: String,
  host: String,
  port: Int,
  user: String,
  pass: String,
  db: String
) : MySqlDatabaseConfiguration(driver, host, port, user, pass, db) {
  override val encoding: String = "utf8mb4"
  override val collation: String = "utf8mb4_general_ci"
}
