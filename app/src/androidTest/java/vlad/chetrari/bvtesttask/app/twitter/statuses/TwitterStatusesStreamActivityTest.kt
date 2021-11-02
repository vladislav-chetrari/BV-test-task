package vlad.chetrari.bvtesttask.app.twitter.statuses

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.isKeyboardShown
import vlad.chetrari.bvtesttask.recyclerViewMatches

@RunWith(AndroidJUnit4::class)
class TwitterStatusesStreamActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<TwitterStatusesStreamActivity> =
        ActivityScenarioRule(TwitterStatusesStreamActivity::class.java)

    @Test
    fun searchFab_whenClicked_thenShowsSearchCardViewAndKeyboard() {
        onView(withId(R.id.searchFab))
            .perform(click())
        onView(withId(R.id.searchCardView))
            .check(matches(isDisplayed()))
        assertTrue(isKeyboardShown())
    }

    @Test
    fun searchFab_whenSearchCardViewVisibleAndClicked_thenHidesSearchCardView() {
        searchFab_whenClicked_thenShowsSearchCardViewAndKeyboard()

        onView(withId(R.id.searchFab))
            .perform(click())
        onView(withId(R.id.searchCardView))
            .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun searchCats() {
        searchFab_whenClicked_thenShowsSearchCardViewAndKeyboard()

        onView(withId(R.id.searchInput))
            .perform(typeText("cat"))
        onView(withId(R.id.searchFab))
            .perform(click())

        Thread.sleep(10_000)

        onView(withId(R.id.list))
            .check(matches(recyclerViewMatches { it.childCount != 0 }))
    }
}