package vlad.chetrari.bvtesttask.data.model.ui

data class TwitterStatus(
    val id: Long,
    val text: String,
    val timestampEpochMillis: Long,
    val userName: String,
    val userScreenName: String,
    val userDescription: String,
    val userProfileImageUrl: String? = null,
)