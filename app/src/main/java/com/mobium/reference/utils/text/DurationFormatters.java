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

import java.util.Calendar;
import java.util.Date;

/**
 *
 *
 * Date: 30.11.12
 * Time: 20:34
 */
public class DurationFormatters {

    private static final String[] RU_MONTHS = {
            "января",
            "февраля",
            "марта",
            "апреля",
            "мая",
            "июня",
            "июля",
            "августа",
            "сентября",
            "октября",
            "ноября",
            "декабря"};

    private static final String[] EN_MONTHS = {
            "Jun",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"};

    public static String formatHumanReadableDate(long time, String locale) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        if ("en".equals(locale)) {
            return calendar.get(Calendar.DAY_OF_MONTH) + " " + EN_MONTHS[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        } else {
            return calendar.get(Calendar.DAY_OF_MONTH) + " " + RU_MONTHS[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
        }
    }

    public static final String[] DAYS_AVAILABLE_PLURALS = {"день", "дня", "дней"};
    public static final String[] HOURS_AVAILABLE_PLURALS = {"час", "часа", "часов"};
    public static final String[] MINUTES_AVAILABLE_PLURALS = {"минута", "минуты", "минут"};

    public static final String[] AVAILABLE_MINUTES_PREFIX_PLURALS = {"Осталась", "Осталось", "Осталось"};
    public static final String[] AVAILABLE_HOURS_PREFIX_PLURALS = {"Остался", "Осталось", "Осталось"};
    public static final String[] AVAILABLE_DAYS_PREFIX_PLURALS = {"Остался", "Осталось", "Осталось"};

    public static String formatHumanReadableAvailableTime(int endTime) {
        int delta = (int) (endTime - System.currentTimeMillis() / 1000);//Secs
        if (delta < 60) {
            return "В эту минуту";
        } else if (delta < 60 * 60) {
            int minutes = delta / 60;
            return AVAILABLE_MINUTES_PREFIX_PLURALS[RussianPlurals.getPluralIndex(minutes)] +
                    ' ' + RussianPlurals.formatPlurals(MINUTES_AVAILABLE_PLURALS, minutes);
        } else if (delta < 24 * 60 * 60) {
            int hours = delta / (60 * 60);
            return AVAILABLE_HOURS_PREFIX_PLURALS[RussianPlurals.getPluralIndex(hours)] +
                    ' ' + RussianPlurals.formatPlurals(HOURS_AVAILABLE_PLURALS, hours);
        } else {
            int days = delta / (24 * 60 * 60);

            return AVAILABLE_DAYS_PREFIX_PLURALS[RussianPlurals.getPluralIndex(days)] +
                    ' ' + RussianPlurals.formatPlurals(DAYS_AVAILABLE_PLURALS, days);
        }
    }

    public static String formatHumanReadableTimeAgo(int lastSeen, String locale) {
        if ("en".equals(locale)) {
            int delta = (int) (System.currentTimeMillis() / 1000 - lastSeen);//Secs
            if (delta < 60) {
                return "just now";
            } else if (delta < 60 * 60) {
                int minutes = delta / 60;
                if (minutes == 1) {
                    return "minute ago";
                } else {
                    return minutes + " minutes ago";
                }
            } else if (delta < 24 * 60 * 60) {
                int hours = delta / (60 * 60);
                if (hours == 1) {
                    return "an hour ago";
                } else {
                    return hours + " hour ago";
                }
            } else {
                Date date = new Date(lastSeen * 1000L);
                return date.getDate() + " " + EN_MONTHS[date.getMonth()];
                /* Date date = new Date(lastSeen * 1000);
                if (new Date().getTime() - date.getTime() < HOURS_DELTA) {
                    return "at " + format2Digit(date.getHours()) + ":" + format2Digit(date.getMinutes());
                } else {
                    return format2Digit(date.getDate()) +
                            "." + format2Digit((date.getMonth() + 1)) +
                            "." + format2Digit((date.getYear() % 100));
                }*/
            }
        } else {
            int delta = (int) (System.currentTimeMillis() / 1000 - lastSeen);//Secs
            if (delta < 60) {
                return "Только что";
            } else if (delta < 60 * 60) {
                int minutes = delta / 60;
                return minutes + " " + RussianPlurals.formatPluralsTitle(RussianPlurals.MINUTES_PLURALS, minutes) + " назад";
            } else if (delta < 24 * 60 * 60) {
                int hours = delta / (60 * 60);
                return hours + " " + RussianPlurals.formatPluralsTitle(RussianPlurals.HOURS_PLURALS, hours) + " назад";
            } else {
                Date date = new Date(lastSeen * 1000L);
                return date.getDate() + " " + RU_MONTHS[date.getMonth()];
            }
        }
    }
}
