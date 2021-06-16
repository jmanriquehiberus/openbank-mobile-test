package com.javiermp.model.characters

data class SeriesList(
    var available: Int?,
    val returned: Int?,
    val collectionUri: String?,
    val items: List<SeriesSummary>?)