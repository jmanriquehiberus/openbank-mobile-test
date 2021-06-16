package com.javiermp.openbankmobiletest.characters.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.javiermp.domain.characters.interactor.GetCharacterDetail
import com.javiermp.domain.characters.interactor.GetCharacters
import com.javiermp.model.characters.Character
import com.javiermp.openbankmobiletest.common.model.ResourceState
import com.javiermp.openbankmobiletest.common.viewmodel.CommonEvent
import com.javiermp.openbankmobiletest.common.viewmodel.SingleLiveEvent
import com.javiermp.openbankmobiletest.test.util.CharactersFactory
import com.javiermp.openbankmobiletest.test.util.DataFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.*
import java.util.concurrent.TimeUnit

class CharactersViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val mockGetCharacters = mock<GetCharacters>()
    private val mockGetCharacterDetail = mock<GetCharacterDetail>()
    private val commonLiveEvent = mock<SingleLiveEvent<CommonEvent>>()

    private val testErrorBundleBuilder = TestErrorBundleBuilder()
    private val charactersViewModel = CharactersViewModel(mockGetCharacters, mockGetCharacterDetail, testErrorBundleBuilder)

    @Before
    fun setUp() {
        charactersViewModel.commonLiveEvent = commonLiveEvent
    }

    @Test
    fun getCharactersReturnsSuccess() {
        val characters = CharactersFactory.makeCharactersEntityList(3)
        stubGetCharacters(Single.just(characters))
        charactersViewModel.getCharacters(true)

        Assert.assertTrue(charactersViewModel.getCharactersLiveData().value is ResourceState.Success)
    }

    @Test
    fun getCharactersReturnsDataOnSuccess() {
        val characters = CharactersFactory.makeCharactersEntityList(3)
        stubGetCharacters(Single.just(characters))
        charactersViewModel.getCharacters(true)

        val result = charactersViewModel.getCharactersLiveData().value
        Assert.assertTrue(result is ResourceState.Success && result.data == characters)
    }

    @Test
    fun getCharactersReturnsError() {
        stubGetCharacters(Single.error(RuntimeException()))
        charactersViewModel.getCharacters(true)

        Assert.assertTrue(charactersViewModel.getCharactersLiveData().value is ResourceState.Error)
    }

    @Test
    fun getCharactersFailsAndContainsMessage() {
        val errorMessage = DataFactory.getRandomString(5)
        stubGetCharacters(Single.error(RuntimeException(errorMessage)))
        charactersViewModel.getCharacters(true)

        val result = charactersViewModel.getCharactersLiveData().value
        Assert.assertTrue(result is ResourceState.Error && result.errorBundle.debugMessage == errorMessage)
    }

    @Test
    fun getCharactersReturnsLoading() {
        val characters = CharactersFactory.makeCharactersEntityList(3)
        stubGetCharacters(Single.just(characters).delay(60, TimeUnit.SECONDS))
        charactersViewModel.getCharacters(true)

        Assert.assertTrue(charactersViewModel.getCharactersLiveData().value is ResourceState.Loading)
    }

    private fun stubGetCharacters(single: Single<List<Character>>) {
        whenever(mockGetCharacters.execute(any()))
            .thenReturn(single)
    }
}