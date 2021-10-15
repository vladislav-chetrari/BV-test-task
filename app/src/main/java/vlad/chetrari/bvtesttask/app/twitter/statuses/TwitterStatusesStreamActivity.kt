package vlad.chetrari.bvtesttask.app.twitter.statuses

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseActivity

@AndroidEntryPoint
class TwitterStatusesStreamActivity : BaseActivity(R.layout.activity_twitter_statuses_stream) {

    private val viewModel by viewModels<TwitterStatusesStreamViewModel>()
    private val listAdapter = TwitterStatusListAdapter()

    private val list: RecyclerView
        get() = findViewById(R.id.list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list.adapter = listAdapter
        title = getString(R.string.twitter_statuses_stream_title)
        //TODO make searchable list
        viewModel.onSearchQuery("kitten")
    }

    override fun observeLiveData() {
        viewModel.twits.observe(listAdapter::submitList)
    }

    override fun onDestroy() {
        list.adapter = null
        super.onDestroy()
    }
}