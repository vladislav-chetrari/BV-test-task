package vlad.chetrari.bvtesttask

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import vlad.chetrari.bvtesttask.app.base.AppDispatchers

class TestAppDispatchers : AppDispatchers {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val io: CoroutineDispatcher
        get() = Dispatchers.Main
    override val computation: CoroutineDispatcher
        get() = Dispatchers.Main
}