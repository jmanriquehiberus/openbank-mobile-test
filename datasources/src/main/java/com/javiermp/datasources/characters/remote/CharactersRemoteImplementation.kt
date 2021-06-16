package com.javiermp.datasources.characters.remote

import com.javiermp.data.characters.source.CharactersRemoteDataStore
import com.javiermp.datasources.BuildConfig
import com.javiermp.datasources.characters.remote.mapper.CharactersRemoteMapper
import com.javiermp.datasources.remote.errorhandling.RemoteExceptionMapper
import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

/**
 * Remote implementation for retrieving Character instances. This class implements the
 * [CharactersRemoteDataStore] from the Data layer as it is that avia_background responsibility for defining the
 * operations in which data store implementation can carry out.
 */
class CharactersRemoteImplementation constructor(
        private val openbankMobileTestService: OpenbankMobileTestService,
        private var charactersRemoteMapper: CharactersRemoteMapper
) : CharactersRemoteDataStore {

    /**
     * Retrieve a list of [Character] from the [OpenbankMobileTestService]
     */
    override fun getCharacters(timestamp: String, hash: String): Single<List<Character>>? {
        return openbankMobileTestService.getCharacters(timestamp, BuildConfig.API_KEY, hash)
            .map { data ->
                charactersRemoteMapper.mapFromRemote(data)
            }
            .onErrorResumeNext { throwable ->
                // If remote request fails, use remote exception mapper to transform Retrofit exception to an app exception
                Single.error(RemoteExceptionMapper.getException(throwable))
            }
    }

    /**
     * Retrieve one [Character] from the [OpenbankMobileTestService]
     */
    override fun getCharacterDetail(timestamp: String, hash: String, characterId: Int?): Single<Character>? {
        return openbankMobileTestService.getCharacterDetail(characterId.toString(), timestamp, BuildConfig.API_KEY, hash)
            .map { data ->
                charactersRemoteMapper.mapCharacterFromRemote(data)
            }
            .onErrorResumeNext { throwable ->
                // If remote request fails, use remote exception mapper to transform Retrofit exception to an app exception
                Single.error(RemoteExceptionMapper.getException(throwable))
            }
    }
}