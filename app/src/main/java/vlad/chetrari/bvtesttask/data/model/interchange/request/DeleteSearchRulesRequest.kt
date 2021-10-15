package vlad.chetrari.bvtesttask.data.model.interchange.request

import com.google.gson.annotations.SerializedName

data class DeleteSearchRulesRequest(
    val delete: Ids
) {
    data class Ids(
        @SerializedName("ids")
        val list: List<String>
    )
}
