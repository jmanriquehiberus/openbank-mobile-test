package com.javiermp.datasources.characters.memory

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.javiermp.datasources.characters.factory.DataFactory
import com.javiermp.datasources.characters.memory.factory.CharactersEntityFactory
import org.junit.*
import org.junit.runner.*

@RunWith(AndroidJUnit4::class)
class CharactersMemoryImplementationTest {

    private val memory = Memory()
    private val charactersMemoryDataStore = CharactersMemoryImplementation(memory)

    @Test
    fun getCharactersFromMemory() {
        val characters = CharactersEntityFactory.makeCharactersEntityList(3)
        memory.characters?.addAll(characters) ?: run { memory.characters = characters.toMutableList() }
        val results = charactersMemoryDataStore.getCharacters(DataFactory.getRandomString(5), DataFactory.getRandomString(5))
        val testObserver = results?.test()
        testObserver?.assertValue(characters)
    }

    @Test
    fun clearCharactersFromMemory() {
        val testObserver = charactersMemoryDataStore.clearCharacters().test()
        testObserver.assertComplete()
    }

    @Test
    fun saveCharactersIntoMemory() {
        val characters = CharactersEntityFactory.makeCharactersEntityList(3)
        val testObserver = charactersMemoryDataStore.saveCharacters(characters).test()
        testObserver.assertComplete()
    }
}