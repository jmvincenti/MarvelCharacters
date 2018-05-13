package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName


data class Character(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("description")
        var description: String? = null,
        @SerializedName("modified")
        var modified: String? = null,
        @SerializedName("resourceURI")
        var resourceUri: String? = null,
        @SerializedName("urls")
        var urls: List<ApiUrl>? = null,
        @SerializedName("thumbnail")
        var thumbnail: ApiImage? = null)