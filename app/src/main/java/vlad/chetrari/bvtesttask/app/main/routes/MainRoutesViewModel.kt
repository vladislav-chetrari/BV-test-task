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
    val navigateV2StreamSearch = actionLiveData<Result<Bearer>>()
    val navigateV1StreamSearch = actionLiveData<Unit>()

    fun onRouteSelected(route: MainRoute) = when (route) {
        MainRoute.API_V1 -> navigateV1StreamSearch.mutable.postValue(Unit)
        MainRoute.API_V2 -> gainOauth2Bearer()
    }

    private fun gainOauth2Bearer() {
        navigateV2StreamSearch.mutable.postValue(Result.Progress)
        viewModelScope.launch {
            runCatching { oAuth2Client.authenticate() }.fold(
                onSuccess = { navigateV2StreamSearch.mutable.postValue(Result.Success(it)) },
                onFailure = { navigateV2StreamSearch.mutable.postValue(Result.Error(it)) }
            )
        }
    }
}