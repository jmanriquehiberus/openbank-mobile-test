package com.javiermp.data.characters.source

import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Completable

interface CharactersMemoryDataStore: CharactersDataStore {
    fun saveCharacters(characters: List<Character>): Completable
    fun clearCharacters(): Completable
}