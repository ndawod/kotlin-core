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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "SameParameterValue")

package org.noordawod.kotlin.security

import java.nio.charset.Charset

/**
 * Helper functions to work with contents encoded in Base64, that needs decoding from Base64.
 */
object Base64 {
  fun encode(bytes: ByteArray): ByteArray = java.util.Base64.getEncoder().encode(bytes)

  fun encodeBinary(bytes: ByteArray): String =
    String(encode(bytes), Charsets.ISO_8859_1)

  fun encodeMessage(message: String, charset: Charset = Charsets.UTF_8): String =
    encodeInternal(message, charset, Charsets.ISO_8859_1)

  fun decode(bytes: ByteArray): ByteArray =
    java.util.Base64.getDecoder().decode(bytes)

  fun decodeBinary(string: String): ByteArray =
    decode(string.toByteArray(Charsets.ISO_8859_1))

  fun decodeMessage(string: String): String =
    decodeMessage(string, Charsets.UTF_8)

  fun decodeMessage(message: String, charset: Charset): String =
    decodeInternal(message, Charsets.ISO_8859_1, charset)

  private fun encodeInternal(string: String, source: Charset, target: Charset): String =
    String(encode(string.toByteArray(source)), target)

  private fun decodeInternal(string: String, source: Charset, target: Charset): String =
    String(decode(string.toByteArray(source)), target)
}
