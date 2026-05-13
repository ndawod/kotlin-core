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
 * Thrown when a class cannot be encoded as JSON.
 *
 * @param message error message explaining the error
 * @param cause optional cause of the error
 */
class JsonEncodingError(
  message: String,
  cause: Throwable? = null,
) : Throwable(
  message = message,
  cause = cause,
)
