package vlad.chetrari.bvtesttask.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.BINARY

@Qualifier
@Retention(BINARY)
annotation class Twitter {

    @Qualifier
    @Retention(BINARY)
    annotation class BaseUrl

    @Qualifier
    @Retention(BINARY)
    annotation class OAuth {

        @Qualifier
        @Retention(BINARY)
        annotation class Key

        @Qualifier
        @Retention(BINARY)
        annotation class KeySecret

        @Qualifier
        @Retention(BINARY)
        annotation class Client

        @Qualifier
        @Retention(BINARY)
        annotation class GrantType
    }
}