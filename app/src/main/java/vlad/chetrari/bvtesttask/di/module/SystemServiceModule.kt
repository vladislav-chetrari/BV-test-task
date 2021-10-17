package vlad.chetrari.bvtesttask.di.module

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SystemServiceModule {


    @Provides
    @Singleton
    fun connectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = context.getSystemService()!!

}