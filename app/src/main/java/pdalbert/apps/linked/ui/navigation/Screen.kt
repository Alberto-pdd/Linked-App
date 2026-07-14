package pdalbert.apps.linked.ui.navigation

import java.util.UUID

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object Settings : Screen("settings")
    object AllLinks : Screen("all_links")
    object AllFolders : Screen("all_folders")
    object LinkDetail : Screen("link_detail/{linkId}") {
        fun createRoute(linkId: UUID) = "link_detail/$linkId"
    }
}
