package com.jmvincenti.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class ApiUrl(
        @SerializedName("type") var type: String,
        @SerializedName("url") var url: String) {
    companion object {
        const val TYPE_WIKI = "wiki"
        const val TYPE_DETAIL = "detail"
        const val TYPE_COMICLINK = "comiclink"
    }
}