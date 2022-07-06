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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core.config

/**
 * A configuration describing a URI consisting of protocol, host, port and base path.
 *
 * @param protocol the protocol part, such as "https"
 * @param host the host name part, such as "www.example.com"
 * @param port the port part, if known or non-standard, or 0 to use standard one per protocol
 * @param path the path part, such as "/path/to/file.html"
 */
@kotlinx.serialization.Serializable
open class UriConfiguration constructor(
  val protocol: String,
  val host: String,
  val port: Int = 0,
  val path: String = "/"
) {
  @Suppress("MagicNumber")
  override fun toString(): String {
    val builder = StringBuilder(128)
    if (host.isNotBlank()) {
      if (protocol.isNotBlank()) {
        builder.append(protocol)
        builder.append(":")
      }
      builder.append("//")
      builder.append(host)

      // If the port is unusual (not 80 for http, and not 443 for https), we'll add it.
      @Suppress("ComplexCondition", "MagicNumber")
      if (
        0 != port &&
        (443 != port || !"https".equals(protocol, ignoreCase = true)) &&
        (80 != port || !"http".equals(protocol, ignoreCase = true))
      ) {
        builder.append(":")
        builder.append(port)
      }
    }
    if (path.isNotBlank()) {
      builder.append(path)
    }
    return builder.toString()
  }

  override fun equals(other: Any?): Boolean = other is UriConfiguration &&
    other.protocol == protocol &&
    other.host == host &&
    other.port == port &&
    other.path == path

  @Suppress("MagicNumber")
  override fun hashCode(): Int = port +
    protocol.hashCode() * 1609 +
    host.hashCode() * 2269 +
    path.hashCode() * 947
}
