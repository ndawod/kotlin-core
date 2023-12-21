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

package org.noordawod.kotlin.core.logger

import org.noordawod.kotlin.core.repository.PublicId

/**
 * Reports a warning about a public identifier that is not a proper hash value.
 *
 * @param tag log tag to use
 * @param publicId problematic hash value
 */
fun Logger.hashValueWarning(
  tag: String,
  publicId: PublicId,
) {
  warning(tag, "Unable to calculate hash value: '$publicId'")
}

/**
 * Reports a warning about a data model that cannot be converted to an entity.
 *
 * @param tag log tag to use
 * @param model model that cannot be converted
 */
fun Logger.modelWarning(
  tag: String,
  model: Any,
) {
  warning(tag, "Cannot convert model to entity: $model")
}

/**
 * Reports a warning about an entity that cannot be converted to a data model.
 *
 * @param tag log tag to use
 * @param entity entity that cannot be converted
 */
fun Logger.entityWarning(
  tag: String,
  entity: Any,
) {
  warning(tag, "Cannot convert entity to model: $entity")
}
