package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class CharacterDataContainer<T>(
        @SerializedName("offset") var offset: Int?,
        @SerializedName("limit") var limit: Int?,
        @SerializedName("total") var total: Int?,
        @SerializedName("count") var count: Int?,
        @SerializedName("results") var results: List<T>?
)