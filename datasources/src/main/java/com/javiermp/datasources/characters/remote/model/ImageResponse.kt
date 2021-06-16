package com.javiermp.datasources.characters.remote.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("path")
    var path: String?,
    @SerializedName("extension")
    val extension: String?)