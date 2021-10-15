package vlad.chetrari.bvtesttask.data.network.client

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.internal.toLongOrDefault
import timber.log.Timber
import vlad.chetrari.bvtesttask.data.model.interchange.response.SearchStreamTwitResponse
import vlad.chetrari.bvtesttask.data.model.ui.SearchStreamTwit
import vlad.chetrari.bvtesttask.data.network.api.TwitterStreamApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterSearchClient @Inject constructor(
    private val gson: Gson,
    private val api: TwitterStreamApi
) {

    fun search(keyword: String): Observable<SearchStreamTwit> = api.statusesFilter(keyword)
        .map { it.source() }
        .flatMap { stringEvents(it) }
        .filter { it.isNotBlank() }
        .doOnNext { Timber.d("tweet data: $it") }
        .map { json -> gson.fromJson(json, SearchStreamTwitResponse::class.java) }
        .map { response ->
            SearchStreamTwit(
                response.id,
                response.extendedTweet?.text ?: response.text,
                response.timestampEpochMillis.toLongOrDefault(System.currentTimeMillis()),
                response.user.name,
                response.user.screenName,
                response.user.description ?: "",
                response.user.profileImageUrl
            )
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}