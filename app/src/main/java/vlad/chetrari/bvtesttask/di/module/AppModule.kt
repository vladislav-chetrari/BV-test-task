package vlad.chetrari.bvtesttask.di.module

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vlad.chetrari.bvtesttask.app.base.AppDispatchers
import vlad.chetrari.bvtesttask.app.base.DefaultAppDispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun resources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun dispatchers(): AppDispatchers = DefaultAppDispatchers()
}