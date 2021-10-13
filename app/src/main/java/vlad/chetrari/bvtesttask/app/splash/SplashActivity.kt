package vlad.chetrari.bvtesttask.app.splash

import android.widget.TextView
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.app.base.BaseActivity

@AndroidEntryPoint
class SplashActivity : BaseActivity(R.layout.activity_splash) {

    private val viewModel by viewModels<SplashViewModel>()

    override fun observeLiveData() {
        viewModel.response.observe {
            findViewById<TextView>(R.id.text).text = it.toString()
        }
    }
}