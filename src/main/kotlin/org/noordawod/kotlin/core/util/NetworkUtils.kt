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

import org.noordawod.kotlin.core.DEFAULT_CONNECT_TIMEOUT
import org.noordawod.kotlin.core.extension.mutableListWith

/**
 * Utility functions for networking.
 */
@Suppress("MemberVisibilityCanBePrivate")
object NetworkUtils {
  /**
   * Creates a new [HttpClient][java.net.http.HttpClient].
   *
   * @param connectTimeout the connection timeout, defaults to [DEFAULT_CONNECT_TIMEOUT]
   * @param trustAll whether to trust all TLS/SSL certificates, even if they're bogus
   */
  fun newHttpClient(
    connectTimeout: java.time.Duration = DEFAULT_CONNECT_TIMEOUT,
    trustAll: Boolean = false,
  ): java.net.http.HttpClient = java.net.http.HttpClient.newBuilder()
    .apply {
      connectTimeout(connectTimeout)
      followRedirects(java.net.http.HttpClient.Redirect.NEVER)
      version(java.net.http.HttpClient.Version.HTTP_1_1)
      if (!trustAll) {
        javax.net.ssl.SSLContext.getInstance("TLS").also { sslContext ->
          sslContext.init(
            null,
            trustAllCerts,
            java.security.SecureRandom(),
          )
          sslContext(sslContext)
        }
      }
    }
    .build()

  /**
   * Downloads a file from the network and stores it in a file created in the configured
   * temporary directory of the system.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * Note: The local file will be overwritten if it exists, or created if it
   * doesn't exist.
   *
   * @param url the URL of the file to download
   * @param extension the file extension to use, defaults to ""
   * @param method the HTTP method to use, defaults to [HttpMethod.GET]
   * @param trustAll whether to trust all TLS/SSL certificates, even if they're bogus
   * @param connectTimeout the connection timeout, defaults to [DEFAULT_CONNECT_TIMEOUT]
   * @param headers an optional list of HTTP headers
   * @param cookies an optional list of HTTP cookies
   */
  @Throws(
    IllegalArgumentException::class,
    SecurityException::class,
    InterruptedException::class,
    java.io.IOException::class,
  )
  @Suppress("LongParameterList")
  fun downloadUrl(
    url: String,
    extension: String,
    method: HttpMethod = HttpMethod.GET,
    trustAll: Boolean = false,
    connectTimeout: java.time.Duration = DEFAULT_CONNECT_TIMEOUT,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): java.io.File = downloadUrl(
    url = java.net.URI.create(url),
    extension = extension,
    method = method,
    trustAll = trustAll,
    connectTimeout = connectTimeout,
    headers = headers,
    cookies = cookies,
  )

  /**
   * Downloads a file from the network and stores it in a file created in the configured
   * temporary directory of the system.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * Note: The local file will be overwritten if it exists, or created if it
   * doesn't exist.
   *
   * @param url the URL of the file to download
   * @param extension the file extension to use, defaults to ""
   * @param method the HTTP method to use, defaults to [HttpMethod.GET]
   * @param trustAll whether to trust all TLS/SSL certificates, even if they're bogus
   * @param connectTimeout the connection timeout, defaults to [DEFAULT_CONNECT_TIMEOUT]
   * @param headers an optional list of HTTP headers
   * @param cookies an optional list of HTTP cookies
   */
  @Throws(
    IllegalArgumentException::class,
    SecurityException::class,
    InterruptedException::class,
    java.io.IOException::class,
  )
  @Suppress("LongParameterList")
  fun downloadUrl(
    url: java.net.URI,
    extension: String,
    method: HttpMethod = HttpMethod.GET,
    trustAll: Boolean = false,
    connectTimeout: java.time.Duration = DEFAULT_CONNECT_TIMEOUT,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): java.io.File {
    val file = FileSystem.createTempFile(extension)

    downloadUrl(
      url = url,
      file = file,
      method = method,
      trustAll = trustAll,
      connectTimeout = connectTimeout,
      headers = headers,
      cookies = cookies,
    )

    return file
  }

  /**
   * Downloads a file from the network and stores it in a file.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * Note: The local file will be overwritten if it exists, or created if it
   * doesn't exist.
   *
   * @param url the URL of the file to download
   * @param file the file to store the contents into.
   * @param method the HTTP method to use, defaults to [HttpMethod.GET]
   * @param trustAll whether to trust all TLS/SSL certificates, even if they're bogus
   * @param connectTimeout the connection timeout, defaults to [DEFAULT_CONNECT_TIMEOUT]
   * @param headers an optional list of HTTP headers
   * @param cookies an optional list of HTTP cookies
   */
  @Throws(
    IllegalArgumentException::class,
    SecurityException::class,
    InterruptedException::class,
    java.io.IOException::class,
  )
  @Suppress("LongParameterList")
  fun downloadUrl(
    url: String,
    file: java.io.File,
    method: HttpMethod = HttpMethod.GET,
    trustAll: Boolean = false,
    connectTimeout: java.time.Duration = DEFAULT_CONNECT_TIMEOUT,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ) {
    downloadUrl(
      url = java.net.URI.create(url),
      file = file,
      method = method,
      trustAll = trustAll,
      connectTimeout = connectTimeout,
      headers = headers,
      cookies = cookies,
    )
  }

  /**
   * Downloads a file from the network and stores it in a file.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * Note: The local file will be overwritten if it exists, or created if it
   * doesn't exist.
   *
   * @param url the URL of the file to download
   * @param file the file to store the contents into.
   * @param method the HTTP method to use, defaults to [HttpMethod.GET]
   * @param trustAll whether to trust all TLS/SSL certificates, even if they're bogus
   * @param connectTimeout the connection timeout, defaults to [DEFAULT_CONNECT_TIMEOUT]
   * @param headers an optional list of HTTP headers
   * @param cookies an optional list of HTTP cookies
   */
  @Throws(
    IllegalArgumentException::class,
    SecurityException::class,
    InterruptedException::class,
    java.io.IOException::class,
  )
  @Suppress("LongParameterList")
  fun downloadUrl(
    url: java.net.URI,
    file: java.io.File,
    method: HttpMethod = HttpMethod.GET,
    trustAll: Boolean = false,
    connectTimeout: java.time.Duration = DEFAULT_CONNECT_TIMEOUT,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ) {
    require(file.isDirectory) {
      "Output file $file cannot be a directory."
    }

    val request = java.net.http.HttpRequest.newBuilder().apply {
      uri(url)
      method(method)

      if (!headers.isNullOrEmpty()) {
        for ((name, value) in headers) {
          setHeader(name, value)
        }
      }

      if (!cookies.isNullOrEmpty()) {
        val cookiesValue = mutableListWith<String>(cookies.size)
        for ((name, value) in cookies) {
          val nameEncoded =
            java.net.URLEncoder.encode(name, java.nio.charset.StandardCharsets.UTF_8)
          val valueEncoded =
            java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8)

          cookiesValue.add("$nameEncoded=$valueEncoded")
        }
        setHeader("Cookie", cookiesValue.joinToString("; "))
      }
    }
      .build()

    val httpClient = newHttpClient(
      connectTimeout = connectTimeout,
      trustAll = trustAll,
    )

    httpClient.send(
      request,
      java.net.http.HttpResponse.BodyHandlers.ofFile(file.toPath()),
    )
  }

  // A special TrustManager that trusts all issuers and certificates.
  private val trustAllCerts = arrayOf<javax.net.ssl.TrustManager>(
    object : javax.net.ssl.X509TrustManager {
      override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?> =
        arrayOfNulls(0)

      override fun checkClientTrusted(
        certs: Array<java.security.cert.X509Certificate?>?,
        authType: String?,
      ) {
        // NO-OP.
      }

      override fun checkServerTrusted(
        certs: Array<java.security.cert.X509Certificate?>?,
        authType: String?,
      ) {
        // NO-OP.
      }
    },
  )
}
