package vlad.chetrari.bvtesttask.app.main.stream.search.v1

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseFragment

@AndroidEntryPoint
class TwitStreamSearchFragment : BaseFragment(R.layout.fragment_twit_stream_search) {

    private val viewModel by viewModels<TwitStreamSearchViewModel>()
    private val listAdapter = TwitListAdapter()

    private val toolbar: Toolbar
        get() = requireView().findViewById(R.id.toolbar)
    private val list: RecyclerView
        get() = requireView().findViewById(R.id.list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = listAdapter
        toolbar.setupWithNavController(findNavController())
        viewModel.onSearchQuery("kitten")
    }

    override fun onDestroyView() {
        list.adapter = null
        super.onDestroyView()
    }

    override fun observeLiveData() {
        viewModel.twits.observe(listAdapter::submitList)
    }
}