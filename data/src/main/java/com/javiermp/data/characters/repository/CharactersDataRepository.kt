package com.javiermp.data.characters.repository

import com.javiermp.data.characters.source.CharactersMemoryDataStore
import com.javiermp.data.characters.source.CharactersRemoteDataStore
import com.javiermp.domain.characters.repository.CharactersRepository
import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

class CharactersDataRepository(
        private val remoteCharactersRepository: CharactersRemoteDataStore,
        private val memoryCharactersDataStore: CharactersMemoryDataStore
) : CharactersRepository {

    override fun getCharacters(forceRemote: Boolean, timestamp: String, hash: String): Single<List<Character>> =
        if (forceRemote) {
            memoryCharactersDataStore.clearCharacters().andThen(
                remoteCharactersRepository.getCharacters(timestamp, hash)?.flatMap { characters ->
                    memoryCharactersDataStore.saveCharacters(characters).andThen(Single.just(characters))
                } ?: throw RuntimeException("Null single on remote characters list")
            )
        } else {
            memoryCharactersDataStore.getCharacters("", "") ?: run {
                remoteCharactersRepository.getCharacters(timestamp, hash)?.flatMap { characters ->
                    memoryCharactersDataStore.saveCharacters(characters).andThen(Single.just(characters))
                } ?: throw RuntimeException("Null single on remote characters list")
            }
        }

    override fun getCharacterDetail(forceRemote: Boolean, timestamp: String, hash: String, characterId: Int?): Single<Character> =
        remoteCharactersRepository.getCharacterDetail(timestamp, hash, characterId) ?: throw RuntimeException("Null single on remote character detail")
}