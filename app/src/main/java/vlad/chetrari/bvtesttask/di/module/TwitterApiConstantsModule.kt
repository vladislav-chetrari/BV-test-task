package vlad.chetrari.bvtesttask.di.module

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vlad.chetrari.bvtesttask.R
import vlad.chetrari.bvtesttask.di.Twitter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TwitterApiConstantsModule {

    @Provides
    @Singleton
    @Twitter.BaseUrl
    fun twitterStreamApiBaseUrl(res: Resources): String = res.getString(R.string.twitter_stream_api_base_url)

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
    @Twitter.OAuth.AccessToken
    fun twitterApiAccessToken(res: Resources): String = res.getString(R.string.twitter_api_access_token)

    @Provides
    @Singleton
    @Twitter.OAuth.AccessSecret
    fun twitterApiAccessSecret(res: Resources): String = res.getString(R.string.twitter_api_access_secret)

}