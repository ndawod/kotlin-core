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

/**
 * Generic database configuration suitable for most database drivers.
 */
open class DatabaseConfiguration constructor(
  val driver: String,
  val host: String,
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
  user: String,
  pass: String,
  db: String,
  val encoding: String,
  val charset: String,
  val collation: String
) : DatabaseConfiguration(driver, host, user, pass, db) {
  /**
   * Returns the connection URI string.
   */
  val uri: String = "mysql://$host/$db" +
    "?user=$user" +
    "&password=$pass" +
    "&useUnicode=true" +
    "&characterEncoding=$encoding" +
    "&useSSL=false" +
    "&useCompression=false" +
    "&connectTimeout=2000" +
    "&socketTimeout=2000" +
    "&autoReconnect=true" +
    "&serverTimezone=UTC"
}

/**
 * A special [MySqlDatabaseConfiguration] that uses the "utf8" encoding and "utf8mb4"
 * character set.
 */
open class Utf8Mb4DatabaseConfiguration constructor(
  driver: String,
  host: String,
  user: String,
  pass: String,
  db: String
) : MySqlDatabaseConfiguration(
  driver, host, user, pass, db,
  "utf8", "utf8mb4", "utf8mb4_general_ci"
)
