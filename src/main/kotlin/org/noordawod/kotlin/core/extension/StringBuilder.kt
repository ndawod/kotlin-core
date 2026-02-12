/*
 * The MIT License
 *
 * Copyright 2026 Noor Dawod. All rights reserved.
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

@file:Suppress("unused", "MagicNumber")

package org.noordawod.kotlin.core.extension

/**
 * Appends a numeric value to this [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendIfValid(value: Number?): StringBuilder {
  if (null != value) {
    append(value)
  }

  return this
}

/**
 * Appends a numeric value, following by a new-line character, to this [StringBuilder]
 * if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendLineIfValid(value: Number?): StringBuilder {
  if (null != value) {
    append(value)
    appendLine()
  }

  return this
}

/**
 * Appends a character sequence value to this [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendIfValid(value: CharSequence?): StringBuilder {
  val valueNormalized = value.trimOrNull()

  if (null != valueNormalized) {
    append(valueNormalized)
  }

  return this
}

/**
 * Appends a character sequence value, following by a new-line character, to this
 * [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendLineIfValid(value: CharSequence?): StringBuilder {
  val valueNormalized = value.trimOrNull()

  if (null != valueNormalized) {
    append(valueNormalized)
    appendLine()
  }

  return this
}

/**
 * Appends a boolean value to this [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendIfValid(value: Boolean?): StringBuilder {
  if (null != value) {
    append(value)
  }

  return this
}

/**
 * Appends a boolean value, following by a new-line character, to this
 * [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendLineIfValid(value: Boolean?): StringBuilder {
  if (null != value) {
    append(value)
    appendLine()
  }

  return this
}

/**
 * Appends an enum value to this [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun <T : Enum<T>> StringBuilder.appendIfValid(value: Enum<T>?): StringBuilder =
  appendIfValid(value?.toString())

/**
 * Appends an enum value, following by a new-line character, to this [StringBuilder]
 * if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun <T : Enum<T>> StringBuilder.appendLineIfValid(value: Enum<T>?): StringBuilder =
  appendLineIfValid(value?.toString())

/**
 * Appends an iteration of enum values to this [StringBuilder] if they're valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun <T : Enum<T>> StringBuilder.appendIfValid(value: Iterable<Enum<T>?>?): StringBuilder {
  if (null != value) {
    val valueList = value.mapNotNull { it?.toString().trimOrNull() }
    for (row in valueList) {
      appendIfValid(row)
    }
  }

  return this
}

/**
 * Appends an iteration of enum values, following by a new-line character, to this
 * [StringBuilder] if they're valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun <T : Enum<T>> StringBuilder.appendLineIfValid(value: Iterable<Enum<T>?>?): StringBuilder {
  if (null != value) {
    val valueList = value.mapNotNull { it?.toString().trimOrNull() }
    for (row in valueList) {
      appendLineIfValid(row)
    }
  }

  return this
}

/**
 * Appends an array of enum values to this [StringBuilder] if they're valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun <T : Enum<T>> StringBuilder.appendIfValid(value: Array<Enum<T>?>?): StringBuilder {
  if (null != value) {
    val valueList = value.mapNotNull { it?.toString().trimOrNull() }
    for (row in valueList) {
      appendIfValid(row)
    }
  }

  return this
}

/**
 * Appends an array of enum values, following by a new-line character, to this
 * [StringBuilder] if they're valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun <T : Enum<T>> StringBuilder.appendLineIfValid(value: Array<Enum<T>?>?): StringBuilder {
  if (null != value) {
    val valueList = value.mapNotNull { it?.toString().trimOrNull() }
    for (row in valueList) {
      appendLineIfValid(row)
    }
  }

  return this
}

/**
 * Appends a [Date][java.util.Date] value to this [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendIfValid(value: java.util.Date?): StringBuilder = appendIfValid(value?.time)

/**
 * Appends a [Date][java.util.Date], following by a new-line character, to this
 * [StringBuilder] if it's valid (non-null).
 *
 * @param value the value to append, if valid
 */
fun StringBuilder.appendLineIfValid(value: java.util.Date?): StringBuilder =
  appendLineIfValid(value?.time)
