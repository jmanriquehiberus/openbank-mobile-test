package com.javiermp.domain.characters.interactor

import com.javiermp.domain.executor.PostExecutionThread
import com.javiermp.domain.executor.ThreadExecutor
import com.javiermp.domain.characters.repository.CharactersRepository
import com.javiermp.domain.interactor.SingleUseCase
import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

open class GetCharacterDetail (
    private val charactersRepository: CharactersRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : SingleUseCase<Character, GetCharacterDetail.GetCharacterDetailParams?>(threadExecutor, postExecutionThread) {

    data class GetCharacterDetailParams(val forceRemote: Boolean = true, val timestamp: String, val hash: String, val characterId: Int?)

    public override fun buildUseCaseObservable(params: GetCharacterDetailParams?): Single<Character> {
        checkNotNull(params)
        return charactersRepository.getCharacterDetail(params.forceRemote, params.timestamp, params.hash, params.characterId)
    }
}