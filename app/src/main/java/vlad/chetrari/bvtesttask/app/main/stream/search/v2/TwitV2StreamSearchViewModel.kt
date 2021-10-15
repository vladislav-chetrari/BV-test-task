package vlad.chetrari.bvtesttask.app.main.stream.search.v2

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import timber.log.Timber
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.model.ui.Twit
import vlad.chetrari.bvtesttask.data.network.client.TwitterV2SearchClient
import vlad.chetrari.bvtesttask.data.network.client.TwitterV2SearchSetupClient
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TwitV2StreamSearchViewModel @Inject constructor(
    private val setupClient: TwitterV2SearchSetupClient,
    private val searchClient: TwitterV2SearchClient
) : BaseViewModel() {

    private var twitStreamDisposable: Disposable? = null

    private val bearerToken = MutableLiveData<String>()
    private val searchKeyword = MutableLiveData<String>()
    private val searchSetUp = MediatorLiveData<Boolean>().apply {
        var token: String? = null
        addSource(bearerToken) { token = it }
        addSource(searchKeyword) { query -> trySetUpSearchReportingState(token, query) }
    }
    val twits: LiveData<List<Twit>> =
        searchSetUp.switchMap { isSearchSetUp -> twitStreamLiveData(bearerToken.value, isSearchSetUp) }

    fun onBearerTokenReceived(token: String) = bearerToken.postValue(token)

    fun onSearchQuery(query: String) = searchKeyword.postValue(query)

    private fun MediatorLiveData<Boolean>.trySetUpSearchReportingState(token: String?, query: String) {
        try {
            if (token == null) {
                throw RuntimeException("bearer token missing!")
            }
            if (query.isBlank()) {
                throw RuntimeException("blank query!")
            }
            viewModelScope.launch {
                runCatching {
                    setupClient.setupSearch(token, query)
                }.fold { postValue(true) }
            }
        } catch (e: RuntimeException) {
            onError(e)
            postValue(false)
        }
    }

    private fun twitStreamLiveData(token: String?, isSearchSetUp: Boolean): LiveData<List<Twit>> {
        val liveData = MutableLiveData<List<Twit>>()
        val twitList = LinkedList<Twit>()
        twitStreamDisposable?.dispose()
        if (isSearchSetUp && token != null) {
            twitStreamDisposable = searchClient.search(token).subscribe({ twit ->
                twitList.add(twit)
                liveData.postValue(twitList.sortedByDescending(Twit::creationTimestampEpochMillis))
            }, ::onError)
        } else {
            liveData.postValue(emptyList())
        }
        return liveData
    }

    private fun restartTwitStream() = searchSetUp.postValue(searchSetUp.value)

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
        twitStreamDisposable?.dispose()
        super.onCleared()
    }
}