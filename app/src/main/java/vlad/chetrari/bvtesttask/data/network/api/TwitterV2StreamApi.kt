package vlad.chetrari.bvtesttask.data.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming

interface TwitterV2StreamApi {

    @GET("2/tweets/search/stream")
    @Streaming
    fun search(
        @Header("Authorization") authorizationHeader: String
    ): Observable<ResponseBody>
}