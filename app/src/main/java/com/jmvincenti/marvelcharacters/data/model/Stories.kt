package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

/**
 * TODO: Add a class header comment! 😘
 */
data class Stories(
        @SerializedName("resourceURI") var resourceURI: String?,
        @SerializedName("name") var name: String?,
        @SerializedName("type") var type: String?)