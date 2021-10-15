package vlad.chetrari.bvtesttask.app.main.stream.search.v2

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseFragment

@AndroidEntryPoint
//TODO make common parent fragment out of v1 and v2
class TwitV2StreamSearchFragment : BaseFragment(R.layout.fragment_twit_stream_search) {

    private val viewModel by viewModels<TwitV2StreamSearchViewModel>()
    private val navArgs by navArgs<TwitV2StreamSearchFragmentArgs>()
    private val listAdapter = TwitListAdapter()

    private val toolbar: Toolbar
        get() = requireView().findViewById(R.id.toolbar)
    private val list: RecyclerView
        get() = requireView().findViewById(R.id.list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController(findNavController())
        viewModel.onBearerTokenReceived(navArgs.bearer.token)
        list.adapter = listAdapter
        viewModel.onSearchQuery("kitten")
    }

    override fun observeLiveData() {
        viewModel.twits.observe(listAdapter::submitList)
    }

    override fun onDestroyView() {
        list.adapter = null
        super.onDestroyView()
    }
}