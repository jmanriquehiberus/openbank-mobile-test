package com.javiermp.datasources.characters.remote.mapper

import com.javiermp.datasources.characters.remote.factory.CharactersResponseFactory
import org.junit.*
import org.junit.runner.*
import org.junit.runners.*
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class CharactersMapperTest {

    private val charactersRemoteMapper = CharactersRemoteMapper()

    @Test
    fun mapFromRemoteMapsData() {
        val charactersResponse = CharactersResponseFactory.makeCharactersResponse()
        val charactersEntity = charactersRemoteMapper.mapFromRemote(charactersResponse)

        repeat (charactersEntity.count()) { characterIndex ->
            //General character attributes
            val characterResponse = charactersResponse.characterDataContainer.results[characterIndex]
            val characterEntity = charactersEntity[characterIndex]
            val urlsResponse = characterResponse.urls!!
            val urlsEntity = characterEntity.urls!!

            //Comics
            val comicListResponse = characterResponse.comics!!
            val comicListEntity = characterEntity.comics!!
            val comicSummariesResponse = comicListResponse.items!!
            val comicSummariesEntity = comicListEntity.items!!

            //Stories
            val storyListResponse = characterResponse.stories!!
            val storyListEntity = characterEntity.stories!!
            val storySummariesResponse = storyListResponse.items!!
            val storySummariesEntity = storyListEntity.items!!

            //Series
            val seriesListResponse = characterResponse.series!!
            val seriesListEntity = characterEntity.series!!
            val seriesSummariesResponse = seriesListResponse.items!!
            val seriesSummariesEntity = seriesListEntity.items!!

            ////General character attributes
            assertEquals(characterResponse.id, characterEntity.id)
            assertEquals(characterResponse.name, characterEntity.name)
            assertEquals(characterResponse.description, characterEntity.description)
            assertEquals(characterResponse.resourceUri, characterEntity.resourceUri)
            repeat (urlsResponse.count()) {
                assertEquals(urlsResponse[it].type, urlsEntity[it].type)
                assertEquals(urlsResponse[it].url, urlsEntity[it].url)
            }
            assertEquals(characterResponse.thumbnail!!.path, characterEntity.thumbnail!!.path)

            //Comics
            assertEquals(comicListResponse.available, comicListEntity.available)
            assertEquals(comicListResponse.returned, comicListEntity.returned)
            assertEquals(comicListResponse.collectionUri, comicListEntity.collectionUri)
            repeat (comicSummariesEntity.count()) {
                assertEquals(comicSummariesEntity[it].name, comicSummariesEntity[it].name)
                assertEquals(comicSummariesEntity[it].resourceUri, comicSummariesEntity[it].resourceUri)
            }

            //Stories
            assertEquals(storyListResponse.available, storyListEntity.available)
            assertEquals(storyListResponse.returned, storyListEntity.returned)
            assertEquals(storyListResponse.collectionUri, storyListEntity.collectionUri)
            repeat (storySummariesEntity.count()) {
                assertEquals(storySummariesResponse[it].name, storySummariesEntity[it].name)
                assertEquals(storySummariesResponse[it].resourceUri, storySummariesEntity[it].resourceUri)
                assertEquals(storySummariesResponse[it].type, storySummariesEntity[it].type)
            }

            //Series
            assertEquals(seriesListResponse.available, seriesListEntity.available)
            assertEquals(seriesListResponse.returned, seriesListEntity.returned)
            assertEquals(seriesListResponse.collectionUri, seriesListEntity.collectionUri)
            repeat (seriesSummariesEntity.count()) {
                assertEquals(seriesSummariesResponse[it].name, seriesSummariesEntity[it].name)
                assertEquals(seriesSummariesResponse[it].resourceUri, seriesSummariesEntity[it].resourceUri)
            }
        }
    }
}