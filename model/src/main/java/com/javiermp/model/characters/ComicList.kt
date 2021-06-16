package com.javiermp.model.characters

data class ComicList(
    var available: Int?,
    val returned: Int?,
    val collectionUri: String?,
    val items: List<ComicSummary>?)