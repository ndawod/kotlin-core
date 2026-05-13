/*
 * COPYRIGHT (C) PAPER KITE SYSTEMS I.K.E. ALL RIGHTS RESERVED.
 * UNAUTHORIZED DUPLICATION, MODIFICATION OR PUBLICATION IS PROHIBITED.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF PAPER KITE SYSTEMS I.K.E.
 * THE COPYRIGHT NOTICE ABOVE DOES NOT EVIDENCE ANY ACTUAL OR INTENDED
 * PUBLICATION OF SUCH SOURCE CODE.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core.error

/**
 * Thrown when data is immutable.
 *
 * @param message error message explaining the error
 * @param data the human-readable form of the data or its parameter
 * @param statusCode optional HTTP status code for the error
 * @param cause optional cause of the error
 */
class DataImmutableError(
  message: String,
  val data: String,
  var statusCode: Int? = null,
  cause: Throwable? = null,
) : Throwable(
  message = message,
  cause = cause,
) {
  constructor(
    message: String,
    statusCode: Int? = null,
    cause: Throwable? = null,
  ) : this(
    message = message,
    data = "",
    statusCode = statusCode,
    cause = cause,
  )
}
