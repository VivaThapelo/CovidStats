package com.thapelo.covidstats.databases

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun toString(value: List<Any>?): String? {
        val value = value as List<String>
        //  val lists : MutableList<String> = mutableListOf()
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromString(value: String?): List<Any> {
        val res = value as List<Any>
        return res
    }


}