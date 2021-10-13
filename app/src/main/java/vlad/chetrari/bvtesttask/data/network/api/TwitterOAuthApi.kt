package vlad.chetrari.bvtesttask.data.network.api

import retrofit2.http.POST
import retrofit2.http.Query
import vlad.chetrari.bvtesttask.data.network.model.response.BearerTokenResponse

interface TwitterOAuthApi {

    @POST("oauth2/token")
    suspend fun authenticate(
        @Query("grant_type") grantType: String
    ): BearerTokenResponse
}