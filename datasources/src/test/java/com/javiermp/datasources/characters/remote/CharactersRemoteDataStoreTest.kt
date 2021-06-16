package com.javiermp.datasources.characters.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.javiermp.datasources.BuildConfig
import com.javiermp.datasources.characters.factory.DataFactory
import com.javiermp.datasources.characters.remote.factory.CharactersResponseFactory
import com.javiermp.datasources.characters.remote.mapper.CharactersRemoteMapper
import com.javiermp.datasources.characters.remote.model.CharacterDataWrapperResponse
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.*
import org.junit.runner.*

@RunWith(AndroidJUnit4::class)
class CharactersRemoteDataStoreTest {

    private val entityMapper = mock<CharactersRemoteMapper>()
    private val openbankMobileTestService = mock<OpenbankMobileTestService>()
    private val charactersRemoteDataStore = CharactersRemoteImplementation(openbankMobileTestService, entityMapper)

    @Test
    fun getCharactersFromRemote() {
        val charactersResponse = CharactersResponseFactory.makeCharactersResponse()
        val timestamp = System.currentTimeMillis().toString()
        val hash = DataFactory.getHash(timestamp)
        stubOpenbankMobileTestServiceGetCharacters(Single.just(charactersResponse), timestamp, hash)
        val characterEntities = entityMapper.mapFromRemote(charactersResponse)

        val results = charactersRemoteDataStore.getCharacters(timestamp, hash)
        val testObserver = results?.test()
        testObserver?.assertValue(characterEntities)
    }

    private fun stubOpenbankMobileTestServiceGetCharacters(observable: Single<CharacterDataWrapperResponse>, timestamp: String, hash: String) =
        whenever(openbankMobileTestService.getCharacters(
            timestamp,
            BuildConfig.API_KEY,
            hash)).thenReturn(observable)
}