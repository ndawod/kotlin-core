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

import com.ibm.icu.text.PluralRules
import org.noordawod.kotlin.core.extension.trimOrNull
import org.noordawod.kotlin.core.repository.LocalizationRepository
import org.noordawod.kotlin.core.util.Localization

internal class LocalizationRepositoryImpl(
  override val l10n: Localization,
) : LocalizationRepository {
  override val locale: java.util.Locale = l10n.locale

  override fun translate(key: String): String = translate(key, listOf())

  override fun translate(key: String, args: Iterable<Any>): String {
    val text = l10n.translation.get<Any>(key)?.toString().trimOrNull()

    return if (null == text) {
      key
    } else {
      @Suppress("SpreadOperator")
      java.lang.String.format(
        l10n.locale,
        text,
        *args.map { it.toString() }.toTypedArray(),
      )
    }
  }

  override fun pluralize(key: String, count: Int): String {
    val pluralRules = PluralRules.forLocale(l10n.locale)
    val rule = pluralRules.select(count.toDouble()).lowercase(java.util.Locale.ENGLISH)
    return translate(keyRuleValue(key, rule))
  }

  override fun pluralize(key: String, count: Int, args: Iterable<Any>): String {
    val pluralRules = PluralRules.forLocale(l10n.locale)
    val rule = pluralRules.select(count.toDouble()).lowercase(java.util.Locale.ENGLISH)
    return translate(keyRuleValue(key, rule), args)
  }

  override fun quantify(key: String, quantity: Int): String {
    val rule = quantifyRule(quantity)
    return translate(keyRuleValue(key, rule))
  }

  override fun quantify(key: String, quantity: Int, args: Iterable<Any>): String {
    val rule = quantifyRule(quantity)
    return translate(keyRuleValue(key, rule), args)
  }

  private fun quantifyRule(quantity: Int): String = when (quantity) {
    0 -> PluralRules.KEYWORD_ZERO
    1 -> PluralRules.KEYWORD_ONE
    2 -> PluralRules.KEYWORD_TWO
    else -> PluralRules.KEYWORD_OTHER
  }

  private fun keyRuleValue(key: String, rule: String): String = "$key.$rule"
}
