package vlad.chetrari.bvtesttask.app.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.network.client.TwitterOAuthClient
import vlad.chetrari.bvtesttask.data.network.model.response.BearerTokenResponse
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val client: TwitterOAuthClient
) : BaseViewModel() {

    val response: LiveData<BearerTokenResponse> = liveData {
        runCatching { client.authenticate() }.fold { emit(it) }
    }
}