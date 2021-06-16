package com.javiermp.datasources.characters.remote.model

import com.google.gson.annotations.SerializedName

data class StoryListResponse(
    @SerializedName("available")
    var available: Int?,
    @SerializedName("returned")
    val returned: Int?,
    @SerializedName("collectionURI")
    val collectionUri: String?,
    @SerializedName("items")
    val items: List<StorySummaryResponse>?)