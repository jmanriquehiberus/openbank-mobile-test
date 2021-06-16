package com.javiermp.openbankmobiletest.characters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javiermp.domain.characters.interactor.GetCharacterDetail
import com.javiermp.domain.characters.interactor.GetCharacters
import com.javiermp.model.characters.Character
import com.javiermp.openbankmobiletest.BuildConfig
import com.javiermp.openbankmobiletest.common.errorhandling.AppAction
import com.javiermp.openbankmobiletest.common.errorhandling.ErrorBundleBuilder
import com.javiermp.openbankmobiletest.common.model.ResourceState
import com.javiermp.openbankmobiletest.common.model.ResourceState.*
import com.javiermp.openbankmobiletest.common.viewmodel.CommonEventsViewModel
import com.javiermp.openbankmobiletest.common.viewmodel.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

typealias CharactersState = ResourceState<List<Character>>
typealias CharacterDetailState = ResourceState<Character>

class CharactersViewModel(
    private val getCharacters: GetCharacters,
    private val getCharacterDetail: GetCharacterDetail,
    private val errorBuilder: ErrorBundleBuilder
) : CommonEventsViewModel() {

    val charactersNavigationLiveEvent = SingleLiveEvent<CharactersNavigationCommand>()
    private val charactersLiveData: MutableLiveData<CharactersState> = MutableLiveData()
    private val characterDetailLiveData: MutableLiveData<CharacterDetailState> = MutableLiveData()
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    var selectedCharacterId: Int? = null

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getCharactersLiveData(): LiveData<CharactersState?> = charactersLiveData

    fun getCharacterDetailLiveData(): LiveData<CharacterDetailState?> = characterDetailLiveData

    fun getCharacters(forceRemote: Boolean = false) {
        val timestamp: String = getTimestamp()
        val hash: String = getHash(timestamp)
        charactersLiveData.value = Loading()
        compositeDisposable.add(getCharacters.execute(GetCharacters.GetCharactersParams(forceRemote, timestamp, hash))
            .subscribe({
                charactersLiveData.value = Success(it)
            }, {
                charactersLiveData.value = Error(errorBuilder.build(it, AppAction.GET_CHARACTERS))
                Timber.e(it)
            }))
    }

    fun getCharacterDetail(forceRemote: Boolean = false) {
        characterDetailLiveData.value = Loading()

        val timestamp: String = System.currentTimeMillis().toString()
        val hash: String = getHash(timestamp)

        compositeDisposable.add(getCharacterDetail.execute(GetCharacterDetail.GetCharacterDetailParams(forceRemote, timestamp, hash, selectedCharacterId))
            .subscribe({
                characterDetailLiveData.value = Success(it)
            }, {
                characterDetailLiveData.value = Error(errorBuilder.build(it, AppAction.GET_CHARACTER_DETAIL))
                Timber.e(it)
            }))
    }

    private fun getTimestamp() = System.currentTimeMillis().toString()

    private fun getHash(timestamp: String): String {
        val md = MessageDigest.getInstance("MD5")
        val input = "$timestamp${BuildConfig.PRIVATE_KEY}${BuildConfig.API_KEY}"
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0').toLowerCase(Locale.ROOT)
    }

    fun onGoToDetail() {
        charactersNavigationLiveEvent.value = CharactersNavigationCommand.GoToDetail
    }
}