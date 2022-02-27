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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "LongParameterList")

package org.noordawod.kotlin.core.config

/**
 * Describes the configuration of an SMTP server and how to connect to it.
 *
 * @param host host name of mail server
 * @param port [host] port to connect to
 * @param ssl whether the [host] supports SSL or not
 * @param auth optional connection authentication configuration
 * @param emails optional list of commonly-used email addresses
 * @param logging whether to log sending emails or not
 */
@kotlinx.serialization.Serializable
open class SmtpConfiguration constructor(
  val host: String,
  val port: Int,
  val ssl: Boolean? = false,
  val auth: SmtpAuthConfiguration? = null,
  val emails: SmtpEmails? = null,
  val logging: Boolean? = false
) {
  override fun equals(other: Any?): Boolean = other is SmtpConfiguration &&
    other.host == host &&
    other.port == port &&
    other.ssl == ssl &&
    other.auth == auth &&
    other.emails == emails &&
    other.logging == logging

  @Suppress("MagicNumber")
  override fun hashCode(): Int = port +
    host.hashCode() * 1609 +
    ssl.hashCode() * 947 +
    auth.hashCode() * 457 +
    emails.hashCode() * 1627 +
    logging.hashCode() * 3499
}

/**
 * Describes the authentication required by an SMTP server in order to be able to send emails.
 *
 * @param user authentication username
 * @param pass authentication password
 */
@kotlinx.serialization.Serializable
open class SmtpAuthConfiguration constructor(
  val user: String,
  val pass: String
) {
  override fun equals(other: Any?): Boolean = other is SmtpAuthConfiguration &&
    other.user == user &&
    other.pass == pass

  @Suppress("MagicNumber")
  override fun hashCode(): Int = user.hashCode() * 349 + pass.hashCode() * 1609
}

/**
 * List of common email addresses used in our website.
 *
 * @param sender email address to use for "Return-Path:" envelope header
 * @param replyTo email address to use for the "Reply-To:" message header
 * @param feedback optional email address to receive feedback
 * @param account optional email address for account-related messages
 * @param newsletter optional email address for newsletter-related messages
 * @param investor optional email address for investor-related messages
 * @param media optional email address for media-related messages
 * @param privacy optional email address for privacy-related messages
 * @param support optional email address for support-related messages
 */
@kotlinx.serialization.Serializable
open class SmtpEmails(
  val sender: String,
  val replyTo: String,
  val feedback: String? = null,
  val account: String? = null,
  val newsletter: String? = null,
  val investor: String? = null,
  val media: String? = null,
  val privacy: String? = null,
  val support: String? = null
) {
  override fun equals(other: Any?): Boolean = other is SmtpEmails &&
    other.sender == sender &&
    other.replyTo == replyTo &&
    other.feedback == feedback &&
    other.account == account &&
    other.newsletter == newsletter &&
    other.investor == investor &&
    other.media == media &&
    other.privacy == privacy &&
    other.support == support

  @Suppress("MagicNumber")
  override fun hashCode(): Int = sender.hashCode() +
    replyTo.hashCode() * 1609 +
    feedback.hashCode() * 947 +
    account.hashCode() * 457 +
    newsletter.hashCode() * 1627 +
    investor.hashCode() * 3499 +
    media.hashCode() * 79 +
    privacy.hashCode() * 419 +
    support.hashCode() * 709
}
