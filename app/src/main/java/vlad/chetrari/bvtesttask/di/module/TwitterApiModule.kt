package vlad.chetrari.bvtesttask.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import vlad.chetrari.bvtesttask.data.network.api.TwitterStreamApi
import vlad.chetrari.bvtesttask.data.network.interceptor.TwitterOAuthInterceptor
import vlad.chetrari.bvtesttask.di.Twitter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TwitterApiModule {

    @Provides
    @Singleton
    fun twitterStreamSearchV1Api(
        @Twitter.BaseUrl baseUrl: String,
        interceptor: TwitterOAuthInterceptor
    ): TwitterStreamApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TwitterStreamApi::class.java)
}