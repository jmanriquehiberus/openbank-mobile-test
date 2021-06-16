package com.javiermp.data.characters.factory

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

        private fun makeCharacterEntity(): Character =
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

        private fun makeUrlsEntityList(count: Int): List<Url> {
            val urls = mutableListOf<Url>()
            repeat(count) {
                urls.add(Url(DataFactory.getRandomString(5), DataFactory.getRandomString(5)))
            }
            return urls
        }

        private fun makeImageEntity() =
            Image(DataFactory.getRandomString(5), DataFactory.getRandomString(5))

        private fun makeComicListEntity(): ComicList =
            ComicList(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeComicSummaryEntity(3)
            )

        private fun makeComicSummaryEntity(count: Int): List<ComicSummary> {
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

        private fun makeStoryListEntity(): StoryList =
            StoryList(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeStorySummaryEntity(3)
            )

        private fun makeStorySummaryEntity(count: Int): List<StorySummary> {
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

        private fun makeSeriesListEntity(): SeriesList =
            SeriesList(
                DataFactory.getRandomInt(),
                DataFactory.getRandomInt(),
                DataFactory.getRandomString(5),
                makeSeriesSummaryEntity(3)
            )

        private fun makeSeriesSummaryEntity(count: Int): List<SeriesSummary> {
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
