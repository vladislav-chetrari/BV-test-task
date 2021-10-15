package vlad.chetrari.bvtesttask.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import vlad.chetrari.bvtesttask.data.network.api.TwitterOAuth2Api
import vlad.chetrari.bvtesttask.data.network.api.TwitterStreamApi
import vlad.chetrari.bvtesttask.data.network.api.TwitterV2StreamApi
import vlad.chetrari.bvtesttask.data.network.api.TwitterV2StreamSetupApi
import vlad.chetrari.bvtesttask.data.network.interceptor.TwitterOAuth2Interceptor
import vlad.chetrari.bvtesttask.data.network.interceptor.TwitterOAuthInterceptor
import vlad.chetrari.bvtesttask.di.Twitter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TwitterApiModule {

    @Provides
    @Singleton
    @Twitter.OAuth.Client
    fun twitterOAuthClient(
        builder: OkHttpClient.Builder,
        interceptor: TwitterOAuth2Interceptor
    ): OkHttpClient = builder
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    @Twitter.StreamSearch.Client
    fun twitterStreamSearchSetupClient(builder: OkHttpClient.Builder): OkHttpClient = builder.build()

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
    fun twitterStreamSearchApi(@Twitter.BaseUrl baseUrl: String): TwitterV2StreamApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TwitterV2StreamApi::class.java)

    @Provides
    @Singleton
    fun twitterStreamSearchV1Api(
        @Twitter.StreamBaseUrl baseUrl: String,
        interceptor: TwitterOAuthInterceptor
    ): TwitterStreamApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TwitterStreamApi::class.java)
}