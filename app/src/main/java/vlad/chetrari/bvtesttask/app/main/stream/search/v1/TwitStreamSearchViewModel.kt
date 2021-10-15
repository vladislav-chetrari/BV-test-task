package vlad.chetrari.bvtesttask.app.main.stream.search.v1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import timber.log.Timber
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.model.ui.SearchStreamTwit
import vlad.chetrari.bvtesttask.data.network.client.TwitterSearchClient
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TwitStreamSearchViewModel @Inject constructor(
    private val client: TwitterSearchClient
) : BaseViewModel() {

    private var twitStreamDisposable: Disposable? = null

    private val searchKeyword = MutableLiveData<String>()
    val twits: LiveData<List<SearchStreamTwit>> = searchKeyword.switchMap { twitStreamLiveData(it) }

    fun onSearchQuery(query: String) = searchKeyword.postValue(query)

    private fun twitStreamLiveData(query: String): LiveData<List<SearchStreamTwit>> {
        val liveData = MutableLiveData<List<SearchStreamTwit>>()
        twitStreamDisposable?.dispose()
        if (query.isNotBlank()) {
            val twitList = LinkedList<SearchStreamTwit>()
            twitStreamDisposable = client.search(query).subscribe({ str ->
                twitList.add(str)
                liveData.postValue(twitList.sortedByDescending { it.timestampEpochMillis })
            }, ::onError)
        } else {
            liveData.postValue(emptyList())
        }
        return liveData
    }

    private fun restartTwitStream() = searchKeyword.postValue(searchKeyword.value)

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