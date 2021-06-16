package com.javiermp.model.characters

data class Character(
    var id: Int?,
    val name: String?,
    val description: String?,
    val resourceUri: String?,
    val urls: List<Url>?,
    val thumbnail: Image?,
    val comics: ComicList?,
    val stories: StoryList?,
    val series: SeriesList?)