package vlad.chetrari.bvtesttask.data.model.response

import com.google.gson.annotations.SerializedName

data class TwitterStatusResponse(
    val id: Long,
    val text: String,
    val user: User,
    val extendedTweet: ExtendedTweet?,
    @SerializedName("timestamp_ms")
    val timestampEpochMillis: String
) {

    data class User(
        val name: String,
        @SerializedName("screen_name")
        val screenName: String,
        val description: String? = null,
        @SerializedName("profile_image_url_https")
        val profileImageUrl: String? = null
    )

    data class ExtendedTweet(
        @SerializedName("full_text")
        val text: String
    )
}