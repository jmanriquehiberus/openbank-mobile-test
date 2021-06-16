package com.javiermp.domain.characters.repository

import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

interface CharactersRepository {

    fun getCharacters(forceRemote: Boolean, timestamp: String, hash: String): Single<List<Character>>
    fun getCharacterDetail(forceRemote: Boolean, timestamp: String, hash: String, characterId: Int?): Single<Character>
}