package vlad.chetrari.bvtesttask.app.twitter.statuses

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import timber.log.Timber
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.network.client.TwitterStreamClient
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class TwitterStatusesStreamViewModel @Inject constructor(
    private val client: TwitterStreamClient
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
        runStream(searchQuery.value!!)
    }

    private fun restartTwitStream() = onSearchQuery(searchQuery.value)

    private fun runStream(query: String) {
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
                restartTwitStream()
            }
            else -> super.onError(error)
        }
    }

    override fun onCleared() {
        streamDisposable?.dispose()
        super.onCleared()
    }

    private companion object {
        const val STATUS_LIFESPAN_SECONDS = 10
    }
}