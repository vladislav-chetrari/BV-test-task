package vlad.chetrari.bvtesttask.data.network

import okhttp3.Interceptor
import okhttp3.Response
import vlad.chetrari.bvtesttask.data.util.Base64Encoder
import vlad.chetrari.bvtesttask.di.Twitter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterOAuth2Interceptor @Inject constructor(
    @Twitter.OAuth.Key key: String,
    @Twitter.OAuth.KeySecret keySecret: String,
    private val encoder: Base64Encoder
) : Interceptor {

    private val credentialsPair = "$key:$keySecret"

    override fun intercept(chain: Interceptor.Chain): Response {
        val basicAuth = "Basic ${encoder.encode(credentialsPair)}"
        val request = chain.request().newBuilder()
            .addHeader("Authorization", basicAuth)
            .build()
        return chain.proceed(request)
    }
}