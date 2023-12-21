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

package org.noordawod.kotlin.core.repository.impl

import org.noordawod.kotlin.core.repository.ExecutorRepository
import org.noordawod.kotlin.core.repository.RunningTask

internal class ExecutorRepositoryImpl : ExecutorRepository {
  private var executor: java.util.concurrent.ScheduledExecutorService? = null

  override val isStarted: Boolean
    get() = null != executor

  @Synchronized
  override fun start(threads: Int) {
    if (null == executor) {
      executor = java.util.concurrent.ScheduledThreadPoolExecutor(threads)
    }
  }

  @Synchronized
  override fun stop() {
    val executorLocked = executor
    executor = null
    executorLocked?.shutdown()
  }

  @Synchronized
  override fun execute(task: Runnable) {
    ensureStarted().execute(task)
  }

  override fun <T> execute(
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  ) {
    execute {
      val result = task.call()
      onResult?.invoke(result)
    }
  }

  @Synchronized
  override fun execute(
    time: java.util.Date,
    task: Runnable,
  ): RunningTask = ensureStarted().schedule(
    task,
    time.time - java.util.Date().time.coerceAtLeast(0L),
    java.util.concurrent.TimeUnit.MILLISECONDS,
  )

  override fun <T> execute(
    time: java.util.Date,
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  ): RunningTask = execute(time) {
    val result = task.call()
    onResult?.invoke(result)
  }

  override fun execute(
    rate: java.time.Duration,
    task: Runnable,
  ): RunningTask = execute(
    time = java.util.Date(),
    rate = rate,
    task = task,
  )

  @Synchronized
  override fun execute(
    time: java.util.Date,
    rate: java.time.Duration,
    task: Runnable,
  ): RunningTask = ensureStarted().scheduleAtFixedRate(
    task,
    (time.time - java.util.Date().time).coerceAtLeast(0L),
    rate.toMillis(),
    java.util.concurrent.TimeUnit.MILLISECONDS,
  )

  override fun <T> execute(
    rate: java.time.Duration,
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  ): RunningTask = execute(
    time = java.util.Date(),
    rate = rate,
    task = task,
    onResult = onResult,
  )

  override fun <T> execute(
    time: java.util.Date,
    rate: java.time.Duration,
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  ): RunningTask = execute(
    time = time,
    rate = rate,
  ) {
    val result = task.call()
    onResult?.invoke(result)
  }

  @Suppress("UseCheckOrError")
  private fun ensureStarted(): java.util.concurrent.ScheduledExecutorService =
    executor ?: throw IllegalStateException("The ExecutorRepository is stopped.")
}
