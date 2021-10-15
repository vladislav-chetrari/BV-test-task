package vlad.chetrari.bvtesttask.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.BINARY

@Qualifier
@Retention(BINARY)
annotation class Twitter {

    @Qualifier
    @Retention(BINARY)
    annotation class BaseUrl

    annotation class OAuth {

        @Qualifier
        @Retention(BINARY)
        annotation class Key

        @Qualifier
        @Retention(BINARY)
        annotation class KeySecret

        @Qualifier
        @Retention(BINARY)
        annotation class AccessToken

        @Qualifier
        @Retention(BINARY)
        annotation class AccessSecret
    }
}