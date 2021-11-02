package vlad.chetrari.bvtesttask

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Description
import org.hamcrest.Matcher


fun isKeyboardShown(): Boolean {
    val inputMethodManager = InstrumentationRegistry.getInstrumentation().targetContext
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.isAcceptingText
}

fun recyclerViewMatches(condition: (RecyclerView) -> Boolean): Matcher<View?> =
    object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) = Unit
        override fun matchesSafely(item: RecyclerView): Boolean = condition(item)
    }