package com.javiermp.domain.characters.interactor

import com.javiermp.domain.executor.PostExecutionThread
import com.javiermp.domain.executor.ThreadExecutor
import com.javiermp.domain.characters.repository.CharactersRepository
import com.javiermp.domain.interactor.SingleUseCase
import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

open class GetCharacters (
    private val charactersRepository: CharactersRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : SingleUseCase<List<Character>, GetCharacters.GetCharactersParams?>(threadExecutor, postExecutionThread) {

    data class GetCharactersParams(val forceRemote: Boolean = true, val timestamp: String, val hash: String)

    public override fun buildUseCaseObservable(params: GetCharactersParams?): Single<List<Character>> {
        checkNotNull(params)
        return charactersRepository.getCharacters(params.forceRemote, params.timestamp, params.hash)
    }
}