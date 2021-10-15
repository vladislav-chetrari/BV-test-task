package vlad.chetrari.bvtesttask.data.network.client

import vlad.chetrari.bvtesttask.data.model.interchange.StreamSearchRuleDto
import vlad.chetrari.bvtesttask.data.model.interchange.request.AddSearchStreamRulesRequest
import vlad.chetrari.bvtesttask.data.model.interchange.request.DeleteSearchRulesRequest
import vlad.chetrari.bvtesttask.data.network.api.TwitterV2StreamSetupApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwitterV2SearchSetupClient @Inject constructor(
    private val api: TwitterV2StreamSetupApi
) {

    suspend fun setupSearch(bearerToken: String, keyword: String): Boolean {
        val authorization = "Bearer $bearerToken"
        val rules = api.getRules(authorization)
        val oldRulesIdsToRemove = rules.data.mapNotNull(StreamSearchRuleDto::id)
        val newSearchRulesRequest = AddSearchStreamRulesRequest(listOf(StreamSearchRuleDto(value = keyword)))
        if (oldRulesIdsToRemove.isNotEmpty()) {
            val deleteSearchRulesRequest = DeleteSearchRulesRequest(DeleteSearchRulesRequest.Ids(oldRulesIdsToRemove))
            api.deleteRules(authorization, deleteSearchRulesRequest)
        }
        val rulesAppliedResponse = api.addRules(authorization, newSearchRulesRequest)
        return rulesAppliedResponse.data.isNotEmpty()
    }
}