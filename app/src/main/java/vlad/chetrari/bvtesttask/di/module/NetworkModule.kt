package vlad.chetrari.bvtesttask.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(Level.BODY)

    @Provides
    fun okHttpClientBuilder(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)

    @Provides
    fun retrofitBuilder(gsonConverterFactory: GsonConverterFactory): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(gsonConverterFactory)

    private companion object {
        const val REQUEST_TIMEOUT = 90L
    }
}