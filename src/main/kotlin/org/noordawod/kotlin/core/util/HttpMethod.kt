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

package org.noordawod.kotlin.core.util

import java.net.http.HttpRequest

/**
 * The allowed HTTP methods.
 */
sealed interface HttpMethod {
  /**
   * The `GET` HTTP method.
   */
  data object GET : HttpMethod

  /**
   * The `DELETE` HTTP method.
   */
  data object DELETE : HttpMethod

  /**
   * The `POST` HTTP method.
   *
   * @param publisher the publisher to use for sending the payload
   */
  data class POST(val publisher: HttpRequest.BodyPublisher) : HttpMethod

  /**
   * The `PUT` HTTP method.
   *
   * @param publisher the publisher to use for sending the payload
   */
  data class PUT(val publisher: HttpRequest.BodyPublisher) : HttpMethod

  /**
   * The `PATCH` HTTP method.
   *
   * @param publisher the publisher to use for sending the payload
   */
  data class PATCH(val publisher: HttpRequest.BodyPublisher) : HttpMethod

  /**
   * The `QUERY` HTTP method.
   *
   * @param publisher the publisher to use for sending the payload
   */
  data class QUERY(val publisher: HttpRequest.BodyPublisher) : HttpMethod
}

/**
 * Allows defining [HttpMethod] directly on an established [HttpRequest.Builder] instance.
 *
 * @param method the [HttpMethod] instance to use
 */
fun HttpRequest.Builder.method(method: HttpMethod): HttpRequest.Builder {
  when (method) {
    is HttpMethod.GET -> GET()
    is HttpMethod.DELETE -> DELETE()
    is HttpMethod.POST -> POST(method.publisher)
    is HttpMethod.PUT -> PUT(method.publisher)
    is HttpMethod.PATCH -> method("PATCH", method.publisher)
    is HttpMethod.QUERY -> method("QUERY", method.publisher)
  }

  return this
}
