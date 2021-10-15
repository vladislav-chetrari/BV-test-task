package vlad.chetrari.bvtesttask.data.model.interchange.response

data class SearchStreamV2TwitResponse(
    val data: Data
) {
    data class Data(
        val id: String,
        val text: String
    )
}