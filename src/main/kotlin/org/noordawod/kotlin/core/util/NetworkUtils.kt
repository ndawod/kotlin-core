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

import org.noordawod.kotlin.core.ASCII_LOCALE
import org.noordawod.kotlin.core.DEFAULT_CONNECT_TIMEOUT
import org.noordawod.kotlin.core.extension.mutableListWith

/**
 * An alias of an HTTP response that points to a [file path][java.nio.file.Path].
 */
typealias NetworkDownloadResponse = java.net.http.HttpResponse<java.nio.file.Path>

/**
 * An alias of an HTTP response that contains to [String].
 */
typealias NetworkUploadResponse = java.net.http.HttpResponse<String>

/**
 * An alias for a function that configures an HTTP client builder.
 */
typealias HttpBuilderConfigurator = java.net.http.HttpClient.Builder.() -> Unit

/**
 * Utility functions for networking.
 */
@Suppress("MemberVisibilityCanBePrivate")
object NetworkUtils {
  private val newSecureRandom: java.security.SecureRandom
    get() = java.security.SecureRandom()

  /**
   * A special [TrustManager][javax.net.ssl.TrustManager] that trusts all
   * issuers and certificates.
   *
   * **Only use in non-production environments!**
   *
   * If used inadvertently in a production environment, you may be exposed to all
   * sorts of security-related pitfalls, such as:
   *
   * - Complete vulnerability to man-in-the-middle (MitM) attacks
   * - Total loss of privacy and encryption integrity
   * - Play store / App store rejections
   * - Accidental leaks into production
   */
  val noopTrustManager: Array<javax.net.ssl.TrustManager> = arrayOf(
    object : javax.net.ssl.X509TrustManager {
      private val nulls = arrayOfNulls<java.security.cert.X509Certificate?>(0)

      override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?> = nulls

      override fun checkClientTrusted(
        certs: Array<java.security.cert.X509Certificate?>?,
        authType: String?,
      ) {
        // Raising no concerns means that we trust the chain of certificates.
      }

      override fun checkServerTrusted(
        certs: Array<java.security.cert.X509Certificate?>?,
        authType: String?,
      ) {
        // Raising no concerns means that we trust the chain of certificates.
      }
    },
  )

  /**
   * Creates a new [HttpClient][java.net.http.HttpClient].
   *
   * @param version the HTTP version to use, defaults to HTTP/1.1
   * @param connectTimeout the connection timeout, defaults to [DEFAULT_CONNECT_TIMEOUT]
   * @param trustAll whether to trust all TLS/SSL certificates, even if they're bogus
   * @param configure an optional configurator block for HTTP client builder
   */
  fun newHttpClient(
    version: java.net.http.HttpClient.Version = java.net.http.HttpClient.Version.HTTP_1_1,
    connectTimeout: java.time.Duration = DEFAULT_CONNECT_TIMEOUT,
    trustAll: Boolean = false,
    configure: HttpBuilderConfigurator? = null,
  ): java.net.http.HttpClient = newHttpClient(
    version = version,
    connectTimeout = connectTimeout,
    trustManager = if (trustAll) noopTrustManager else null,
    configure = configure,
  )

  /**
   * Creates a new [HttpClient][java.net.http.HttpClient].
   *
   * @param version the HTTP version to use, defaults to HTTP/1.1
   * @param connectTimeout the connection timeout, defaults to [DEFAULT_CONNECT_TIMEOUT]
   * @param trustManager the SSL/TLS trust manager to use
   * @param configure an optional configurator block for HTTP client builder
   */
  fun newHttpClient(
    version: java.net.http.HttpClient.Version = java.net.http.HttpClient.Version.HTTP_1_1,
    connectTimeout: java.time.Duration = DEFAULT_CONNECT_TIMEOUT,
    trustManager: Array<javax.net.ssl.TrustManager>? = null,
    configure: HttpBuilderConfigurator? = null,
  ): java.net.http.HttpClient = java.net.http.HttpClient.newBuilder()
    .apply {
      connectTimeout(connectTimeout)
      followRedirects(java.net.http.HttpClient.Redirect.NEVER)
      version(version)
      if (!trustManager.isNullOrEmpty()) {
        val sslContext = javax.net.ssl.SSLContext.getInstance("TLS")
        sslContext.init(null, trustManager, newSecureRandom)
        sslContext(sslContext)
      }
      configure?.invoke(this)
    }
    .build()

  /**
   * Downloads a file from the network and stores it in a file created in the configured
   * temporary directory of the system.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * @param url the URL of the file to download
   * @param extension the file extension to use
   * @param httpClient the configured HTTP client to use
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
  fun download(
    url: String,
    extension: String,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkDownloadResponse = download(
    url = java.net.URI.create(url),
    extension = extension,
    httpClient = httpClient,
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
   * @param url the URL of the file to download
   * @param extension the file extension to use
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
  fun download(
    url: java.net.URI,
    extension: String,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkDownloadResponse = download(
    url = url,
    file = FileSystem.createTempFile(extension),
    httpClient = httpClient,
    headers = headers,
    cookies = cookies,
  )

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
   * @param file the local file to store the contents into
   * @param httpClient the configured HTTP client to use
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
  fun download(
    url: String,
    file: java.io.File,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkDownloadResponse = download(
    url = java.net.URI.create(url),
    file = file,
    httpClient = httpClient,
    headers = headers,
    cookies = cookies,
  )

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
   * @param file the local file to store the contents into
   * @param httpClient the configured HTTP client to use
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
  fun download(
    url: java.net.URL,
    file: java.io.File,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkDownloadResponse = download(
    url = java.net.URI.create("$url"),
    file = file,
    httpClient = httpClient,
    headers = headers,
    cookies = cookies,
  )

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
   * @param file the local file to store the contents into
   * @param httpClient the configured HTTP client to use
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
  fun download(
    url: java.net.URI,
    file: java.io.File,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkDownloadResponse {
    require(!file.isDirectory) {
      "Output file $file cannot be a directory."
    }

    val request = java.net.http.HttpRequest.newBuilder()
      .uri(url)
      .GET()
      .augmentHeaders(
        headers = headers,
        cookies = cookies,
      )
      .build()

    return httpClient.send(
      request,
      java.net.http.HttpResponse.BodyHandlers.ofFile(file.toPath()),
    )
  }

  /**
   * Uploads a local file to a remote network endpoint.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * @param method the HTTP method to use for uploading
   * @param url the URL of the endpoint to upload to
   * @param file the file to upload
   * @param httpClient the configured HTTP client to use
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
  fun upload(
    method: String,
    url: String,
    file: java.io.File,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkUploadResponse = upload(
    method = method,
    url = java.net.URI.create(url),
    file = file,
    httpClient = httpClient,
    headers = headers,
    cookies = cookies,
  )

  /**
   * Uploads a local file to a remote network endpoint.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * @param method the HTTP method to use for uploading
   * @param url the URL of the endpoint to upload to
   * @param file the file to upload
   * @param httpClient the configured HTTP client to use
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
  fun upload(
    method: String,
    url: java.net.URL,
    file: java.io.File,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkUploadResponse = upload(
    method = method,
    url = java.net.URI.create("$url"),
    file = file,
    httpClient = httpClient,
    headers = headers,
    cookies = cookies,
  )

  /**
   * Uploads a local file to a remote network endpoint.
   *
   * The function will automatically URL-encode the [cookies] names and values,
   * so do not encode them yourself.
   *
   * @param method the HTTP method to use for uploading
   * @param url the URL of the endpoint to upload to
   * @param file the file to upload
   * @param httpClient the configured HTTP client to use
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
  fun upload(
    method: String,
    url: java.net.URI,
    file: java.io.File,
    httpClient: java.net.http.HttpClient,
    headers: Map<String, String>? = null,
    cookies: Map<String, String>? = null,
  ): NetworkUploadResponse {
    require(file.isFile) {
      "File does not exist: $file"
    }
    require(file.canRead()) {
      "File is unreadable: $file"
    }

    val request = java.net.http.HttpRequest.newBuilder()
      .uri(url)
      .method(
        method.uppercase(ASCII_LOCALE),
        java.net.http.HttpRequest.BodyPublishers.ofFile(file.toPath()),
      )
      .augmentHeaders(
        headers = headers,
        cookies = cookies,
      )
      .build()

    return httpClient.send(
      request,
      java.net.http.HttpResponse.BodyHandlers.ofString(),
    )
  }

  private fun java.net.http.HttpRequest.Builder.augmentHeaders(
    headers: Map<String, String>?,
    cookies: Map<String, String>?,
  ): java.net.http.HttpRequest.Builder {
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

    return this
  }
}
