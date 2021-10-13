package vlad.chetrari.bvtesttask.data.network.client

import vlad.chetrari.bvtesttask.data.network.api.TwitterOAuthApi
import vlad.chetrari.bvtesttask.data.network.model.response.BearerTokenResponse
import vlad.chetrari.bvtesttask.di.Twitter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterOAuthClient @Inject constructor(
    private val api: TwitterOAuthApi,
    @Twitter.OAuth.GrantType
    private val grantType: String
) {

    suspend fun authenticate(): BearerTokenResponse = api.authenticate(grantType)
}