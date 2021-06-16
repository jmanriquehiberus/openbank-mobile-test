package com.javiermp.data.characters.source

import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

interface CharactersRemoteDataStore: CharactersDataStore {

    fun getCharacterDetail(timestamp: String, hash: String, characterId: Int?): Single<Character>?
}