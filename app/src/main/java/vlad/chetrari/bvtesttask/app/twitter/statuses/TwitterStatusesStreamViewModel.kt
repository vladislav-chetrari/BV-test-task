package vlad.chetrari.bvtesttask.app.twitter.statuses

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import timber.log.Timber
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.network.client.TwitterStreamClient
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class TwitterStatusesStreamViewModel @Inject constructor(
    private val client: TwitterStreamClient,
    private val connectivityManager: ConnectivityManager
) : BaseViewModel() {

    private var streamDisposable: Disposable? = null

    private val liveStatusesManager = TwitterLiveStatusesManager(
        viewModelScope.coroutineContext,
        STATUS_LIFESPAN_SECONDS
    )

    val searchQuery = mutableLiveData("")
    val statuses: LiveData<List<TwitterLiveStatus>> = liveStatusesManager.liveData

    fun onSearchQuery(query: String?) {
        searchQuery.mutable.value = query?.trim() ?: ""
        liveStatusesManager.clear()
        runStream()
    }

    private fun runStream() {
        val query = searchQuery.value!!
        streamDisposable?.dispose()
        if (query.isNotBlank()) {
            streamDisposable = client.search(query)
                .subscribe(liveStatusesManager::add, ::onError)
        }
    }

    override fun onError(error: Throwable) {
        when (error) {
            is SocketTimeoutException -> {
                Timber.w("SocketTimeoutException capt, restarting the stream")
                runStream()
            }
            is SocketException, is UnknownHostException -> {
                liveStatusesManager.onNetworkStateChange(false)
                registerAutoCancellableNetworkCallback {
                    liveStatusesManager.onNetworkStateChange(true)
                }
            }
            else -> super.onError(error)
        }
    }

    private fun registerAutoCancellableNetworkCallback(onNetworkAvailable: () -> Unit) {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    onNetworkAvailable()
                    connectivityManager.unregisterNetworkCallback(this)
                }
            }
        )
    }

    override fun onCleared() {
        streamDisposable?.dispose()
        super.onCleared()
    }

    private companion object {
        const val STATUS_LIFESPAN_SECONDS = 10
    }
}