package vlad.chetrari.bvtesttask.app.main.routes

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseFragment
import vlad.chetrari.bvtesttask.app.main.routes.MainRoute.values
import vlad.chetrari.bvtesttask.data.model.ui.Result

@AndroidEntryPoint
class MainRoutesFragment : BaseFragment(R.layout.fragment_main_routes) {

    private val viewModel by viewModels<MainRoutesViewModel>()
    private val listAdapter = MainRoutesListAdapter(::onRouteSelected)

    private val coordinator: CoordinatorLayout
        get() = requireView().findViewById(R.id.coordinator)
    private val toolbar: Toolbar
        get() = requireView().findViewById(R.id.toolbar)
    private val progressBar: ProgressBar
        get() = requireView().findViewById(R.id.progressBar)
    private val list: RecyclerView
        get() = requireView().findViewById(R.id.list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController(findNavController())
        list.adapter = listAdapter
        listAdapter.submitList(values().toList())
    }

    override fun observeLiveData() {
        viewModel.routes.observe(listAdapter::submitList)
        viewModel.message.observe { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        viewModel.oauth2Bearer.observe { result ->
            progressBar.isVisible = result.isComplete.not()
            when (result) {
                is Result.Error -> Snackbar.make(coordinator, result.throwable.message.toString(), Snackbar.LENGTH_SHORT).show()
                is Result.Success -> findNavController().navigate(
                    MainRoutesFragmentDirections.actionMainRoutesFragmentToTwitStreamSearchFragment(result.value)
                )
            }
        }
    }

    override fun onDestroyView() {
        list.adapter = null
        super.onDestroyView()
    }

    private fun onRouteSelected(route: MainRoute) = viewModel.onRouteSelected(route)
}