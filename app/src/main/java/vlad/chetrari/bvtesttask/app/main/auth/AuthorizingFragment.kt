package vlad.chetrari.bvtesttask.app.main.auth

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseFragment
import vlad.chetrari.bvtesttask.data.model.ui.Result

@AndroidEntryPoint
class AuthorizingFragment : BaseFragment(R.layout.fragment_authorizing) {

    private val viewModel by viewModels<AuthorizingViewModel>()

    private val toolbar: Toolbar
        get() = requireView().findViewById(R.id.toolbar)
    private val progressBar: ProgressBar
        get() = requireView().findViewById(R.id.progressBar)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController(findNavController())
    }

    override fun observeLiveData() {
        viewModel.bearerToken.observe { result ->
            progressBar.isVisible = result.isComplete.not()
            when (result) {
                is Result.Error -> Toast.makeText(requireContext(), result.throwable.message, Toast.LENGTH_SHORT).show()
                is Result.Success -> onBearerTokenReceived(result.value)
            }
        }
    }

    private fun onBearerTokenReceived(token: String) {
        toolbar.setTitle(R.string.authorized_title)
        findNavController().navigate(
            AuthorizingFragmentDirections.actionAuthorizingFragmentToStreamSearchFragment(token)
        )
    }
}