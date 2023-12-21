/*
 * The MIT License
 *
 * Copyright 2022 Noor Dawod. All rights reserved.
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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core.util

import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

private const val DEFAULT_KEEP_ALIVE = 30L

/**
 * Alternative, simpler service to execute code on separate threads.
 */
class ExecutorService private constructor(
  keepAlive: Duration,
) {
  private val immediately: java.util.concurrent.ExecutorService = ThreadPoolExecutor(
    // corePoolSize
    0,
    // maximumPoolSize
    Int.MAX_VALUE,
    // keepAliveTime
    keepAlive.seconds,
    // keepAliveUnit
    TimeUnit.SECONDS,
    // workQueue
    SynchronousQueue(),
  )
  private val delayed = Executors.newSingleThreadScheduledExecutor()

  /**
   * Whether this instance is destroyed or not.
   */
  var isDestroyed: Boolean = false
    private set

  /**
   * Executes a task on a thread immediately.
   */
  fun execute(task: () -> Unit) {
    ensureNotDestroyed()
    execute(Runnable(task))
  }

  /**
   * Executes a task on a thread after the specified [delay] has passed.
   */
  fun execute(
    task: () -> Unit,
    delay: Duration,
  ) {
    ensureNotDestroyed()
    execute(Runnable(task), delay)
  }

  /**
   * Executes a task on a thread immediately.
   */
  fun execute(task: Runnable) {
    ensureNotDestroyed()
    immediately.execute(task)
  }

  /**
   * Executes a task on a thread after the specified [delay] has passed.
   */
  fun execute(
    task: Runnable,
    delay: Duration,
  ) {
    ensureNotDestroyed()
    delayed.schedule(task, delay.toMillis(), TimeUnit.MILLISECONDS)
  }

  /**
   * Destroys this instance and try to halt execution of current tasks.
   */
  fun destroy() {
    immediately.shutdownNow()
    delayed.shutdownNow()
  }

  private fun ensureNotDestroyed() {
    check(!isDestroyed) {
      throw IllegalStateException("This executor service instance is destroyed.")
    }
  }

  companion object {
    /**
     * Returns a new [ExecutorService] instance with a default keep-alive time of 30 seconds.
     */
    fun newInstance(): ExecutorService = ExecutorService(Duration.ofSeconds(DEFAULT_KEEP_ALIVE))

    /**
     * Returns a new [ExecutorService] instance with the specified [keepAlive] duration.
     */
    fun newInstance(keepAlive: Duration): ExecutorService = ExecutorService(keepAlive)
  }
}
