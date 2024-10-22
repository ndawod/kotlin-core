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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "MagicNumber")

package org.noordawod.kotlin.core.security

/**
 * An implementation of SipHashes algorithm for hashing short, arbitrary data. Adapted from
 * original code written by Forward Computing and Control Pty. Ltd. (www.forward.com.au).
 *
 * Note: this class is NOT thread-safe.
 */
class SipHash internal constructor(
  private val key: ByteArray,
) {
  private var value0: Long = 0
  private var value1: Long = 0
  private var value2: Long = 0
  private var value3: Long = 0
  private var byteCounter: Byte = 0 // hold number of msg bytes % 256
  private var accumulator: Long = 0 // accumulates bytes until we have 8 in number
  private var accumulatorCount = 0 // counter of bytes being accumulated

  /**
   * Calculates the SipHash of [bytes] and returns the result as a [Long].
   *
   * Note: this method is NOT thread safe.
   */
  fun compute(bytes: ByteArray): Long {
    reset()

    for (aByte in bytes) {
      updateHash(aByte)
    }

    return finish() // result in v0
  }

  /**
   * Calculates the SipHash of [stream] and returns the result as a [Long].
   *
   * Note: this method is NOT thread safe.
   */
  @Throws(java.io.IOException::class)
  fun compute(stream: java.io.InputStream): Long {
    reset()

    var aByte = stream.read()

    while (-1 != aByte) {
      updateHash(aByte.toByte())
      aByte = stream.read()
    }

    return finish() // result in v0
  }

  /**
   * Calculates the SipHash of [string] and returns the result as a [ByteArray].
   *
   * Note: this method is NOT thread safe.
   */
  fun asByteArray(
    string: String,
    ignoreCase: Boolean = true,
  ): ByteArray {
    val normalized = if (ignoreCase) string.lowercase() else string

    return asByteArray(normalized.toByteArray())
  }

  /**
   * Calculates the SipHash of [string] and returns the result as a [ByteArray].
   *
   * Note: this method is NOT thread safe.
   */
  fun asByteArrayOr(
    string: String?,
    ignoreCase: Boolean = true,
    fallback: ByteArray = byteArrayOf(),
  ): ByteArray = if (string.isNullOrEmpty()) {
    fallback
  } else {
    val normalized = if (ignoreCase) string.lowercase() else string
    asByteArrayOr(normalized.toByteArray())
  }

  /**
   * Calculates the SipHash of [bytes] and returns the result as a [ByteArray].
   *
   * Note: this method is NOT thread safe.
   */
  fun asByteArray(bytes: ByteArray): ByteArray = SipHashFactory.longToBytes(compute(bytes))

  /**
   * Hashes the specified [bytes] and returns the result as a [ByteArray].
   */
  fun asByteArrayOr(
    bytes: ByteArray?,
    fallback: ByteArray = byteArrayOf(),
  ): ByteArray = if (true == bytes?.isNotEmpty()) {
    SipHashFactory.longToBytes(compute(bytes))
  } else {
    fallback
  }

  /**
   * Calculates the SipHash of [number] and returns the result as a [ByteArray].
   *
   * Note: this method is NOT thread safe.
   */
  fun asByteArray(number: Long): ByteArray = asByteArrayOr(SipHashFactory.longToBytes(number))

  /**
   * Calculates the SipHash of [] and returns the result as a [ByteArray].
   *
   * Note: this method is NOT thread safe.
   */
  fun asByteArray(number: Number): ByteArray = asByteArray(number.toLong())

  /**
   * Calculates the SipHash of [] and returns the result as a [ByteArray].
   *
   * Note: this method is NOT thread safe.
   */
  @Throws(java.io.IOException::class)
  fun asByteArray(stream: java.io.InputStream): ByteArray =
    SipHashFactory.longToBytes(compute(stream))

  /**
   * Calculates the SipHash of [bytes] and returns the result as a hexadecimal [String].
   *
   * Note: this method is NOT thread safe.
   */
  fun asHex(bytes: ByteArray): String = SipHashFactory.toHex(asByteArrayOr(bytes))

  /**
   * Calculates the SipHash of [string] and returns the result as a hexadecimal [String].
   *
   * Note: this method is NOT thread safe.
   */
  fun asHex(
    string: String?,
    ignoreCase: Boolean = true,
  ): String? = if (string.isNullOrEmpty()) {
    null
  } else {
    val normalized = if (ignoreCase) string.lowercase() else string
    SipHashFactory.toHex(asByteArrayOr(normalized.toByteArray()))
  }

  /**
   * Calculates the SipHash of [number] and returns the result as a hexadecimal [String].
   *
   * Note: this method is NOT thread safe.
   */
  fun asHex(number: Long): String = SipHashFactory.toHex(asByteArray(number))

  /**
   * Calculates the SipHash of [number] and returns the result as a hexadecimal [String].
   *
   * Note: this method is NOT thread safe.
   */
  fun asHex(number: Number): String = SipHashFactory.toHex(asByteArray(number))

  /**
   * Calculates the SipHash of [stream] and returns the result as a hexadecimal [String].
   *
   * Note: this method is NOT thread safe.
   */
  @Throws(java.io.IOException::class)
  fun asHex(stream: java.io.InputStream): String = SipHashFactory.toHex(asByteArray(stream))

  /**
   * The current state of hash, v0,v1,v2,v3, as hex digits in BigEndian format.
   */
  override fun toString(): String {
    var result = ""
    var bytes: ByteArray = convertToBytes(value0)
    var hexStr: String = SipHashFactory.toHex(
      bytes = bytes,
      offset = 0,
      length = bytes.size,
    )

    result += "v0=$hexStr "
    bytes = convertToBytes(value1)
    hexStr = SipHashFactory.toHex(
      bytes = bytes,
      offset = 0,
      length = bytes.size,
    )

    result += "v1=$hexStr "
    bytes = convertToBytes(value2)
    hexStr = SipHashFactory.toHex(
      bytes = bytes,
      offset = 0,
      length = bytes.size,
    )

    result += "v2=$hexStr "
    bytes = convertToBytes(value3)
    hexStr = SipHashFactory.toHex(
      bytes = bytes,
      offset = 0,
      length = bytes.size,
    )

    result += "v3=$hexStr "

    return result
  }

  private fun reset() {
    value0 = 0x736f6d6570736575L
    value1 = 0x646f72616e646f6dL
    value2 = 0x6c7967656e657261L
    value3 = 0x7465646279746573L
    val long0 = SipHashFactory.bytesToLong(key, 0)
    val long1 = SipHashFactory.bytesToLong(key, 8)
    value0 = value0 xor long0
    value1 = value1 xor long1
    value2 = value2 xor long0
    value3 = value3 xor long1
    byteCounter = 0 // to track number of msg byte % 256
  }

  /**
   * Add a byte to the hash and increment the message length count % 256.
   *
   * The bytes are accumulated until there are 8 of them and then the
   * corresponding long (read from the bytes as LittleEndian format) is added to
   * the hash.
   */
  private fun updateHash(byte: Byte) {
    byteCounter++ // mod 256 since this counter is only 1 byte
    accumulator = accumulator or (byte.toLong() and 0xFF shl accumulatorCount * 8)
    accumulatorCount++

    if (accumulatorCount >= ByteUtils.LONG_BYTES) {
      // hash it now
      value3 = value3 xor accumulator
      sipHashRound()
      sipHashRound()
      value0 = value0 xor accumulator
      // clear counter and long
      accumulatorCount = 0
      accumulator = 0
    }
  }

  /**
   * Call this to complete the hash by processing any remaining bytes and adding
   * the msg length.
   *
   * This method returns the hash as long. Use one of the utility methods,
   * longToByteLE or longToByte to convert the long to bytes in LittleEndian or
   * BigEndian format respectively and then use toHex to convert the [ByteArray] to a
   * string of hex digits for display or transmission.
   */
  private fun finish(): Long {
    val oldByteCounter = byteCounter

    // Pad out the last long with zeros, leave one space for the message length % 256
    while (accumulatorCount < ByteUtils.LONG_BYTES - 1) { // leave one byte for length
      updateHash(0.toByte())
    }

    // add the message length this will force the last long to be added to the hash
    updateHash(oldByteCounter)

    // finish off the hash
    value2 = value2 xor 0xFF
    sipHashRound()
    sipHashRound()
    sipHashRound()
    sipHashRound()

    // calculate the final result overwrites v0
    val result = value0 xor value1 xor value2 xor value3
    reset()

    return result
  }

  /**
   * Preform a siphash_round() on the current state.
   */
  private fun sipHashRound() {
    value0 += value1
    value2 += value3
    value1 = rotateLeft(value1, 13)
    value3 = rotateLeft(value3, 16)
    value1 = value1 xor value0
    value3 = value3 xor value2
    value0 = rotateLeft(value0, 32)
    value2 += value1
    value0 += value3
    value1 = rotateLeft(value1, 17)
    value3 = rotateLeft(value3, 21)
    value1 = value1 xor value2
    value3 = value3 xor value0
    value2 = rotateLeft(value2, 32)
  }

  /**
   * Static functions, constants and other values.
   */
  companion object {
    /**
     * Rotate long left by shift bits. bits rotated off to the left are put back
     * on the right.
     */
    private fun rotateLeft(
      number: Long,
      shift: Int,
    ): Long = number shl shift or number ushr 64 - shift

    /**
     * Convert a long to bytes in BigEndian format (Java is a BigEndian platform).
     */
    private fun convertToBytes(value: Long): ByteArray {
      val bytes = ByteArray(8)

      for (loop in 0..7) {
        bytes[7 - loop] = (value ushr 8 * loop and 0xFF).toByte()
      }

      return bytes
    }
  }
}

/**
 * Helper extension function to convert a [String] to a [ByteArray] suitable for use as a
 * [SipHash] key.
 */
fun String.toSipHashKey(): ByteArray {
  val result = toByteArray(Charsets.ISO_8859_1)

  require(16 == result.size) {
    "String must be encoded in ${Charsets.ISO_8859_1.name()} " +
      "and have 16 characters exactly."
  }

  return result
}

/**
 * Create a new [SipHash] object with a 16 byte key.
 */
class SipHashFactory(
  private val key: ByteArray,
) {
  init {
    require(16 == key.size) { "Key must be exactly 16 bytes long." }
  }

  /**
   * Returns a new [SipHash] instance.
   *
   * Note: [SipHash] is NOT thread-safe.
   */
  fun newInstance(): SipHash = SipHash(key)

  /**
   * Static functions, constants and other values.
   */
  companion object {
    /**
     * Converts [ByteArray] in LittleEndian format to a long.
     */
    fun bytesToLong(
      bytes: ByteArray,
      offset: Int,
    ): Long {
      require(bytes.size - offset >= 8) {
        "Less then 8 bytes starting from offset: $offset"
      }

      var result: Long = 0

      for (loop in 0..7) {
        result = result or (bytes[loop + offset].toLong() and 0xFF shl 8 * loop)
      }

      return result
    }

    /**
     * Convert a long to bytes in LittleEndian format.
     */
    fun longToBytes(value: Long): ByteArray {
      val bytes = ByteArray(8)

      for (loop in 0..7) {
        bytes[loop] = (value ushr 8 * loop and 0xFF).toByte()
      }

      return bytes
    }

    /**
     * Convert a [ByteArray] to Hex Digits.
     */
    fun toHex(
      bytes: ByteArray,
      offset: Int = 0,
      length: Int = bytes.size,
    ): String {
      require(bytes.size - offset >= length) {
        "Less then $length bytes starting from offset: $offset"
      }

      val buffer = CharArray(length * 2)
      var loop = 0
      var bufferPos = 0
      var anInt: Int

      while (loop < length) {
        anInt = bytes[offset + loop++].toInt()
        buffer[bufferPos++] = ByteUtils.HEX_CHARS[anInt ushr 4 and 0x0F]
        buffer[bufferPos++] = ByteUtils.HEX_CHARS[anInt and 0x0F]
      }

      return String(buffer)
    }
  }
}
