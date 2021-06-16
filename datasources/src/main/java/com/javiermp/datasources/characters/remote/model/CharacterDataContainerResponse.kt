package com.javiermp.datasources.characters.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Representation for a [CharacterDataContainerResponse] fetched from an external layer data source
 */
data class CharacterDataContainerResponse(
    @SerializedName("results")
    val results: List<CharacterResponse>
)