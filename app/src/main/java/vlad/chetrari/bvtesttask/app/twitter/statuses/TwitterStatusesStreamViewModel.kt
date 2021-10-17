package vlad.chetrari.bvtesttask.app.twitter.statuses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
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

    private val compositeDisposable = CompositeDisposable()

    val searchQuery = mutableLiveData("")
    val statuses: LiveData<List<TwitterStatus>> = searchQuery.switchMap { twitStreamLiveData(it) }

    fun onSearchQuery(query: String?) = searchQuery.mutable.postValue(query?.trim() ?: "")

    private fun restartTwitStream() = searchQuery.mutable.postValue(searchQuery.value)

    private fun twitStreamLiveData(query: String): LiveData<List<TwitterStatus>> {
        val liveData = MutableLiveData<List<TwitterStatus>>()
        compositeDisposable.clear()
        if (query.isNotBlank()) {
            val twitList = LinkedList<TwitterStatus>()
            compositeDisposable.add(client.search(query).subscribe({ str ->
                twitList.add(str)
                liveData.postValue(twitList.sortedByDescending { it.timestampEpochMillis })
            }, ::onError))
        } else {
            liveData.postValue(emptyList())
        }
        return liveData
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
        compositeDisposable.dispose()
        super.onCleared()
    }
}