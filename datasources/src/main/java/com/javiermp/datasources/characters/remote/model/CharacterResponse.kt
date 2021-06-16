package com.javiermp.datasources.characters.remote.model

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("resourceURI")
    val resourceUri: String?,
    @SerializedName("urls")
    val urls: List<UrlResponse>?,
    @SerializedName("thumbnail")
    val thumbnail: ImageResponse?,
    @SerializedName("comics")
    val comics: ComicListResponse?,
    @SerializedName("stories")
    val stories: StoryListResponse?,
    @SerializedName("series")
    val series: SeriesListResponse?
)