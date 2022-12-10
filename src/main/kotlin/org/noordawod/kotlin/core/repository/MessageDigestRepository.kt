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

@file:Suppress("unused")

package org.noordawod.kotlin.core.repository

import com.github.aelstad.keccakj.provider.Constants

/**
 * The type of public record identifier.
 */
typealias PublicId = String

/**
 * The type of internal record identifiers.
 */
typealias HashValue = ByteArray

/**
 * An alias for [Pair] consisting of a [String] and its [HashValue].
 */
typealias StringAndHashValue = Pair<String, HashValue>

/**
 * Corresponds to a [HashValue] that's 0-byte.
 */
val EmptyHashValue: HashValue = byteArrayOf()

/**
 * A repository contract to calculate the hash value of an arbitrary message using a variety
 * of [MessageDigest][java.security.MessageDigest] functions.
 */
interface MessageDigestRepository {
  /**
   * Calculates the hash of [value] using the provided [message digest][function] function.
   *
   * Before the hash of [value] is calculated, it's converted to a [String] by calling its
   * [toString] method.
   */
  fun digest(function: java.security.MessageDigest, value: Any): HashValue

  /**
   * Calculates the hash of [value] using the SHA3 algorithm with the provided [strength].
   *
   * Before the hash of [value] is calculated, it's converted to a [String] by calling its
   * [toString] method.
   */
  fun sha3(strength: Sha3Strength, value: Any): HashValue

  /**
   * Calculates the hash of [string] using the SHA3 algorithm with the
   * [Sha3Strength.SHA3_512] strength.
   */
  fun sha3(string: String): HashValue
}

/**
 * Defines the strengths of SHA3 hashing algorithm that we support.
 *
 * @param value human-friendly name for this enum
 * @param bits how many bits the final hash consumes
 */
@Suppress("MemberVisibilityCanBePrivate")
enum class Sha3Strength constructor(val value: String, val bits: Int) {
  /**
   * SHA3 algorithm with a strength of 224 bits (produces a 28 byte array).
   */
  // These values represent known number of bits per algorithm.
  @Suppress("MagicNumber")
  SHA3_224(Constants.SHA3_224, 224),

  /**
   * SHA3 algorithm with a strength of 256 bits (produces a 32 byte array).
   */
  // These values represent known number of bits per algorithm.
  @Suppress("MagicNumber")
  SHA3_256(Constants.SHA3_256, 256),

  /**
   * SHA3 algorithm with a strength of 384 bits (produces a 48 byte array).
   */
  // These values represent known number of bits per algorithm.
  @Suppress("MagicNumber")
  SHA3_384(Constants.SHA3_384, 384),

  /**
   * SHA3 algorithm with a strength of 512 bits (produces a 64 byte array).
   */
  // These values represent known number of bits per algorithm.
  @Suppress("MagicNumber")
  SHA3_512(Constants.SHA3_512, 512);
}
