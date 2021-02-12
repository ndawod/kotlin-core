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

@file:Suppress("MemberVisibilityCanBePrivate", "MagicNumber")

package org.noordawod.kotlin.security

import java.util.concurrent.ThreadLocalRandom
import java.util.regex.Pattern
import javax.xml.bind.DatatypeConverter

/**
 * Internal singleton instance to handle [Base62] encoding and decoding.
 */
private val BASE62: Base62 = Base62()

/**
 * Helper methods for dealing with bytes and [ByteArray].
 */
@Suppress("TooManyFunctions")
object ByteUtils {
  /**
   * A secure random number generator.
   */
  internal val RANDOM: java.util.Random =
    java.security.SecureRandom(System.currentTimeMillis().toString().toByteArray())

  /**
   * How many bytes in one [Long].
   */
  const val LONG_BYTES: Int = java.lang.Long.SIZE / java.lang.Byte.SIZE

  /**
   * Contains a list of upper-case alphanumeric characters, as a [String].
   */
  const val HEX_STRING = "0123456789ABCDEF"

  /**
   * Contains a list of upper-case alphanumeric characters, as a [CharArray].
   */
  val HEX_CHARS: CharArray = HEX_STRING.toCharArray()

  /**
   * Contains a list of upper-case alphanumeric characters, as a [ByteArray].
   */
  val HEX_BYTES = HEX_STRING.toByteArray()

  /**
   * A case-insensitive [Pattern] that can match against hexadecimal input.
   */
  val HEX_PATTERN: Pattern = Pattern.compile("^[A-Fa-f0-9]+$")

  /**
   * Generates random bytes and returns them as a [ByteArray].
   */
  fun randomBytes(length: Int): ByteArray = ByteArray(length).apply {
    RANDOM.nextBytes(this)
  }

  /**
   * Fills the supplied [ByteArray] with random bytes.
   */
  fun randomBytes(bytes: ByteArray) {
    if (bytes.isNotEmpty()) {
      RANDOM.nextBytes(bytes)
    }
  }

  /**
   * Generates random characters from the supplied dictionary.
   */
  fun randomChars(dictionary: ByteArray, length: Int): ByteArray {
    val bytes = ByteArray(length)
    val random: ThreadLocalRandom = ThreadLocalRandom.current()
    var idx = 0
    while (length > idx) {
      bytes[idx] = dictionary[random.nextInt(0, dictionary.size)]
      idx++
    }
    return bytes
  }

  /**
   * Generates a random hexadecimal [ByteArray].
   */
  fun randomHex(length: Int, asBinary: Boolean): ByteArray =
    randomChars(HEX_BYTES, (if (asBinary) 2 else 1) * length)

  /**
   * Generates a random alpha-numeric string.
   */
  fun randomAlpha(length: Int): ByteArray = randomChars(Base62.ALPHABET_BYTES, length)

  /**
   * Tests whether a given [ByteArray] contains only hexadecimal characters.
   */
  fun isHex(bytes: ByteArray): Boolean {
    for (b in bytes) {
      if (-1 == HEX_STRING.indexOf(b.toChar())) {
        return false
      }
    }
    return true
  }

  /**
   * Tests whether a given string contains only hexadecimal characters.
   */
  fun isHex(value: String?): Boolean = null != value && HEX_PATTERN.matcher(value).lookingAt()

  /**
   * Encodes the given binary data to hexadecimal string.
   */
  fun toHex(bytes: ByteArray, escape: Boolean = false): String {
    val hex = DatatypeConverter.printHexBinary(bytes).toLowerCase(java.util.Locale.ENGLISH)
    return if (escape) "x'$hex'" else hex
  }

  fun toHex(entries: Array<ByteArray>): Array<String> = toHex(entries, false)

  fun toHex(entries: Array<ByteArray>, escape: Boolean): Array<String> {
    val result = Array(entries.size) { "" }
    var idx = -1
    while (entries.size > ++idx) {
      result[idx] = toHex(entries[idx], escape)
    }
    return result
  }

  fun toHex(entries: Collection<ByteArray>, escape: Boolean = false): Array<String> {
    val result = Array(entries.size) { "" }
    var idx = -1
    for (entry in entries) {
      result[++idx] = toHex(entry, escape)
    }
    return result
  }

  /**
   * Decodes an encoded, hexadecimal [ByteArray] into its binary data.
   */
  fun fromHex(bytes: ByteArray): ByteArray = fromHex(String(bytes, Charsets.US_ASCII))

  /**
   * Decodes an encoded, hexadecimal string into its binary data.
   */
  fun fromHex(str: String): ByteArray = DatatypeConverter.parseHexBinary(str)

  /**
   * Encodes the given binary data to a Base62 string.
   */
  fun toBase62(bytes: ByteArray): String = BASE62.encodeString(bytes)

  /**
   * Decodes a base62 encoded string into its binary data.
   */
  fun fromBase62(str: String): ByteArray = BASE62.decodeString(str)

  /**
   * Encodes the given binary data to a Base64 string.
   */
  fun toBase64(bytes: ByteArray): String = Base64.encodeBinary(bytes)

  /**
   * Decodes a base64 encoded string into its binary data.
   */
  fun fromBase64(str: String): ByteArray = Base64.decodeBinary(str)

  /**
   * Converts a given numeric value, which has no floating points, to a [ByteArray] on success,
   * null otherwise.
   */
  fun toByteArray(number: Number): ByteArray? {
    val length: Int
    val value: Long
    when (number) {
      is Byte -> {
        length = java.lang.Byte.SIZE shr 3
        value = number.toLong()
      }
      is Short -> {
        length = java.lang.Short.SIZE shr 3
        value = number.toLong()
      }
      is Int -> {
        length = Integer.SIZE shr 3
        value = number.toLong()
      }
      is Long -> {
        length = java.lang.Long.SIZE shr 3
        value = number
      }
      else -> return null
    }
    val array = ByteArray(length)
    for (pos in 0 until length) {
      array[pos] = (value shr (length - pos - 1 shl 3)).toByte()
    }
    return array
  }

  /**
   * Converts a [ByteArray] to a numeric value.
   */
  fun toLong(bytes: ByteArray): Long {
    // Max 8 bytes.
    val length = bytes.size
    if (length > 8) {
      return 0L
    }
    var value = 0L
    for (pos in 0 until length) {
      value = value or (0xFFL and bytes[pos].toLong() shl 8 * (length - pos - 1))
    }
    return value
  }

  /**
   * Searches the specified bytes for a character, returns its position if found, -1 otherwise.
   */
  fun indexOf(bytes: ByteArray, c: Byte): Int {
    val length = bytes.size
    var pos = -1
    while (length > ++pos) {
      if (c == bytes[pos]) {
        return pos
      }
    }
    return -1
  }

  /**
   * Searches the specified bytes for a character, returns its position if found, -1 otherwise.
   */
  fun indexOf(bytes: ByteArray, aChar: Char): Int {
    val length = bytes.size
    val aByte = aChar.toByte()
    var pos = -1
    while (length > ++pos) {
      if (aByte == bytes[pos]) {
        return pos
      }
    }
    return -1
  }
}

/**
 * Constants representing the common strengths defined in [ByteArrayStrength].
 */
object ByteArrayLength {
  /**
   * Length of a [ByteArray] that can accommodate a hash value using [SipHash].
   */
  const val SIPHASH: Int = 8

  /**
   * A [ByteArray] with a capacity of 12 bytes.
   */
  const val SHORTER: Int = 12

  /**
   * A [ByteArray] with a capacity of 16 bytes.
   */
  const val SHORT: Int = 16

  /**
   * A [ByteArray] with a capacity to hold either an IPv4 or IPv6 address.
   */
  const val IP_ADDR: Int = SHORT

  /**
   * A [ByteArray] with a capacity of 17 bytes, suitable for storing a MQTT client identifier.
   */
  const val MQTT: Int = 17

  /**
   * A [ByteArray] with a capacity of 20 bytes.
   */
  const val NORMAL: Int = 20

  /**
   * A [ByteArray] with a capacity of 32 bytes.
   */
  const val MEDIUM: Int = 32

  /**
   * A [ByteArray] with a capacity of 40 bytes.
   */
  const val LONG: Int = 40

  /**
   * A [ByteArray] with a capacity of 64 bytes.
   */
  const val SECRET: Int = 64
}

/**
 * Lengths of common [ByteArray] keys for storing hashes in database.
 *
 * @param length strength of this instance
 */
enum class ByteArrayStrength(val length: Int) {
  /**
   * Length of a [ByteArray] that can accommodate a hash value using [SipHash].
   */
  SIPHASH(ByteArrayLength.SIPHASH),

  /**
   * A [ByteArray] with a capacity of 12 bytes.
   */
  SHORTER(ByteArrayLength.SHORTER),

  /**
   * A [ByteArray] with a capacity of 16 bytes.
   */
  SHORT(ByteArrayLength.SHORT),

  /**
   * A [ByteArray] with a capacity to hold either an IPv4 or IPv6 address.
   */
  IP_ADDR(ByteArrayLength.IP_ADDR),

  /**
   * A [ByteArray] with a capacity of 17 bytes, suitable for storing a MQTT client identifier.
   */
  MQTT(ByteArrayLength.MQTT),

  /**
   * A [ByteArray] with a capacity of 20 bytes.
   */
  NORMAL(ByteArrayLength.NORMAL),

  /**
   * A [ByteArray] with a capacity of 32 bytes.
   */
  MEDIUM(ByteArrayLength.MEDIUM),

  /**
   * A [ByteArray] with a capacity of 40 bytes.
   */
  LONG(ByteArrayLength.LONG),

  /**
   * A [ByteArray] with a capacity of 64 bytes.
   */
  SECRET(ByteArrayLength.SECRET);

  override fun toString(): String = name

  companion object {
    /**
     * Returns a [ByteArrayStrength] for a valid [strength], [fallback] otherwise.
     */
    fun from(strength: Int, fallback: ByteArrayStrength = NORMAL): ByteArrayStrength {
      for (value in values()) {
        if (value.length == strength) {
          return value
        }
      }
      return fallback
    }
  }
}
