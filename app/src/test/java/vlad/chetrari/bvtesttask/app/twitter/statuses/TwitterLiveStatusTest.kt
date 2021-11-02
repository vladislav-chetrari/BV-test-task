package vlad.chetrari.bvtesttask.app.twitter.statuses

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus

class TwitterLiveStatusTest {

    @MockK(relaxed = true)
    lateinit var status: TwitterStatus

    private lateinit var liveStatus: TwitterLiveStatus

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        liveStatus = TwitterLiveStatus(status, 5)
    }

    @Test
    fun defaultValues() {
        assertEquals(liveStatus.status, status)
        assertEquals(liveStatus.timeToLiveSeconds, 5)
        assertNull(liveStatus.onCountdownTick)
        assertEquals(liveStatus.lifespanCountdownSeconds, 5)
    }

    @Test
    fun lifespanCountdownSeconds_set_whenOnCountdownTickNull_thenSetsValue() {
        liveStatus.lifespanCountdownSeconds = 16

        assertEquals(liveStatus.lifespanCountdownSeconds, 16)
    }

    @Test
    fun lifespanCountdownSeconds_set_whenOnCountdownTickNotNull_thenSetsValueAndInvokesOnCountdownTick() {
        val intConsumer = mockk<(Int) -> Unit>(relaxed = true)
        liveStatus.onCountdownTick = intConsumer

        liveStatus.lifespanCountdownSeconds = 16

        assertEquals(liveStatus.lifespanCountdownSeconds, 16)
        verify { intConsumer.invoke(16) }
    }
}