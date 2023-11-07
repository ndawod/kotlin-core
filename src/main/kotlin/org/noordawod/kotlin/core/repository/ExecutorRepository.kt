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

@file:Suppress("unused")

package org.noordawod.kotlin.core.repository

/**
 * A task that has been scheduled to execute.
 */
typealias RunningTask = java.util.concurrent.ScheduledFuture<*>

/**
 * Defines a repository to handle concurrent operations using a multi-threading model.
 *
 * The repository can handle immediate, scheduled and repeating tasks.
 */
interface ExecutorRepository {
  /**
   * Evaluates to true if this [ExecutorRepository] is started, false otherwise.
   */
  val isStarted: Boolean

  /**
   * Starts the specified number of threads to handle incoming tasks.
   *
   * Tasks can be scheduled to run using any of the [execute] methods.
   *
   * @param threads number of threads to start
   */
  fun start(threads: Int)

  /**
   * Stops the threads and stops accepting any new tasks.
   *
   * The currently running tasks will continue to run, but no new tasks will be accepted.
   */
  fun stop()

  /**
   * Schedules to execute the specified [task].
   *
   * @param task a callback to run the task
   */
  fun execute(task: Runnable)

  /**
   * Schedules to execute the specified [task] which returns a future result of type [T].
   *
   * @param task a callback to run the task
   * @param onResult a callback to run with the result of running [task]
   */
  fun <T> execute(
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  )

  /**
   * Schedules to execute the specified [task] at a specific time in the future.
   *
   * @param time the future time to run the task
   * @param task a callback to run the task
   */
  fun execute(
    time: java.util.Date,
    task: Runnable,
  ): RunningTask

  /**
   * Schedules to execute the specified [task] at a specific time in the future; the task
   * returns a future result of type [T].
   *
   * @param time the future time to run the task
   * @param task a callback to run the task
   * @param onResult a callback to run with the result of running [task]
   */
  fun <T> execute(
    time: java.util.Date,
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  ): RunningTask

  /**
   * Schedules to execute the specified [task] at a fixed rate, with the first execution
   * starting immediately.
   *
   * @param rate the duration between successive executions
   * @param task a callback to run the task
   */
  fun execute(
    rate: java.time.Duration,
    task: Runnable,
  ): RunningTask

  /**
   * Schedules to execute the specified [task] at a fixed rate, with the first execution
   * starting at the specified time.
   *
   * @param time the future time to run the first task
   * @param rate the duration between successive executions
   * @param task a callback to run the task
   */
  fun execute(
    time: java.util.Date,
    rate: java.time.Duration,
    task: Runnable,
  ): RunningTask

  /**
   * Schedules to execute the specified [task] at a fixed rate, with the first execution
   * starting immediately.
   *
   * @param rate the duration between successive executions
   * @param task a callback to run the task
   * @param onResult a callback to run with the result of running [task]
   */
  fun <T> execute(
    rate: java.time.Duration,
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  ): RunningTask

  /**
   * Schedules to execute the specified [task] at a fixed rate, with the first execution
   * starting at the specified time.
   *
   * @param time the future time to run the first task
   * @param rate the duration between successive executions
   * @param task a callback to run the task
   * @param onResult a callback to run with the result of running [task]
   */
  fun <T> execute(
    time: java.util.Date,
    rate: java.time.Duration,
    task: java.util.concurrent.Callable<T>,
    onResult: ((T) -> Unit)?,
  ): RunningTask
}
