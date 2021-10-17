package vlad.chetrari.bvtesttask.app.twitter.statuses

import timber.log.Timber
import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus

class TwitterLiveStatus(
    val status: TwitterStatus,
    val timeToLiveSeconds: Int
) {

    var onCountdownTick: ((Int) -> Unit)? = null
    var lifespanCountdownSeconds = timeToLiveSeconds
        set(value) {
            field = value
            Timber.v("status[${status.id}].lifespanCountdownSeconds = , $value")
            onCountdownTick?.invoke(value)
        }
}