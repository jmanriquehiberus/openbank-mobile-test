package com.javiermp.datasources.characters.remote.factory

import com.javiermp.datasources.characters.factory.DataFactory
import com.javiermp.datasources.characters.remote.model.*

/**
 * Factory class for CharacterResponse related instances
 */
class CharactersResponseFactory {

    companion object Factory {

        fun makeCharactersResponse(): CharacterDataWrapperResponse =
            CharacterDataWrapperResponse(CharacterDataContainerResponse(makeCharactersResponseList(3)))

        private fun makeCharactersResponseList(count: Int): List<CharacterResponse> {
            val characters = mutableListOf<CharacterResponse>()
            repeat(count) {
                characters.add(makeCharacterResponse())
            }
            return characters
        }

        private fun makeCharacterResponse(): CharacterResponse =
            CharacterResponse(
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                DataFactory.getRandomString(5),
                DataFactory.getRandomString(5),
                makeUrlsResponseList(3),
                makeImageResponse(),
                makeComicListResponse(),
                makeStoryListResponse(),
                makeSeriesListResponse()
            )

        private fun makeUrlsResponseList(count: Int): List<UrlResponse> {
            val urls = mutableListOf<UrlResponse>()
            repeat(count) {
                urls.add(UrlResponse(DataFactory.getRandomString(5), DataFactory.getRandomString(5)))
            }
            return urls
        }

        private fun makeImageResponse() = ImageResponse(DataFactory.getRandomString(5), DataFactory.getRandomString(5))

        private fun makeComicListResponse(): ComicListResponse =
            ComicListResponse(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeComicSummaryResponse(3)
            )

        private fun makeComicSummaryResponse(count: Int): List<ComicSummaryResponse> {
            val comicSummaries = mutableListOf<ComicSummaryResponse>()
            repeat(count) {
                comicSummaries.add(ComicSummaryResponse(DataFactory.getRandomString(5), DataFactory.getRandomString(5)))
            }
            return comicSummaries
        }

        private fun makeStoryListResponse(): StoryListResponse =
            StoryListResponse(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeStorySummaryResponse(3)
            )

        private fun makeStorySummaryResponse(count: Int): List<StorySummaryResponse> {
            val storySummaries = mutableListOf<StorySummaryResponse>()
            repeat(count) {
                storySummaries.add(StorySummaryResponse(DataFactory.getRandomString(5), DataFactory.getRandomString(5), DataFactory.getRandomString(5)))
            }
            return storySummaries
        }

        private fun makeSeriesListResponse(): SeriesListResponse =
            SeriesListResponse(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeSeriesSummaryResponse(3)
            )

        private fun makeSeriesSummaryResponse(count: Int): List<SeriesSummaryResponse> {
            val seriesSummaries = mutableListOf<SeriesSummaryResponse>()
            repeat(count) {
                seriesSummaries.add(SeriesSummaryResponse(DataFactory.getRandomString(5), DataFactory.getRandomString(5)))
            }
            return seriesSummaries
        }
    }
}