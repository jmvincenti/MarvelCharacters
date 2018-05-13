package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class ApiUrl(
        @SerializedName("type") var type: String,
        @SerializedName("url") var url: String)