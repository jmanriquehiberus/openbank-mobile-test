package com.javiermp.model.characters

data class StoryList(
    var available: Int?,
    val returned: Int?,
    val collectionUri: String?,
    val items: List<StorySummary>?)