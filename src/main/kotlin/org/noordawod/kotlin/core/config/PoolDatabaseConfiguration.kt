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

package org.noordawod.kotlin.core.config

/**
 * Generic database configuration for pool-backed database server.
 */
open class PoolDatabaseConfiguration constructor(
  driver: String,
  host: String,
  port: Int,
  user: String,
  pass: String,
  db: String,
  val pool: ConnectionPoolConfiguration
) : DatabaseConfiguration(driver, host, port, user, pass, db)

/**
 * Database configuration suitable for MySQL server.
 */
open class MySqlPoolDatabaseConfiguration constructor(
  driver: String,
  host: String,
  port: Int,
  user: String,
  pass: String,
  db: String,
  val pool: ConnectionPoolConfiguration
) : MySqlDatabaseConfiguration(driver, host, port, user, pass, db)

/**
 * A special [MySqlDatabaseConfiguration] that uses the
 * [DatabaseConfiguration.DEFAULT_ENCODING] encoding and
 * [DatabaseConfiguration.EXTENDED_COLLATION] collation.
 */
open class Utf8Mb4PoolDatabaseConfiguration constructor(
  driver: String,
  host: String,
  port: Int,
  user: String,
  pass: String,
  db: String,
  val pool: ConnectionPoolConfiguration
) : MySqlDatabaseConfiguration(driver, host, port, user, pass, db) {
  override val encoding: String = DEFAULT_ENCODING
  override val collation: String = EXTENDED_COLLATION
}

/**
 * Database pool configuration.
 */
data class ConnectionPoolConfiguration constructor(
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
