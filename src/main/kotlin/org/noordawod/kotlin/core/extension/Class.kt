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

package org.noordawod.kotlin.core.extension

/**
 * Detects the type of this [Class] as follows:
 *
 * - Returns a Class&lt;[Map]&gt; if the type is a descendant of [Map].
 * - Returns a Class&lt;[Collection]&gt; if the type is a descendant of [Collection].
 * - Otherwise, returns the same [Class].
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Class<T>.simplifyType(): Class<T> = when {
  Map::class.java.isAssignableFrom(this) -> Map::class.java
  Array::class.java.isAssignableFrom(this) -> Array::class.java
  List::class.java.isAssignableFrom(this) -> List::class.java
  Collection::class.java.isAssignableFrom(this) -> Collection::class.java
  Iterable::class.java.isAssignableFrom(this) -> Iterable::class.java
  else -> this
} as Class<T>
