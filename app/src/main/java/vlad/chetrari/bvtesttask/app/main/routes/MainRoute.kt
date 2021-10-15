package vlad.chetrari.bvtesttask.app.main.routes

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import vlad.chetrari.bvtesttask.R

enum class MainRoute(
    @StringRes val titleResId: Int,
    @StringRes val subtitleResId: Int,
    @ColorRes val backgroundColorResId: Int
) {
    API_V1(R.string.main_route_api_v1_title, R.string.main_route_api_v1_subtitle, R.color.purple_200),
    API_V2(R.string.main_route_api_v2_title, R.string.main_route_api_v2_subtitle, R.color.teal_200)
}