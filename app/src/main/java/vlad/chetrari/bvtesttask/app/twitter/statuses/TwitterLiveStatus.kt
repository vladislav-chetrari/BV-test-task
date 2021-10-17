package vlad.chetrari.bvtesttask.app.twitter.statuses

import vlad.chetrari.bvtesttask.data.model.ui.TwitterStatus

class TwitterLiveStatus(
    val status: TwitterStatus,
    val timeToLiveSeconds: Int
) {

    var onCountdownTick: ((Int) -> Unit)? = null
    var lifespanCountdownSeconds = timeToLiveSeconds
        set(value) {
            field = value
            onCountdownTick?.invoke(value)
        }
}