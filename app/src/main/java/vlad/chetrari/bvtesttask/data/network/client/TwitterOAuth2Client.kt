package vlad.chetrari.bvtesttask.data.network.client

import vlad.chetrari.bvtesttask.data.model.ui.Bearer
import vlad.chetrari.bvtesttask.data.network.api.TwitterOAuth2Api
import vlad.chetrari.bvtesttask.di.Twitter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterOAuth2Client @Inject constructor(
    private val api: TwitterOAuth2Api,
    @Twitter.OAuth.GrantType
    private val grantType: String
) {

    suspend fun authenticate(): Bearer {
        val response = api.authenticate(grantType)
        return Bearer(response.accessToken)
    }
}