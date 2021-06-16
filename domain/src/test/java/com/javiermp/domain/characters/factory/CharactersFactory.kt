package com.javiermp.domain.characters.factory

import com.javiermp.model.characters.*

/**
 * Factory class for Character related instances
 */
class CharactersFactory {

    companion object Factory {

        fun makeCharactersEntityList(count: Int): List<Character> {
            val characters = mutableListOf<Character>()
            repeat(count) {
                characters.add(makeCharacterEntity())
            }
            return characters
        }

        fun makeCharacterEntity(): Character =
            Character(
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                DataFactory.getRandomString(5),
                DataFactory.getRandomString(5),
                makeUrlsEntityList(3),
                makeImageEntity(),
                makeComicListEntity(),
                makeStoryListEntity(),
                makeSeriesListEntity()
            )

        fun makeUrlsEntityList(count: Int): List<Url> {
            val urls = mutableListOf<Url>()
            repeat(count) {
                urls.add(Url(DataFactory.getRandomString(5), DataFactory.getRandomString(5)))
            }
            return urls
        }

        fun makeImageEntity() =
            Image(DataFactory.getRandomString(5), DataFactory.getRandomString(5))

        fun makeComicListEntity(): ComicList =
            ComicList(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeComicSummaryEntity(3)
            )

        fun makeComicSummaryEntity(count: Int): List<ComicSummary> {
            val comicSummaries = mutableListOf<ComicSummary>()
            repeat(count) {
                comicSummaries.add(
                    ComicSummary(
                        DataFactory.getRandomString(5),
                        DataFactory.getRandomString(5)
                    )
                )
            }
            return comicSummaries
        }

        fun makeStoryListEntity(): StoryList =
            StoryList(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeStorySummaryEntity(3)
            )

        fun makeStorySummaryEntity(count: Int): List<StorySummary> {
            val storySummaries = mutableListOf<StorySummary>()
            repeat(count) {
                storySummaries.add(
                    StorySummary(
                        DataFactory.getRandomString(5),
                        DataFactory.getRandomString(5),
                        DataFactory.getRandomString(5)
                    )
                )
            }
            return storySummaries
        }

        fun makeSeriesListEntity(): SeriesList =
            SeriesList(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeSeriesSummaryEntity(3)
            )

        fun makeSeriesSummaryEntity(count: Int): List<SeriesSummary> {
            val seriesSummaries = mutableListOf<SeriesSummary>()
            repeat(count) {
                seriesSummaries.add(
                    SeriesSummary(
                        DataFactory.getRandomString(5),
                        DataFactory.getRandomString(5)
                    )
                )
            }
            return seriesSummaries
        }
    }
}