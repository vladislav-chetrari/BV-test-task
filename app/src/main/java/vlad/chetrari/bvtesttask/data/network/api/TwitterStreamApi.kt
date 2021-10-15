package vlad.chetrari.bvtesttask.data.network.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface TwitterStreamApi {

    @POST("statuses/filter.json")
    @Streaming
    fun statusesFilter(
        @Query("track") keyword: String
    ): Observable<ResponseBody>

}