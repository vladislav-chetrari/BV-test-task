package vlad.chetrari.bvtesttask.app.twitter.statuses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus
import java.util.*
import kotlin.coroutines.CoroutineContext

class TwitterLiveStatusesManager(
    private val context: CoroutineContext,
    private val statusLifespanSeconds: Int
) : CoroutineScope {

    private var job = Job()

    //TODO get rid of RX here
    private var disposable: Disposable? = null
    override val coroutineContext: CoroutineContext
        get() = context + job

    private val list = LinkedList<TwitterLiveStatus>()

    //use subject to avoid concurrent list changes
    private val operationSubject = PublishSubject.create<Operation>()
    val liveData: LiveData<List<TwitterLiveStatus>> = MutableLiveData()

    init {
        disposable = operationSubject.subscribe(::executeOperation)
    }

    fun add(status: TwitterStatus) {
        val liveStatus = TwitterLiveStatus(status, statusLifespanSeconds)
        operationSubject.onNext(Operation.Add(liveStatus))
        launch {
            liveStatus.launchTimer(::remove)
        }
    }

    fun clear() {
        job.cancel()
        operationSubject.onNext(Operation.Clear)
        job = Job()
    }

    fun close() {
        clear()
        disposable?.dispose()
    }

    private fun remove(liveStatus: TwitterLiveStatus) {
        operationSubject.onNext(Operation.Remove(liveStatus))
    }

    private fun executeOperation(operation: Operation) {
        when (operation) {
            is Operation.Add -> list.add(operation.liveStatus)
            is Operation.Remove -> list.remove(operation.liveStatus)
            Operation.Clear -> list.clear()
        }
        (liveData as MutableLiveData).value = list.toImmutableList()
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