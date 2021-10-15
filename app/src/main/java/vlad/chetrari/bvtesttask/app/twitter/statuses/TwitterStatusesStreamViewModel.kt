package vlad.chetrari.bvtesttask.app.twitter.statuses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import timber.log.Timber
import vlad.chetrari.bvtesttask.app.base.BaseViewModel
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus
import vlad.chetrari.bvtesttask.data.network.client.TwitterStreamClient
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TwitterStatusesStreamViewModel @Inject constructor(
    private val client: TwitterStreamClient
) : BaseViewModel() {

    private var twitStreamDisposable: Disposable? = null

    private val searchKeyword = MutableLiveData<String>()
    val twits: LiveData<List<TwitterStatus>> = searchKeyword.switchMap { twitStreamLiveData(it) }

    fun onSearchQuery(query: String) = searchKeyword.postValue(query)

    private fun twitStreamLiveData(query: String): LiveData<List<TwitterStatus>> {
        val liveData = MutableLiveData<List<TwitterStatus>>()
        twitStreamDisposable?.dispose()
        if (query.isNotBlank()) {
            val twitList = LinkedList<TwitterStatus>()
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