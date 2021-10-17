package vlad.chetrari.bvtesttask.app.twitter.statuses

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.getSystemService
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseActivity

@AndroidEntryPoint
class TwitterStatusesStreamActivity : BaseActivity(R.layout.activity_twitter_statuses_stream) {

    private val viewModel by viewModels<TwitterStatusesStreamViewModel>()
    private val listAdapter = TwitterStatusListAdapter()

    private val list: RecyclerView
        get() = findViewById(R.id.list)
    private val searchFab: FloatingActionButton
        get() = findViewById(R.id.searchFab)
    private val searchCardView: CardView
        get() = findViewById(R.id.searchCardView)
    private val searchInputLayout: TextInputLayout
        get() = findViewById(R.id.searchInputLayout)
    private val searchInput: TextInputEditText
        get() = findViewById(R.id.searchInput)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list.adapter = listAdapter
        setupSearch()
    }

    override fun observeLiveData() {
        viewModel.statuses.observe(listAdapter::submitList)
        viewModel.searchQuery.observe { query ->
            searchInput.setText(query)
            title =
                if (query.isBlank()) getString(R.string.twitter_statuses_stream_title)
                else getString(R.string.twitter_statuses_stream_title_format, query)
        }
    }

    override fun onDestroy() {
        list.adapter = null
        super.onDestroy()
    }

    private fun setupSearch() {
        searchFab.setOnClickListener {
            searchFab.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    if (searchCardView.isInvisible) R.drawable.ic_done else R.drawable.ic_search
                )
            )
            if (searchCardView.isVisible) {
                viewModel.onSearchQuery(searchInput.text?.toString())
                hideKeyboard()
                searchCardView.isInvisible = true
            } else {
                searchCardView.isVisible = true
                searchInput.requestFocus()
                showKeyboard(searchInput)
            }
        }
        searchInputLayout.setEndIconOnClickListener {
            viewModel.onSearchQuery(null)
            searchInput.text = null
        }
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFab.callOnClick()
            }
            true
        }
    }

    private fun hideKeyboard() {
        val currentFocusedView = currentFocus ?: return
        getSystemService<InputMethodManager>()
            ?.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        getSystemService<InputMethodManager>()
            ?.showSoftInput(view, 0)
    }
}