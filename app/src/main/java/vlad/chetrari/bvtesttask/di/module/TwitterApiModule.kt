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
    fun okHttpClient(
        builder: OkHttpClient.Builder,
        oAuthInterceptor: TwitterOAuthInterceptor
    ) = builder.addInterceptor(oAuthInterceptor).build()

    @Provides
    @Singleton
    fun twitterStreamApi(
        @Twitter.BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient
    ): TwitterStreamApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TwitterStreamApi::class.java)
}