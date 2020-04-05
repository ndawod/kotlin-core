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

package org.noordawod.kotlin.security

/**
 * Hashes a [String] value using [SipHash] and returns the value as a [ByteArray] on success,
 * an empty [ByteArray] otherwise.
 */
fun String?.byteArray(siphash: SipHash, trim: Boolean = true): ByteArray =
  siphash.asByteArrayOr(if (trim) this?.trim() else this)

/**
 * Returns a [ByteArray] representation of this [Base62]-encoded [String] value.
 */
fun String.base62(): ByteArray = ByteUtils.fromBase62(this)

/**
 * Returns a [ByteArray] representation of this [Base62]-encoded [String] value on
 * success, null otherwise.
 */
fun String?.base62(@Suppress("UNUSED_PARAMETER") jvm: ByteArray = byteArrayOf()): ByteArray? =
  if (null == this) null else ByteUtils.fromBase62(this)

/**
 * Hashes a [ByteArray] using [SipHash] and returns the value as a hexadecimal [String].
 */
fun ByteArray.hashId(siphash: SipHash): String = siphash.asHex(this)

/**
 * Hashes an optional [ByteArray] using [SipHash] and returns the value as a
 * hexadecimal [String] on success, null otherwise.
 */
fun ByteArray?.hashIdOrNull(siphash: SipHash): String? =
  if (null == this) null else siphash.asHex(this)

/**
 * Hashes an optional [ByteArray] using [SipHash] and returns the value as a
 * hexadecimal [String] on success, an empty [String] otherwise.
 */
fun ByteArray?.hashIdOrEmpty(siphash: SipHash): String =
  if (null == this) "" else siphash.asHex(this)

/**
 * Returns a [Base62] representation of this [ByteArray] value.
 */
fun ByteArray.base62(): String = ByteUtils.toBase62(this)

/**
 * Returns an optional [Base62] representation of this [ByteArray] value on success,
 * null otherwise.
 */
fun ByteArray?.base62(@Suppress("UNUSED_PARAMETER") jvm: String = ""): String? =
  if (null == this) null else ByteUtils.toBase62(this)
