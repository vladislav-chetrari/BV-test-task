package vlad.chetrari.bvtesttask.app.main.routes

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseFragment

class MainRoutesFragment : BaseFragment(R.layout.fragment_main_routes) {

    private val listAdapter = MainRoutesListAdapter(::onRouteSelected)

    private val toolbar: Toolbar
        get() = requireView().findViewById(R.id.toolbar)
    private val list: RecyclerView
        get() = requireView().findViewById(R.id.list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController(findNavController())
        list.adapter = listAdapter
        listAdapter.submitList(MainRoute.values().toList())
    }

    override fun onDestroyView() {
        list.adapter = null
        super.onDestroyView()
    }

    private fun onRouteSelected(route: MainRoute) {
        //TODO navigate
    }
}