package vlad.chetrari.bvtesttask.data.model.ui

data class Twit(
    val id: String,
    val text: String,
    val creationTimestampEpochMillis: Long = System.currentTimeMillis()
)