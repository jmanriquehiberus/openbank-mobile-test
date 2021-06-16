package com.javiermp.datasources.characters.remote.model

import com.google.gson.annotations.SerializedName

data class StorySummaryResponse(
    @SerializedName("resourceURI")
    var resourceUri: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?)