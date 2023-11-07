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

package org.noordawod.kotlin.core.repository.impl

import com.github.aelstad.keccakj.fips202.SHA3_224
import com.github.aelstad.keccakj.fips202.SHA3_256
import com.github.aelstad.keccakj.fips202.SHA3_384
import com.github.aelstad.keccakj.fips202.SHA3_512
import org.noordawod.kotlin.core.repository.EmptyHashValue
import org.noordawod.kotlin.core.repository.HashValue
import org.noordawod.kotlin.core.repository.MessageDigestRepository
import org.noordawod.kotlin.core.repository.Sha3Strength

/**
 * A repository that can calculate the hash value of an arbitrary message using a variety
 * of [MessageDigest][java.security.MessageDigest] functions.
 *
 * @param salt the salt to apply to a string before calculating its hash
 */
internal class MessageDigestRepositoryImpl(
  private val salt: String,
) : MessageDigestRepository {
  private val saltLengthPlusOne: Int = salt.length + 1

  /**
   * Calculates the hash of [value], salted with [salt], using the provided
   * [message digest][function] function.
   *
   * Before the hash of [value] is calculated, it's converted to a [String] by calling its
   * [toString] method.
   *
   * The final string value to hash is composed of the following parts:
   *
   * First part of the salt + string value + remaining salt.
   *
   * The position at which the salt is divided into 2 parts is calculated based on the salt's
   * and string value's lengths, as follows:
   *
   * [String][value].length % [salt].length
   */
  override fun digest(function: java.security.MessageDigest, value: Any): HashValue {
    // Convert to a string always.
    val string = value.toString()
    val length = string.length

    // When there's no input value, return an empty byte array.
    if (1 > length) {
      return EmptyHashValue
    }

    // To allow true randomness and uniqueness of the input value and configured salt,
    // we shall cut the salt in half based on the value's length.
    // Afterwards, we'll calculate the hash of `first salt half` + `value` + `second salt half`.
    val moduloPos = length % saltLengthPlusOne
    val saltHalf1 = salt.substring(0, moduloPos)
    val saltHalf2 = salt.substring(moduloPos)

    return function.digest("$saltHalf1$string$saltHalf2".toByteArray(Charsets.UTF_8))
  }

  /**
   * Calculates the hash of [value], salted with [salt], using the SHA3 algorithm
   * with the provided [strength].
   *
   * Before the hash of [value] is calculated, it's converted to a [String] by calling its
   * [toString] method.
   */
  override fun sha3(strength: Sha3Strength, value: Any): HashValue = when (strength) {
    Sha3Strength.SHA3_224 -> SHA3_224()
    Sha3Strength.SHA3_256 -> SHA3_256()
    Sha3Strength.SHA3_384 -> SHA3_384()
    Sha3Strength.SHA3_512 -> SHA3_512()
  }.let {
    digest(it, value)
  }

  /**
   * Calculates the hash of [string], salted with [salt], using the SHA3 algorithm
   * with the [Sha3Strength.SHA3_512] strength.
   */
  override fun sha3(string: String): HashValue = sha3(Sha3Strength.SHA3_512, string)
}
