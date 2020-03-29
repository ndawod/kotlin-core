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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "MagicNumber")

package org.noordawod.kotlin.security

import java.nio.charset.Charset

/**
 * A class that provides an implementation for Base62 encoding.
 */
@Suppress("TooManyFunctions")
class Base62 {
  /**
   * Base62 provider implementation using the GMP implementation.
   */
  private val instance = io.seruco.encoding.base62.Base62.createInstanceWithGmpCharacterSet()

  /**
   * Returns a Base62-encoded [ByteArray] that represents the provided [bytes] on success,
   * null otherwise.
   */
  fun encode(bytes: ByteArray): ByteArray = instance.encode(bytes)

  /**
   * Returns a Base62-encoded [String] that represents the provided [bytes] on success,
   * null otherwise.
   */
  fun encodeString(bytes: ByteArray): String = String(encode(bytes), Charsets.ISO_8859_1)

  /**
   * Returns a Base62-encoded [String] that represents the provided UTF-8-encoded
   * [message] on success, null otherwise.
   */
  fun encodeMessage(message: String): String = encodeMessage(message, Charsets.UTF_8)

  /**
   * Returns a Base62-encoded [String] that represents the provided [charset]-encoded
   * [message] on success, null otherwise.
   */
  fun encodeMessage(message: String, charset: Charset): String = encodeInternal(
    message,
    charset,
    Charsets.ISO_8859_1
  )

  /**
   * Returns a Base62-encoded [String] that represents the provided
   * [Charsets.ISO_8859_1]-encoded [hex] on success, null otherwise.
   */
  fun encodeHex(hex: String): String = encodeInternal(
    hex,
    Charsets.ISO_8859_1,
    Charsets.ISO_8859_1
  )

  /**
   * Decodes the Base62-encoded [bytes] and returns a [ByteArray] that represents the
   * original decoded bytes on success, null otherwise.
   */
  fun decode(bytes: ByteArray): ByteArray = instance.decode(bytes)

  /**
   * Decodes the Base62-encoded [string] and returns a [ByteArray] that represents the
   * original decoded bytes on success, null otherwise.
   */
  fun decodeString(string: String): ByteArray =
    decode(string.toByteArray(Charsets.ISO_8859_1))

  /**
   * Decodes the Base62-encoded [message] and returns a UTF-8-encoded [String] that
   * represents the original decoded bytes on success, null otherwise.
   */
  fun decodeMessage(message: String): String = decodeMessage(message, Charsets.UTF_8)

  /**
   * Decodes the Base62-encoded [message] and returns a [charset]-encoded [String] that
   * represents the original decoded bytes on success, null otherwise.
   */
  fun decodeMessage(message: String, charset: Charset): String = decodeInternal(
    message,
    Charsets.ISO_8859_1,
    charset
  )

  /**
   * Decodes the Base62-encoded [hex] and returns a [Charsets.ISO_8859_1]-encoded
   * [String] that represents the original hexadecimal string on success, null otherwise.
   */
  fun decodeHex(hex: String): String = decodeInternal(
    hex,
    Charsets.ISO_8859_1,
    Charsets.ISO_8859_1
  )

  private fun encodeInternal(string: String, source: Charset, target: Charset): String =
    String(instance.encode(string.toByteArray(source)), target)

  private fun decodeInternal(string: String, source: Charset, target: Charset): String =
    String(decode(string.toByteArray(source)), target)

  companion object {
    /**
     * The alphabet (GMP) as a [String] this class is using to encode and decode Base62 data.
     */
    const val ALPHABET: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    /**
     * The alphabet (GMP) as a [ByteArray] this class is using to encode and decode Base62 data.
     */
    val ALPHABET_BYTES: ByteArray = ALPHABET.toByteArray()
  }
}
