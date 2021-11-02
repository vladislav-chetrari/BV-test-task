package vlad.chetrari.bvtesttask.app.twitter.statuses

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import vlad.chetrari.bvtesttask.TestAppDispatchers
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus

@ExperimentalCoroutinesApi
class TwitterLiveStatusesManagerTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val appDispatchers = TestAppDispatchers()

    @MockK(relaxed = true)
    lateinit var listObserver: Observer<List<TwitterLiveStatus>>

    @MockK(relaxed = true)
    lateinit var twitterStatus: TwitterStatus

    private lateinit var manager: TwitterLiveStatusesManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testCoroutineDispatcher)
        manager = TwitterLiveStatusesManager(appDispatchers, appDispatchers.main, 3)
        manager.liveData.observeForever(listObserver)
    }

    @After
    fun tearDown() {
        manager.liveData.removeObserver(listObserver)
        Dispatchers.resetMain()
    }

    @Test
    fun add_shouldAddLiveStatusToListLiveData() = runBlockingTest(testCoroutineDispatcher) {
        manager.add(twitterStatus)

//        verify status added
        val slot = slot<List<TwitterLiveStatus>>()
        verify { listObserver.onChanged(capture(slot)) }
        assertTrue(slot.isCaptured)
        val captList = slot.captured
        assertEquals(1, captList.size)
        val liveStatus = captList[0]
        assertEquals(twitterStatus, liveStatus.status)

//        verify lifespan countdown of twitter live status
        assertEquals(3, liveStatus.lifespanCountdownSeconds)
        advanceTimeBy(1_000)
        assertEquals(2, liveStatus.lifespanCountdownSeconds)
        advanceTimeBy(1_000)
        assertEquals(1, liveStatus.lifespanCountdownSeconds)
        advanceTimeBy(1_000)

//        verify auto-removal after timer ends
        verify { listObserver.onChanged(match { it.isEmpty() }) }
    }

    @Test
    fun onNetworkStateChange_shouldPauseTimersOnDisconnectAndResumeOnConnect() = runBlockingTest(testCoroutineDispatcher) {
        manager.add(twitterStatus)
        clearMocks(listObserver)
        advanceTimeBy(500)
        manager.add(twitterStatus)
        advanceTimeBy(500)
        val slot = slot<List<TwitterLiveStatus>>()
        verify { listObserver.onChanged(capture(slot)) }
        assertTrue(slot.isCaptured)
        val captList = slot.captured
        assertEquals(2, captList.size)
        val liveStatus0 = captList[0]
        val liveStatus1 = captList[1]
        assertEquals(3, liveStatus0.lifespanCountdownSeconds)
        assertEquals(2, liveStatus1.lifespanCountdownSeconds)

//        verify timers paused on disconnect
        manager.onNetworkStateChange(false)
        advanceTimeBy(50_000)
        assertEquals(3, liveStatus0.lifespanCountdownSeconds)
        assertEquals(2, liveStatus1.lifespanCountdownSeconds)

//        verify timers resume on connection established
        manager.onNetworkStateChange(true)
        advanceTimeBy(1_000)
        assertEquals(2, liveStatus0.lifespanCountdownSeconds)
        assertEquals(1, liveStatus1.lifespanCountdownSeconds)
    }

    @Test
    fun clear_shouldClearListOfLiveData() = runBlockingTest(testCoroutineDispatcher) {
        manager.add(twitterStatus)
        manager.add(twitterStatus)
        clearMocks(listObserver)

        manager.clear()
        verify { listObserver.onChanged(match { it.isEmpty() }) }
    }
}