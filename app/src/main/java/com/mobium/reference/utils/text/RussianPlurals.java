/*
 * Copyright (c) 2013 Extradea LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobium.reference.utils.text;

/**
 *
 * Date: 07.10.12
 * Time: 18:51
 */
public class RussianPlurals {
    public static final String[] DAYS_PLURALS = {"день", "дня", "дней"};
    public static final String[] HOURS_PLURALS = {"час", "часа", "часов"};
    public static final String[] MINUTES_PLURALS = {"минуту", "минуты", "минут"};
    public static final String[] GOODS_PLURALS = {"товар", "товара", "товаров"};
    public static final String[] ANSWERS_PLURALS = {"ответ", "ответа", "ответов"};
    public static final String[] QUESTIONS_PLURALS = {"вопрос", "вопроса", "вопросов"};

    public static int getPluralIndex(int n) {
        return n % 10 == 1 && n % 100 != 11 ? 0 : n % 10 >= 2 && n % 10 <= 4 && (n % 100 < 10 || n % 100 >= 20) ? 1 : 2;
    }

    public static String formatPlurals(String[] source, int count) {
        return new StringBuilder()
                .append(count).append(' ')
                .append(source[getPluralIndex(count)])
                .toString();
    }

    public static String formatPluralsTitle(String[] source, int count) {
        return source[getPluralIndex(count)];
    }

    public static String formatAnswer(final int count) {
        return new StringBuilder()
                .append(count).append(' ')
                .append(ANSWERS_PLURALS[getPluralIndex(count)])
                .toString();
    }

    public static String formatQuestion(final int count) {
        return new StringBuilder()
                .append(count).append(' ')
                .append(QUESTIONS_PLURALS[getPluralIndex(count)])
                .toString();
    }

    public static String formatGoodsCount(int count) {
        return new StringBuilder()
                .append(count).append(' ')
                .append(GOODS_PLURALS[getPluralIndex(count)])
                .toString();
    }
}
