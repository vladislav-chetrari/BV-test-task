package vlad.chetrari.bvtesttask.di.module

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.data.network.TwitterOAuth2Interceptor
import vlad.chetrari.bvtesttask.data.network.api.TwitterOAuth2Api
import vlad.chetrari.bvtesttask.data.network.api.TwitterV2StreamApi
import vlad.chetrari.bvtesttask.data.network.api.TwitterV2StreamSetupApi
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
    fun twitterOAuthClient(builder: OkHttpClient.Builder, interceptor: TwitterOAuth2Interceptor): OkHttpClient = builder
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    @Twitter.StreamSearch.Client
    fun twitterStreamSearchClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

    @Provides
    @Singleton
    fun twitterOAuthApi(
        builder: Retrofit.Builder,
        @Twitter.BaseUrl baseUrl: String,
        @Twitter.OAuth.Client client: OkHttpClient
    ): TwitterOAuth2Api = builder
        .baseUrl(baseUrl)
        .client(client)
        .build()
        .create(TwitterOAuth2Api::class.java)

    @Provides
    @Singleton
    fun twitterStreamSearchSetupApi(
        builder: Retrofit.Builder,
        @Twitter.BaseUrl baseUrl: String,
        @Twitter.StreamSearch.Client client: OkHttpClient
    ): TwitterV2StreamSetupApi = builder
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TwitterV2StreamSetupApi::class.java)

    @Provides
    @Singleton
    fun twitterStreamSearchApi(
        @Twitter.BaseUrl baseUrl: String,
    ): TwitterV2StreamApi {
        val okHttp = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TwitterV2StreamApi::class.java)
    }
}