package vlad.chetrari.bvtesttask.data.network.model.response

import com.google.gson.annotations.SerializedName

data class BearerTokenResponse(
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("access_token")
    val accessToken: String
)