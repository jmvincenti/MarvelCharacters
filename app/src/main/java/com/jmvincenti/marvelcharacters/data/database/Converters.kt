package com.jmvincenti.marvelcharacters.data.database

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jmvincenti.marvelcharacters.data.model.*


/**
 * TODO: Add a class header comment! 😘
 */
class Converters {

    var gson = Gson()

    @TypeConverter
    fun fromApiUrls(value: List<ApiUrl>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toApiUrls(value: String?): List<ApiUrl>? {
        return if (value != null) {
            val listType = object : TypeToken<List<ApiUrl>>() {}.type
            gson.fromJson(value, listType);
        } else null
    }

    @TypeConverter
    fun fromApiImage(value: ApiImage?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toApiImage(value: String?): ApiImage? {
        return if (value != null) {
            val listType = object : TypeToken<ApiImage>() {}.type
            gson.fromJson(value, listType);
        } else null
    }


    @TypeConverter
    fun fromApiComicList(value: ApiList<Comic>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toApiComicList(value: String?): ApiList<Comic>? {
        return if (value != null) {
            val listType = object : TypeToken<ApiList<Comic>>() {}.type
            gson.fromJson(value, listType);
        } else null
    }


    @TypeConverter
    fun fromApiSeriesList(value: ApiList<Series>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toApiSeriesList(value: String?): ApiList<Series>? {
        return if (value != null) {
            val listType = object : TypeToken<ApiList<Series>>() {}.type
            gson.fromJson(value, listType);
        } else null
    }


    @TypeConverter
    fun fromApiStoriesList(value: ApiList<Stories>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toApiStoriesList(value: String?): ApiList<Stories>? {
        return if (value != null) {
            val listType = object : TypeToken<ApiList<Stories>>() {}.type
            gson.fromJson(value, listType);
        } else null
    }
}