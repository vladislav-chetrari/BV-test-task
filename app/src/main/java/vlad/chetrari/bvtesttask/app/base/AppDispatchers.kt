package vlad.chetrari.bvtesttask.app.base

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val computation: CoroutineDispatcher
}