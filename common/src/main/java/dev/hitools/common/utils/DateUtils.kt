/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.common.utils

import android.content.Context
import android.util.LruCache
import dev.hitools.common.R
import dev.hitools.common.extensions.date.toEpochMilli
import dev.hitools.common.extensions.date.toLocalDateTime
import dev.hitools.common.extensions.toLocalDate
import dev.hitools.common.extensions.toLocalDateTime
import java.text.ParseException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 时间工具类
 */
object DateUtils {
    /**
     * 1秒
     */
    const val SECOND_1 = 1000L

    /**
     * 30秒
     */
    const val SECOND_30 = 30L * SECOND_1

    /**
     * 1 分钟
     */
    const val MINUTE_1 = 60L * SECOND_1

    /**
     * 1 小时
     */
    const val HOUR_1 = 60L * MINUTE_1

    /**
     * 1 天
     */
    const val DAY_1 = 24L * HOUR_1

    /**
     * 7 天
     */
    const val DAY_7 = 7L * DAY_1

    /**
     * 一月
     */
    const val ONE_MONTH = 30L * DAY_1

    /**
     * 一年
     */
    const val ONE_YEAR = 365L * DAY_1

    /**
     * 英文简写如：2016
     */
    const val FORMAT_YEAR = "yyyy"

    /**
     * 英文简写如：12:01
     */
    const val FORMAT_HM = "HH:mm"

    /**
     * 英文简写（默认）如：2016-12-01
     */
    const val FORMAT_YMD = "yyyy-MM-dd"

    /**
     * 英文全称  如：2016-12-01 23:15
     */
    const val FORMAT_YMDHM = "yyyy-MM-dd HH:mm"

    /**
     * 英文全称  如：2016-12-01 23:15:06
     */
    const val FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss"

    /**
     * Formatter的缓存
     */
    private val formatterMap = LruCache<String, DateTimeFormatter>(10)

    /**
     * format 时间字符串
     *
     * @param model    当前时间模型 例如: MM-dd
     */
    @JvmOverloads
    fun now(model: String = FORMAT_YMDHMS): String {
        return format(LocalDateTime.now(), model)
    }

    /**
     * format 时间字符串
     *
     * @param time        当前时间的字符串
     * @param nowModel    当前时间模型 例如: MM-dd
     * @param targetModel 目标时间模型 例如 yyyy-MM-dd
     */
    @JvmOverloads
    fun format(time: String, nowModel: String, targetModel: String = FORMAT_YMD): String {
        try {
            val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(nowModel))
            return format(localDateTime, targetModel)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time
    }

    /**
     * 把字符串转成long
     *
     * @param time     输入的时间
     * @param nowModel 时间模型 例如 yyyy-MM-dd
     * @param locale   区域
     */
    @JvmOverloads
    fun formatToLong(time: String, nowModel: String = FORMAT_YMDHMS): Long {
        try {
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(nowModel)).toEpochMilli()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0L
    }


    /**
     * format时间
     * 默认区域是中国
     *
     * @param time     时间
     * @param targetModel 时间模型 例如 yyyy-MM-dd
     * @return format后的字符串
     */
    @JvmOverloads
    fun format(time: Long, targetModel: String = FORMAT_YMDHMS): String {
        return format(time.toLocalDateTime(), targetModel)
    }

    /**
     * format时间
     *
     * @param date     时间
     * @param targetModel 时间模型 例如 yyyy-MM-dd
     * @return format后的字符串
     */
    @JvmOverloads
    fun format(date: Date, targetModel: String = FORMAT_YMDHMS): String {
        return format(date.toLocalDateTime(), targetModel)
    }

    /**
     * format时间
     *
     * @param dateTime     时间
     * @param targetModel 时间模型 例如 yyyy-MM-dd
     * @return format后的字符串
     */
    @JvmOverloads
    fun format(dateTime: LocalDateTime, targetModel: String = FORMAT_YMDHMS): String {
        return dateTime.format(DateTimeFormatter.ofPattern(targetModel))
    }


    /**
     * 获取一个long型的时间
     */
    fun getTime(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.clear()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.timeInMillis
    }


    /**
     * 移除时分秒
     */
    fun removeHourMinSec(time: Long): Long {
        val c = Calendar.getInstance()
        c.timeInMillis = time
        //设置当前时刻的时钟为0
        c.set(Calendar.HOUR_OF_DAY, 0)
        //设置当前时刻的分钟为0
        c.set(Calendar.MINUTE, 0)
        //设置当前时刻的秒钟为0
        c.set(Calendar.SECOND, 0)
        //设置当前的毫秒钟为0
        c.set(Calendar.MILLISECOND, 0)
        return c.timeInMillis
    }

    /**
     * 判断是不是同一天
     */
    fun isSameDay(time1: Long, time2: Long = System.currentTimeMillis()): Boolean {
        return removeHourMinSec(time1) == removeHourMinSec(time2)
    }


    /**
     * 将时间戳转换成描述性时间（昨天、今天、明天）
     *
     * @param timestamp 时间戳
     * @return 描述性日期
     */
    fun formatFriendly(timestamp: Long): String {
        return when (LocalDate.now().toEpochDay() - timestamp.toLocalDate().toEpochDay()) {
            0L -> "今天"
            in 1L..7L -> "一周内"
            else -> format(timestamp)
        }
    }
}

