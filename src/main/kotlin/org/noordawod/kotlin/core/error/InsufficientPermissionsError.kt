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
 * Thrown exception if the remote user has insufficient permissions to perform an action.
 *
 * @param message human-friendly error message
 * @param klass the class trying to perform the action
 * @param host requested endpoint's host name
 * @param port requested endpoint's port
 * @param method requested endpoint's method
 * @param path requested endpoint's path
 * @param cause optional exception that caused this error
 */
class InsufficientPermissionsError(
  message: String,
  val klass: Class<*>,
  val host: String,
  val port: Int,
  val method: String,
  val path: String,
  cause: Throwable? = null,
) : Throwable(
  message = message,
  cause = cause,
) {
  override fun toString(): String = "$message [" +
    "class=${klass.simpleName} " +
    "host=$host, " +
    "port=$port, " +
    "method=$method, " +
    "path=$path]"
}
