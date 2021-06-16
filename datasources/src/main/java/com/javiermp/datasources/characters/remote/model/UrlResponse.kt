package com.javiermp.datasources.characters.remote.model

import com.google.gson.annotations.SerializedName

data class UrlResponse(
    @SerializedName("type")
    var type: String,
    @SerializedName("url")
    val url: String)