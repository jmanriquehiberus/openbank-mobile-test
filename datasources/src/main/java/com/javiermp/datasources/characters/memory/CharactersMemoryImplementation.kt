package com.javiermp.datasources.characters.memory

import com.javiermp.data.characters.source.CharactersMemoryDataStore
import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class CharactersMemoryImplementation constructor(
    private val memory: Memory
) : CharactersMemoryDataStore {

    override fun getCharacters(timestamp: String, hash: String): Single<List<Character>>? {
        return memory.getCharacters()
    }

    override fun clearCharacters(): Completable {
        return Completable.defer {
            memory.characters?.clear()
            Completable.complete()
        }
    }

    override fun saveCharacters(characters: List<Character>): Completable {
        return Completable.defer {
            memory.characters?.addAll(characters) ?: run {
                memory.characters = characters.toMutableList()
            }
            Completable.complete()
        }
    }
}