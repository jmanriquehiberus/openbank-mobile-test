package com.javiermp.datasources.characters.remote

import com.javiermp.datasources.characters.remote.model.CharacterDataWrapperResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

/**
 * Defines the abstract methods used for interacting with the Marvel API
 */
interface OpenbankMobileTestService {

    companion object {
        const val BASE_URL = "https://gateway.marvel.com:443/"
        private const val CHARACTERS_PATH = "v1/public/characters"
    }

    @GET(CHARACTERS_PATH)
    fun getCharacters(@Query("ts") timestamp: String,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String): Single<CharacterDataWrapperResponse>

    @GET("$CHARACTERS_PATH/{id}")
    fun getCharacterDetail(@Path("id") id: String,
                           @Query("ts") timestamp: String,
                           @Query("apikey") apikey: String,
                           @Query("hash") hash: String): Single<CharacterDataWrapperResponse>
}