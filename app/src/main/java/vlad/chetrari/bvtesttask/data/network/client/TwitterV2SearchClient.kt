package vlad.chetrari.bvtesttask.data.network.client

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import timber.log.Timber
import vlad.chetrari.bvtesttask.data.model.interchange.response.SearchStreamV2TwitResponse
import vlad.chetrari.bvtesttask.data.model.ui.Twit
import vlad.chetrari.bvtesttask.data.network.api.TwitterV2StreamApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TwitterV2SearchClient @Inject constructor(
    private val api: TwitterV2StreamApi,
    private val gson: Gson
) {

    fun search(bearerToken: String): Observable<Twit> = api.search("Bearer $bearerToken")
        .map(ResponseBody::source)
        .flatMap { stringEvents(it) }
        .filter(String::isNotBlank)
        .map { json -> gson.fromJson(json, SearchStreamV2TwitResponse::class.java) }
        .map(SearchStreamV2TwitResponse::data)
        .map { Twit(it.id, it.text) }
        .doOnNext { twit -> Timber.d("twit received, $twit") }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}