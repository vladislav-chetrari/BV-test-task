package vlad.chetrari.bvtesttask.app.main.routes

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.model.ui.Bearer
import vlad.chetrari.bvtesttask.data.model.ui.Result
import vlad.chetrari.bvtesttask.data.network.client.TwitterOAuth2Client
import javax.inject.Inject

@HiltViewModel
class MainRoutesViewModel @Inject constructor(
    private val oAuth2Client: TwitterOAuth2Client
) : BaseViewModel() {

    val routes = mutableLiveData(MainRoute.values().toList())
    val oauth2Bearer = actionLiveData<Result<Bearer>>()
    val message = actionLiveData<String>()

    fun onRouteSelected(route: MainRoute) = when (route) {
        MainRoute.API_V1 -> message.mutable.postValue("TBD")
        MainRoute.API_V2 -> gainOauth2Bearer()
    }

    private fun gainOauth2Bearer() {
        oauth2Bearer.mutable.postValue(Result.Progress)
        viewModelScope.launch {
            runCatching { oAuth2Client.authenticate() }.fold(
                onSuccess = { oauth2Bearer.mutable.postValue(Result.Success(it)) },
                onFailure = { oauth2Bearer.mutable.postValue(Result.Error(it)) }
            )
        }
    }
}