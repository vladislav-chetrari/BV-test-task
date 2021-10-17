package vlad.chetrari.bvtesttask.data.network.client

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.internal.toLongOrDefault
import okio.BufferedSource
import timber.log.Timber
import vlad.chetrari.bvtesttask.data.model.response.TwitterStatusResponse
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus
import vlad.chetrari.bvtesttask.data.network.api.TwitterStreamApi
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterStreamClient @Inject constructor(
    private val gson: Gson,
    private val api: TwitterStreamApi
) {

    fun search(keyword: String): Observable<TwitterStatus> = api.statusesFilter(keyword)
        .map { it.source() }
        .flatMap { stringEvents(it) }
        .filter { it.isNotBlank() }
        .doOnNext { Timber.d("status data: $it") }
        .map { json -> gson.fromJson(json, TwitterStatusResponse::class.java) }
        .map { response ->
            TwitterStatus(
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

    private fun stringEvents(source: BufferedSource): Observable<String> = Observable.create { emitter ->
        val safeEmitter: () -> ObservableEmitter<String>? = { if (emitter.isDisposed) null else emitter }
        try {
            while (!source.exhausted()) {
                safeEmitter()?.onNext(source.readUtf8Line() ?: "")
            }
        } catch (e: IOException) {
            safeEmitter()?.onError(e)
        } finally {
            safeEmitter()?.onComplete()
        }
    }
}