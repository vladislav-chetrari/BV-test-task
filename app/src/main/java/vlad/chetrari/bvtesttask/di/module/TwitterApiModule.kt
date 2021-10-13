package vlad.chetrari.bvtesttask.di.module

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.data.network.TwitterOAuthInterceptor
import vlad.chetrari.bvtesttask.data.network.api.TwitterOAuthApi
import vlad.chetrari.bvtesttask.di.Twitter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TwitterApiModule {

    @Provides
    @Singleton
    @Twitter.BaseUrl
    fun twitterApiBaseUrl(res: Resources): String = res.getString(R.string.twitter_api_base_url)

    @Provides
    @Singleton
    @Twitter.OAuth.Key
    fun twitterApiKey(res: Resources): String = res.getString(R.string.twitter_api_key)

    @Provides
    @Singleton
    @Twitter.OAuth.KeySecret
    fun twitterApiKeySecret(res: Resources): String = res.getString(R.string.twitter_api_key_secret)

    @Provides
    @Singleton
    @Twitter.OAuth.GrantType
    fun twitterApiGrantType(res: Resources): String = res.getString(R.string.twitter_api_grant_type)

    @Provides
    @Singleton
    @Twitter.OAuth.Client
    fun twitterApiClient(builder: OkHttpClient.Builder, interceptor: TwitterOAuthInterceptor): OkHttpClient = builder
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun twitterOAuthApi(
        builder: Retrofit.Builder,
        @Twitter.BaseUrl baseUrl: String,
        @Twitter.OAuth.Client client: OkHttpClient
    ): TwitterOAuthApi = builder
        .baseUrl(baseUrl)
        .client(client)
        .build()
        .create(TwitterOAuthApi::class.java)

}