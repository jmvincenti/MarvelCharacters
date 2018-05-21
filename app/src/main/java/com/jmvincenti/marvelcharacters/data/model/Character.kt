package com.jmvincenti.marvelcharacters.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "character")
data class Character(

        @PrimaryKey
        @SerializedName("id")
        var id: Int? = null,

        @ColumnInfo(name = "name")
        @SerializedName("name")
        var name: String? = null,

        @ColumnInfo(name = "description")
        @SerializedName("description")
        var description: String? = null,

        @ColumnInfo(name = "modified")
        @SerializedName("modified")
        var modified: String? = null,

        @ColumnInfo(name = "resourceURI")
        @SerializedName("resourceURI")
        var resourceUri: String? = null,

        @ColumnInfo(name = "urls")
        @SerializedName("urls")
        var urls: List<ApiUrl>? = null,

        @ColumnInfo(name = "thumbnail")
        @SerializedName("thumbnail")
        var thumbnail: ApiImage? = null,

        @ColumnInfo(name = "comics")
        @SerializedName("comics")
        var comics: ApiList<Comic>? = null,

        @ColumnInfo(name = "series")
        @SerializedName("series")
        var series: ApiList<Series>? = null,

        @ColumnInfo(name = "stories")
        @SerializedName("stories")
        var stories: ApiList<Stories>? = null
)