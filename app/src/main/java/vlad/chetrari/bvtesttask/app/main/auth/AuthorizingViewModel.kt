package vlad.chetrari.bvtesttask.app.main.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.model.ui.Result
import vlad.chetrari.bvtesttask.data.network.client.TwitterOAuth2Client
import javax.inject.Inject

@HiltViewModel
class AuthorizingViewModel @Inject constructor(
    private val client: TwitterOAuth2Client
) : BaseViewModel() {

    val bearerToken: LiveData<Result<String>> = liveData {
        emit(Result.Progress)
        runCatching { client.authenticate() }.fold(
            onSuccess = { emit(Result.Success(it.token)) },
            onFailure = { emit(Result.Error(it)) }
        )
    }
}