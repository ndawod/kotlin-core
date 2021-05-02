/*
 * The MIT License
 *
 * Copyright 2021 Noor Dawod. All rights reserved.
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

import java.util.Comparator

/**
 * A [Comparator] for [ByteArray] objects.
 */
val ByteArrayComparator: Comparator<ByteArray> = Comparator<ByteArray> { o1, o2 ->
  when {
    null != o1 && null != o2 && o1.size != o2.size -> o1.size - o2.size
    null != o1 && null != o2 -> {
      var result = 0
      var idx = -1
      while (o1.size > ++idx) {
        if (o1[idx] != o2[idx]) {
          result = o1[idx] - o2[idx]
          break
        }
      }
      result
    }
    null == o1 && null == o2 -> 0
    null == o1 -> -1
    else -> 1
  }
}
