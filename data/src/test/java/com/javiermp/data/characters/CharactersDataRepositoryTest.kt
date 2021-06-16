package com.javiermp.data.characters

import com.javiermp.data.characters.factory.CharactersFactory
import com.javiermp.data.characters.factory.DataFactory
import com.javiermp.data.characters.repository.CharactersDataRepository
import com.javiermp.data.characters.source.CharactersMemoryDataStore
import com.javiermp.data.characters.source.CharactersRemoteDataStore
import com.javiermp.model.characters.Character
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
open class CharactersDataRepositoryTest {

    private val charactersRemoteDataStore = mock<CharactersRemoteDataStore>()
    private val charactersMemoryDataStore = mock<CharactersMemoryDataStore>()
    private val charactersDataRepository = CharactersDataRepository(charactersRemoteDataStore, charactersMemoryDataStore)

    @Test
    fun getCharactersFromDataSources() {
        val characters = CharactersFactory.makeCharactersEntityList(3)
        val timestamp = System.currentTimeMillis().toString()
        val hash = DataFactory.getHash(timestamp)
        val single = Single.just(characters)

        stubCharactersMemoryDataStoreClearCharacters(Completable.complete())
        stubCharactersMemoryDataStoreSaveCharacters(Completable.complete(), characters)
        stubCharactersMemoryDataStoreGetCharacters(single, timestamp, hash)
        stubCharactersRemoteDataStoreGetCharacters(single, timestamp, hash)

        testObserver(characters,true, timestamp, hash)
        testObserver(characters,false, timestamp, hash)
    }

    private fun stubCharactersMemoryDataStoreClearCharacters(completable: Completable) =
        whenever(charactersMemoryDataStore.clearCharacters())
            .thenReturn(completable)

    private fun stubCharactersMemoryDataStoreSaveCharacters(completable: Completable, characters: List<Character>) =
        whenever(charactersMemoryDataStore.saveCharacters(characters))
            .thenReturn(completable)

    private fun stubCharactersMemoryDataStoreGetCharacters(single: Single<List<Character>>, timestamp: String, hash: String) =
        whenever(charactersMemoryDataStore.getCharacters(timestamp, hash))
            .thenReturn(single)

    private fun stubCharactersRemoteDataStoreGetCharacters(single: Single<List<Character>>, timestamp: String, hash: String) =
        whenever(charactersRemoteDataStore.getCharacters(timestamp, hash))
            .thenReturn(single)

    private fun testObserver(characters: List<Character>, forceRemote: Boolean, timestamp: String, hash: String) {
        val results = charactersDataRepository.getCharacters(forceRemote, timestamp, hash)
        val testObserver = results.test()
        testObserver.assertValue(characters)
    }
}