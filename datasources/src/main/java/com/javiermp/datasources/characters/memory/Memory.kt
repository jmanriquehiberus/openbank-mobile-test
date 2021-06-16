package com.javiermp.datasources.characters.memory

import com.javiermp.model.characters.Character
import io.reactivex.rxjava3.core.Single

class Memory {

    var characters: MutableList<Character>? = null

    fun getCharacters(): Single<List<Character>>? = characters?.let { Single.just(it) }

    fun wipeData() {
        characters?.clear()
    }
}