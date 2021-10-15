package vlad.chetrari.bvtesttask.data.network.client

import io.reactivex.Observable
import okio.BufferedSource
import java.io.IOException

internal fun stringEvents(source: BufferedSource): Observable<String> = Observable.create { emitter ->
    try {
        while (!source.exhausted()) {
            emitter.onNext(source.readUtf8Line() ?: "")
        }
    } catch (e: IOException) {
        emitter.onError(e)
    } finally {
        emitter.onComplete()
    }
}