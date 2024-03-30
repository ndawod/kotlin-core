/*
 * The MIT License
 *
 * Copyright 2024 Noor Dawod. All rights reserved.
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

package org.noordawod.kotlin.core.extension

import io.github.cdimascio.dotenv.Dotenv
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

/**
 * Loads secret values for certain properties from a .env file.
 *
 * The function recursively detect all properties of this [T] class, and finds those
 * properties with String values starting with the provided [prefix]. When found,
 * the function loads the secret value from [dotenv] and updates instance with the
 * new value.
 *
 * Note: Only public or internal properties are checked.
 *
 * @param dotenv the .env file after being loaded into memory
 * @param prefix the prefix market for values, defaults to "ENV_"
 */
fun <T : Any> T.declassify(dotenv: Dotenv, prefix: String = "ENV_"): T {
  for (property in this::class.memberProperties) {
    if (property.visibility !in arrayOf(KVisibility.PUBLIC, KVisibility.INTERNAL)) {
      continue
    }

    val name = property.name
    val value: String

    try {
      val propertyValue = property.getter.call(this) ?: continue

      // We only work with String values.
      if (propertyValue !is String) {
        propertyValue.declassify(dotenv, prefix)
        continue
      }

      if (!propertyValue.startsWith(prefix)) {
        continue
      }

      value = propertyValue
    } catch (ignored: Throwable) {
      // Possibly inaccessible properties, we can ignore.
      continue
    }

    val secretValue = dotenv.get(value)
      ?: throw IllegalStateException("Unable to find secret value for property: $value")

    // We must use Java reflection to be able to modify "val" properties.
    val propertyJavaProps = this::class.java.getDeclaredField(name)
    propertyJavaProps.isAccessible = true
    propertyJavaProps.set(this, secretValue)
  }

  return this
}
