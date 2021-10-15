package vlad.chetrari.bvtesttask.data.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import vlad.chetrari.bvtesttask.data.model.interchange.request.AddSearchStreamRulesRequest
import vlad.chetrari.bvtesttask.data.model.interchange.request.DeleteSearchRulesRequest
import vlad.chetrari.bvtesttask.data.model.interchange.response.StreamSearchRulesDeleteResponse
import vlad.chetrari.bvtesttask.data.model.interchange.response.StreamSearchRulesResponse

interface TwitterV2StreamSetupApi {

    @GET("2/tweets/search/stream/rules")
    suspend fun getRules(
        @Header("Authorization") authorizationHeader: String
    ): StreamSearchRulesResponse

    @POST("2/tweets/search/stream/rules")
    suspend fun deleteRules(
        @Header("Authorization") authorizationHeader: String,
        @Body requestBody: DeleteSearchRulesRequest
    ): StreamSearchRulesDeleteResponse

    @POST("2/tweets/search/stream/rules")
    suspend fun addRules(
        @Header("Authorization") authorizationHeader: String,
        @Body requestBody: AddSearchStreamRulesRequest
    ): StreamSearchRulesResponse
}