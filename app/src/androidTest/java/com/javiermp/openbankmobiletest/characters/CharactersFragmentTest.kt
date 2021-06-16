package com.javiermp.openbankmobiletest.characters

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.javiermp.domain.characters.repository.CharactersRepository
import com.javiermp.model.characters.Character
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.common.di.appModule
import com.javiermp.openbankmobiletest.common.di.charactersModule
import com.javiermp.openbankmobiletest.common.di.webviewModule
import com.javiermp.openbankmobiletest.main.MainActivity
import com.javiermp.openbankmobiletest.test.util.CharactersFactory
import com.javiermp.openbankmobiletest.test.util.DataFactory
import com.javiermp.openbankmobiletest.test.util.RecyclerViewMatcher
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.*
import org.junit.runner.*
import org.koin.core.context.loadKoinModules
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.*

@RunWith(AndroidJUnit4::class)
class CharactersFragmentTest : KoinTest {

    // Deprecated ActivityTestRule cannot be replaced directly for ActivityScenarioRule because there's no way to delay
    // the launch of the activity under test until repository is configured. Therefore, ActivityScenario.launch() must
    // be used: 1) configure stub repository, 2) launch activity, 3) do the test, 4) clean the scenario after finish.
    // ActivityTestRule was cleaning device state automatically, but ActivityScenarioRule doesn't do any cleaning.
    // See: https://medium.com/stepstone-tech/better-tests-with-androidxs-activityscenario-in-kotlin-part-1-6a6376b713ea
    //      https://github.com/android/android-test/issues/446
    lateinit var scenario: ActivityScenario<MainActivity>

    // To let you use the declareMock API, you need to specify a rule to let Koin know how you build your Mock instance.
    // This let you choose the right mocking framework for your need. This function lets know Koin that mocks are
    // created using Mockito.
    // See: https://github.com/InsertKoinIO/koin/blob/master/koin-projects/docs/reference/koin-test/testing.md
    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Before
    fun setUp() {
        loadKoinModules(listOf(
            appModule,
            charactersModule,
            webviewModule))
    }

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun activityLaunches() {
        val timestamp = System.currentTimeMillis().toString()
        val hash = DataFactory.getHash(timestamp)
        stubCharactersRepositoryGetCharacters(Single.just(CharactersFactory.makeCharactersEntityList(3)))

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)
    }

    @Test
    fun characterDisplay() {
        val timestamp = System.currentTimeMillis().toString()
        val hash = DataFactory.getHash(timestamp)
        val characters = CharactersFactory.makeCharactersEntityList(1)
        stubCharactersRepositoryGetCharacters(Single.just(characters))

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        checkCharacterDetailsDisplay(characters[0], 0)
    }

    @Test
    fun charactersAreScrollable() {
        val timestamp = System.currentTimeMillis().toString()
        val hash = DataFactory.getHash(timestamp)
        val characters = CharactersFactory.makeCharactersEntityList(20)
        stubCharactersRepositoryGetCharacters(Single.just(characters))

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        scenario = launchActivity(intent)

        characters.forEachIndexed { index, character ->
            onView(withId(R.id.rv_characters_fragment_list)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index)
            )
            checkCharacterDetailsDisplay(character, index)
        }
    }

    private fun checkCharacterDetailsDisplay(character: Character, position: Int) {
        onView(RecyclerViewMatcher.withRecyclerView(R.id.rv_characters_fragment_list).atPosition(position))
            .check(matches(hasDescendant(withText(character.name))))
        onView(RecyclerViewMatcher.withRecyclerView(R.id.rv_characters_fragment_list).atPosition(position))
            .check(matches(hasDescendant(withText(character.name))))
    }

    private fun stubCharactersRepositoryGetCharacters(single: Single<List<Character>>) {
        // Mock is declared with a stubbing function in order to allow that each repository instance created by the
        // Koin factory is properly mocked and getCharacters() returns test data.
        declareMock<CharactersRepository> {
            whenever(this.getCharacters(any(), any(), any())).thenReturn(single)
        }
    }
}