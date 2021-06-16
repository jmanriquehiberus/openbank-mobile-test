package com.javiermp.data.characters.source

import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

interface CharactersDataStore {

    fun getCharacters(timestamp: String, hash: String): Single<List<Character>>?
}