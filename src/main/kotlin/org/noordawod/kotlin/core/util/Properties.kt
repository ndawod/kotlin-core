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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package org.noordawod.kotlin.core.util

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Handles loading k:v properties into a [Properties] class using *.properties files.
 */
class Properties private constructor(private val props: java.util.Properties) {
  private var destroyed = false

  /**
   * Destroys this instance and clears its memory.
   */
  fun destroy() {
    checkNotDestroyed()
    destroyed = true
    props.clear()
  }

  /**
   * Merges more properties loaded from [files] into this instance.
   */
  fun merge(paths: Iterable<String>? = null, files: Iterable<File>? = null) {
    checkNotDestroyed()

    fun mergeFrom(fis: FileInputStream) {
      InputStreamReader(fis, StandardCharsets.UTF_8).use {
        val fileProps = java.util.Properties()
        fileProps.load(it)
        fileProps.forEach { k, v -> props[k.toString()] = v }
        fileProps.clear()
      }
    }

    paths?.forEach {
      mergeFrom(FileInputStream(it))
    }

    files?.forEach {
      mergeFrom(FileInputStream(it))
    }
  }

  /**
   * Returns the value for the specified [key], and add support for using the index operator to
   * do so as well.
   */
  @Throws(PropertiesDestroyedException::class)
  operator fun <T> get(key: String): T? {
    checkNotDestroyed()

    @Suppress("UNCHECKED_CAST")
    return props[key] as? T
  }

  /**
   * Returns the value for the specified [key], and add support for using the index operator to
   * do so as well.
   */
  @Throws(PropertiesDestroyedException::class)
  private operator fun <T> set(key: String, value: T) {
    checkNotDestroyed()
    props[key] = value
  }

  private fun checkNotDestroyed() {
    if (destroyed) {
      throw PropertiesDestroyedException("This Properties instance is destroyed.")
    }
  }

  companion object {
    /**
     * Loads properties from the specified [file].
     */
    @Throws(IOException::class)
    fun from(file: String): Properties {
      val props = Properties(java.util.Properties())
      props.merge(paths = listOf(file))
      return props
    }

    /**
     * Loads properties from the specified [file].
     */
    @Throws(IOException::class)
    fun from(file: File): Properties {
      val props = Properties(java.util.Properties())
      props.merge(files = listOf(file))
      return props
    }

    /**
     * Loads properties from the list of [files] by merging all together. Properties belonging to
     * later files override those in earlier positions.
     */
    @Throws(IOException::class)
    fun from(paths: Iterable<String>?, files: Iterable<File>?): Properties {
      val props = Properties(java.util.Properties())
      props.merge(paths, files)
      return props
    }
  }
}

/**
 * Denotes that a [Properties] instance has been destroyed and no longer can be used.
 */
class PropertiesDestroyedException(
  message: String,
  cause: Throwable? = null
) : RuntimeException(message, cause)
