package com.javiermp.domain.characters

import com.javiermp.domain.characters.factory.CharactersFactory
import com.javiermp.domain.characters.factory.DataFactory
import com.javiermp.domain.characters.interactor.GetCharacters
import com.javiermp.domain.characters.repository.CharactersRepository
import com.javiermp.domain.executor.PostExecutionThread
import com.javiermp.domain.executor.ThreadExecutor
import com.javiermp.model.characters.Character
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class GetCharactersTest {

    private val mockThreadExecutor = mock<ThreadExecutor>()
    private val mockPostExecutionThread = mock<PostExecutionThread>()
    private val mockCharactersRepository = mock<CharactersRepository>()
    private val getCharacters = GetCharacters(mockCharactersRepository, mockThreadExecutor, mockPostExecutionThread)

    @Test
    fun buildUseCaseObservableCallsRepository() {
        val timestamp = System.currentTimeMillis().toString()
        val hash = DataFactory.getHash(timestamp)
        verifyCallToRepository(true, timestamp, hash)
        verifyCallToRepository(false, timestamp, hash)
    }

    @Test
    fun buildUseCaseObservableReturnsData() {
        val timestamp = System.currentTimeMillis().toString()
        val hash = DataFactory.getHash(timestamp)
        val characters = CharactersFactory.makeCharactersEntityList(3)

        testObserver(characters, true, timestamp, hash)
        testObserver(characters, false, timestamp, hash)
    }

    private fun stubCharactersRepositoryGetCharacters(single: Single<List<Character>>, forceRemote: Boolean, timestamp: String, hash: String) {
        whenever(mockCharactersRepository.getCharacters(forceRemote, timestamp, hash))
            .thenReturn(single)
    }

    private fun verifyCallToRepository(forceRemote: Boolean, timestamp: String, hash: String) {
        getCharacters.buildUseCaseObservable(GetCharacters.GetCharactersParams(forceRemote, timestamp, hash))
        verify(mockCharactersRepository).getCharacters(forceRemote, timestamp, hash)
    }

    private fun testObserver(characters: List<Character>, forceRemote: Boolean, timestamp: String, hash: String) {
        stubCharactersRepositoryGetCharacters(Single.just(characters), forceRemote, timestamp, hash)
        val testObserver = getCharacters.buildUseCaseObservable(GetCharacters.GetCharactersParams(forceRemote, timestamp, hash)).test()
        testObserver.assertValue(characters)
    }
}