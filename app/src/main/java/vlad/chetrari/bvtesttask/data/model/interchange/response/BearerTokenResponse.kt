package vlad.chetrari.bvtesttask.data.model.interchange.response

import com.google.gson.annotations.SerializedName

data class BearerTokenResponse(
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("access_token")
    val accessToken: String
)