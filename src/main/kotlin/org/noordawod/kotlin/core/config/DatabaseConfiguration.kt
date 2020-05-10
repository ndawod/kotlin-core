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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core.config

import net.moznion.uribuildertiny.URIBuilderTiny

/**
 * Generic database configuration suitable for most database drivers.
 */
@kotlinx.serialization.Serializable
data class DatabaseConfiguration constructor(
  /**
   * Name of the JDBC database driver.
   */
  val driver: String,

  /**
   * The associated URI scheme (protocol) for this JDBC driver.
   */
  val scheme: String,

  /**
   * IP address of the database server.
   */
  val ipAddr: String,

  /**
   * Host name of the database server.
   */
  val host: String,

  /**
   * Database server's connection port.
   */
  val port: Int,

  /**
   * Username to authenticate against the database server.
   */
  val user: String,

  /**
   * Password to authenticate against the database server.
   */
  val pass: String,

  /**
   * Main database schema name to attach to.
   */
  val schema: String
) {
  /**
   * Returns the connection URI string for this instance.
   */
  val uri: String = uri(driver, host, port, user, pass, schema)

  companion object {
    /**
     * Default collation is UTF-8, general and case-insensitive.
     */
    const val DEFAULT_COLLATION: String = "utf8mb4_general_ci"

    /**
     * How long in milliseconds until a client can connect to a server.
     */
    const val DEFAULT_CONNECT_TIMEOUT: Long = 2_000L

    /**
     * How long in milliseconds until a socket can connect to a destination.
     */
    const val DEFAULT_SOCKET_TIMEOUT: Long = 2_000L

    /**
     * Sets the timezone of the server.
     */
    const val DEFAULT_TIMEZONE: String = "UTC"

    /**
     * Returns the connection URI string based in input parameters.
     *
     * @see <a href="https://tinyurl.com/yagm2clw">Connector/J Configuration Properties</a>
     */
    @Suppress("LongParameterList")
    fun uri(
      scheme: String,
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
      .setScheme(scheme)
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
  }
}

/**
 * Generic database configuration for file-based migrations.
 */
@kotlinx.serialization.Serializable
data class DatabaseMigrationConfiguration constructor(
  /**
   * Name of the JDBC database driver.
   */
  val driver: String,

  /**
   * The associated URI scheme (protocol) for this JDBC driver.
   */
  val scheme: String,

  /**
   * IP address of the database server.
   */
  val ipAddr: String,

  /**
   * Host name of the database server.
   */
  val host: String,

  /**
   * Database server's connection port.
   */
  val port: Int,

  /**
   * Username to authenticate against the database server.
   */
  val user: String,

  /**
   * Password to authenticate against the database server.
   */
  val pass: String,

  /**
   * Main database schema name to attach to.
   */
  val schema: String,

  /**
   * Where the migrations plans are stored.
   */
  val basePath: String
) {
  /**
   * Returns the connection URI string for this instance.
   */
  val uri: String = DatabaseConfiguration.uri(scheme, host, port, user, pass, schema)
}

/**
 * Generic database configuration for pool-backed database server.
 */
@kotlinx.serialization.Serializable
data class DatabasePoolConfiguration constructor(
  /**
   * Name of the JDBC database driver.
   */
  val driver: String,

  /**
   * The associated URI scheme (protocol) for this JDBC driver.
   */
  val scheme: String,

  /**
   * IP address of the database server.
   */
  val ipAddr: String,

  /**
   * Host name of the database server.
   */
  val host: String,

  /**
   * Database server's connection port.
   */
  val port: Int,

  /**
   * Username to authenticate against the database server.
   */
  val user: String,

  /**
   * Password to authenticate against the database server.
   */
  val pass: String,

  /**
   * Main database schema name to attach to.
   */
  val schema: String,

  /**
   * The connection pool configuration.
   */
  val pool: PoolConfiguration
) {
  /**
   * Returns the connection URI string for this instance.
   */
  val uri: String = DatabaseConfiguration.uri(scheme, host, port, user, pass, schema)
}

/**
 * Database pool configuration.
 */
@kotlinx.serialization.Serializable
data class PoolConfiguration constructor(
  /**
   * How long, in milliseconds, to keep an idle connection open before closing if.
   */
  val ageMillis: Long,

  /**
   * How many concurrent open connections to keep open.
   */
  val maxFree: Int,

  /**
   * How many milliseconds between health checks are done on the database connection.
   */
  val healthCheckMillis: Long
)
