package vlad.chetrari.bvtesttask.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun gson(): Gson = Gson()

    @Provides
    fun okHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)

    private companion object {
        const val REQUEST_TIMEOUT = 90L
    }
}