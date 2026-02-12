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

@file:Suppress("unused")

package org.noordawod.kotlin.core.util

/**
 * The different signatures of an SVG file.
 *
 * @param value a human-friendly value of this instance
 */
enum class SvgSignature(
  private val value: String,
) {
  /**
   * SVG v1.0.
   */
  V1_0("1.0"),

  /**
   * SVG v1.1.
   */
  V1_1("1.1"),

  /**
   * SVG v1.2.
   */
  V1_2("1.2"),
  ;

  override fun toString(): String = value
}

/**
 * Scans this String and treats it as SVG; removes any SVG headers from it and returns the
 * result.
 *
 * @param signature the SVG version, optional
 */
fun String?.normalizeSvgContent(signature: SvgSignature? = null): String? {
  val trimmed = this?.trim() ?: return null

  val contents = if (trimmed.startsWith(SVG_SIGNATURE_OPEN, ignoreCase = true)) {
    val closingTag = trimmed.indexOf(SVG_SIGNATURE_CLOSE, SVG_SIGNATURE_OPEN_LENGTH)
    if (0 > closingTag) {
      return null
    }
    trimmed.substring(closingTag + SVG_SIGNATURE_CLOSE_LENGTH)
  } else {
    this
  }

  if (!contents.startsWith(SVG_ELEMENT_START, ignoreCase = true)) {
    return null
  }

  return if (null == signature) {
    contents
  } else {
    "$SVG_SIGNATURE_OPEN version=\"$signature\" encoding=\"utf-8\"$SVG_SIGNATURE_CLOSE$contents"
  }
}

private const val SVG_SIGNATURE_OPEN: String = "<?xml"
private const val SVG_SIGNATURE_OPEN_LENGTH: Int = SVG_SIGNATURE_OPEN.length
private const val SVG_SIGNATURE_CLOSE: String = "?>"
private const val SVG_SIGNATURE_CLOSE_LENGTH: Int = SVG_SIGNATURE_CLOSE.length
private const val SVG_ELEMENT_START: String = "<svg"
