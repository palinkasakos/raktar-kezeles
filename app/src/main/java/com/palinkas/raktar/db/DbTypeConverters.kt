package com.palinkas.raktar.db

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.util.*


object DbTypeConverters {

    /**
     * Ennyi tizedesre kerekítünk minden értéket
     */
    private const val SCALE = 8


    @JvmStatic
    @TypeConverter
    fun fromLongMoney(value: Long?): BigDecimal? {
        return if (value == null) null
        else BigDecimal(value).scaleByPowerOfTen(-1 * SCALE)
    }

    @JvmStatic
    @TypeConverter
    fun bigDecimalToMoney(value: BigDecimal?): Long? {
        return value?.scaleByPowerOfTen(SCALE)?.toLong()
    }


    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @JvmStatic
    @TypeConverter
    fun integerToBoolean(value: Int?): Boolean {
        return value == 1
    }

    @JvmStatic
    @TypeConverter
    fun booleanToInteger(value: Boolean?): Int {
        return if (value == true) 1 else 0
    }


}