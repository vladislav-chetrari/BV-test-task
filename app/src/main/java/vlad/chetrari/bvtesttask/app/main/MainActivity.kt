package vlad.chetrari.bvtesttask.app.main

import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private val navigationController: NavController by lazy {
        Navigation.findNavController(this, R.id.navigationHostFragment)
    }

    override fun onSupportNavigateUp(): Boolean = navigationController.navigateUp()
}