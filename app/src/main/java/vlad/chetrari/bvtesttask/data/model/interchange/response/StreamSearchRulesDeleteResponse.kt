package vlad.chetrari.bvtesttask.data.model.interchange.response

import com.google.gson.annotations.SerializedName

data class StreamSearchRulesDeleteResponse(
    @SerializedName("meta")
    val metadata: Metadata
) {
    data class Metadata(
        val summary: Summary
    ) {
        data class Summary(
            val rulesDeletedCount: Int,
            val rulesNotDeletedCount: Int
        )
    }
}
