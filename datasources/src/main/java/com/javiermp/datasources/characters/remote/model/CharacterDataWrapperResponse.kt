package com.javiermp.datasources.characters.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Representation for a [CharacterDataWrapperResponse] fetched from an external layer data source
 */
data class CharacterDataWrapperResponse(
    @SerializedName("data")
    val characterDataContainer: CharacterDataContainerResponse
)