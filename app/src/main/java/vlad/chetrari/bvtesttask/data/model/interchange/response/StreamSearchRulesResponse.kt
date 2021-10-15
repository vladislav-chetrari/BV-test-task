package vlad.chetrari.bvtesttask.data.model.interchange.response

import vlad.chetrari.bvtesttask.data.model.interchange.StreamSearchRuleDto

data class StreamSearchRulesResponse(
    val data: List<StreamSearchRuleDto> = emptyList()
)
