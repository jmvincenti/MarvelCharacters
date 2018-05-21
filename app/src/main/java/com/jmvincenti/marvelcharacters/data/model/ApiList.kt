package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName


class ApiList<T>(
        @SerializedName("available") var available: Int = 0,
        @SerializedName("returned") var returned: Int = 0,
        @SerializedName("collectionURI") var collectionUri: String? = null,
        @SerializedName("items") var items: List<T>? = null
)