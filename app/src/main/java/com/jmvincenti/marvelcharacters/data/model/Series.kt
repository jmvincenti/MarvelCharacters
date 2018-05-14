package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

/**
 * TODO: Add a class header comment! 😘
 */
data class Series(
        @SerializedName("resourceURI") var resourceURI: String?,
        @SerializedName("name") var name: String?)