package com.javiermp.datasources.characters.remote.mapper

import com.javiermp.datasources.characters.remote.model.*
import com.javiermp.datasources.remote.mapper.RemoteMapper
import com.javiermp.model.characters.*

/**
 * Map a [List<Character>] from [CharactersDataWrapperResponse] instance when data is moving between
 * this later and the Data layer
 */
open class CharactersRemoteMapper : RemoteMapper<List<Character>, CharacterDataWrapperResponse> {

    /**
     * Map an instance of a [CharacterDataWrapperResponse] to a [List<Character] model
     */
    override fun mapFromRemote(remote: CharacterDataWrapperResponse): List<Character> =
        mapResultsFromRemote(remote.characterDataContainer.results)

    fun mapCharacterFromRemote(remote: CharacterDataWrapperResponse): Character =
        mapResultsFromRemote(remote.characterDataContainer.results).first()

    fun mapResultsFromRemote(results: List<CharacterResponse>) : List<Character> =
        results.map { characterResponse ->
            Character(
                id = characterResponse.id,
                name = characterResponse.name,
                description = characterResponse.description,
                resourceUri = characterResponse.resourceUri,
                urls = mapUrlsFromRemote(characterResponse.urls),
                thumbnail = mapThumbnailFromRemote(characterResponse.thumbnail),
                comics = mapComicsFromRemote(characterResponse.comics),
                stories = mapStoriesFromRemote(characterResponse.stories),
                series = mapSeriesFromRemote(characterResponse.series)
            )
        }

    private fun mapUrlsFromRemote(urls: List<UrlResponse>?) : List<Url>? =
        urls?.map {
            Url(
                type = it.type,
                url = it.url
            )
        }

    private fun mapThumbnailFromRemote(imageResponse: ImageResponse?) : Image =
        Image(
            path = imageResponse?.path,
            extension = imageResponse?.extension
        )

    private fun mapComicsFromRemote(comicListResponse: ComicListResponse?) : ComicList =
        ComicList(
            available = comicListResponse?.available,
            returned = comicListResponse?.returned,
            collectionUri = comicListResponse?.collectionUri,
            items = mapComicSummaryListFromRemote(comicListResponse?.items)
        )

    private fun mapComicSummaryListFromRemote(comicSummaryResponse: List<ComicSummaryResponse>?) : List<ComicSummary>? =
        comicSummaryResponse?.map { comicSummary ->
            ComicSummary(
                resourceUri = comicSummary.resourceUri,
                name = comicSummary.name
            )
        }

    private fun mapStoriesFromRemote(storyListResponse: StoryListResponse?) : StoryList =
        StoryList(
            available = storyListResponse?.available,
            returned = storyListResponse?.returned,
            collectionUri = storyListResponse?.collectionUri,
            items = mapStorySummaryListFromRemote(storyListResponse?.items)
        )

    private fun mapStorySummaryListFromRemote(storySummaryResponse: List<StorySummaryResponse>?) : List<StorySummary>? =
        storySummaryResponse?.map { storySummary ->
            StorySummary(
                resourceUri = storySummary.resourceUri,
                name = storySummary.name,
                type = storySummary.type
            )
        }

    private fun mapSeriesFromRemote(seriesListResponse: SeriesListResponse?) : SeriesList =
        SeriesList(
            available = seriesListResponse?.available,
            returned = seriesListResponse?.returned,
            collectionUri = seriesListResponse?.collectionUri,
            items = mapSeriesSummaryListFromRemote(seriesListResponse?.items)
        )

    private fun mapSeriesSummaryListFromRemote(seriesSummaryResponse: List<SeriesSummaryResponse>?) : List<SeriesSummary>? =
        seriesSummaryResponse?.map { seriesSummary ->
            SeriesSummary(
                resourceUri = seriesSummary.resourceUri,
                name = seriesSummary.name
            )
        }
}