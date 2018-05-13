package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class ApiImage(
        @SerializedName("path") var path: String,
        @SerializedName("extension") var extension: String
)