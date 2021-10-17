package vlad.chetrari.bvtesttask.app.twitter.statuses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import okhttp3.internal.toImmutableList
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus
import java.util.*
import kotlin.coroutines.CoroutineContext

class TwitterLiveStatusesManager(
    private val parentCoroutineContext: CoroutineContext,
    private val statusLifespanSeconds: Int
) : CoroutineScope {

    //lifespan timers need to use separate job so we can cancel them separately from other coroutines
    private val timerJob = Job()

    //launches all coroutines on computation dispatcher by default
    override val coroutineContext: CoroutineContext
        get() = parentCoroutineContext + Dispatchers.Default

    //use shared flow to avoid concurrent list changes and backpressure
    private val operationFlow = MutableSharedFlow<Operation>()
    private val list = LinkedList<TwitterLiveStatus>()
    val liveData: LiveData<List<TwitterLiveStatus>> = MutableLiveData()

    init {
        launch {
            operationFlow.collect { operation ->
                when (operation) {
                    is Operation.Add -> list.addFirst(operation.liveStatus)
                    is Operation.Remove -> list.remove(operation.liveStatus)
                    Operation.Clear -> list.clear()
                }
                withContext(Dispatchers.Main) {
                    (liveData as MutableLiveData).value = list.toImmutableList()
                }
            }
        }
    }

    fun add(status: TwitterStatus) {
        launch {
            val liveStatus = TwitterLiveStatus(status, statusLifespanSeconds)
            operationFlow.emit(Operation.Add(liveStatus))
            launch(timerJob) {
                liveStatus.launchTimer(::remove)
            }
        }
    }

    fun clear() {
        launch {
            operationFlow.emit(Operation.Clear)
            timerJob.cancelChildren()
        }
    }

    private fun remove(liveStatus: TwitterLiveStatus) {
        launch {
            operationFlow.emit(Operation.Remove(liveStatus))
        }
    }

    private suspend fun TwitterLiveStatus.launchTimer(onDeath: (TwitterLiveStatus) -> Unit) {
        while (lifespanCountdownSeconds > 0) {
            delay(1000L)
            lifespanCountdownSeconds -= 1
        }
        if (lifespanCountdownSeconds <= 0) {
            onDeath(this)
        }
    }

    private sealed class Operation {
        class Add(val liveStatus: TwitterLiveStatus) : Operation()
        class Remove(val liveStatus: TwitterLiveStatus) : Operation()
        object Clear : Operation()
    }
}