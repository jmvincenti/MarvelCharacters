package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName


data class CharacterDataWrapper<T>(
        @SerializedName("code") var code: Int = 0,
        @SerializedName("status") var status: String? = null,
        @SerializedName("copyright") var copyright: String? = null,
        @SerializedName("attributionText") var attributionText: String? = null,
        @SerializedName("attributionHTML") var getAttributionHtml: String? = null,
        @SerializedName("etag") var etag: String? = null,
        @SerializedName("data") var response: CharacterDataContainer<T>?
)