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

@file:Suppress("unused")

package org.noordawod.kotlin.core.config

/**
 * Defines configuration of an Undertow server when starting it up.
 *
 * @see <a href="https://tinyurl.com/t8yyhxm">Assembling a Server Manually</a>
 * @see <a href="https://tinyurl.com/wdrwhe7">Architecture Overview</a>
 */
open class ServerConfiguration constructor(
  val host: String,
  val port: Int,
  val threads: ServerThreadsConfiguration,
  val buffer: ServerBufferConfiguration
)

/**
 * Defines the threading configuration of an Undertow server.
 *
 * @see <a href="https://tinyurl.com/wdrwhe7">Architecture Overview</a>
 * @see <a href="http://xnio.jboss.org/">XNIO</a>
 */
data class ServerThreadsConfiguration constructor(
  val io: Int,
  val worker: Int
)

/**
 * Defines the configuration a buffer pool tied to listeners.
 *
 * @see <a href="https://tinyurl.com/v2c7p3u">Buffer Pool</a>
 * @see <a href="https://tinyurl.com/ww4xyct">Listeners</a>
 */
data class ServerBufferConfiguration constructor(
  val size: Int,
  val perRegion: Int
)
