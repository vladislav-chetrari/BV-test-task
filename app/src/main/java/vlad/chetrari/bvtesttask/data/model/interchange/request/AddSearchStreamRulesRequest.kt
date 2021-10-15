package vlad.chetrari.bvtesttask.data.model.interchange.request

import com.google.gson.annotations.SerializedName
import vlad.chetrari.bvtesttask.data.model.interchange.StreamSearchRuleDto

data class AddSearchStreamRulesRequest(
    @SerializedName("add")
    val rules: List<StreamSearchRuleDto>
)
