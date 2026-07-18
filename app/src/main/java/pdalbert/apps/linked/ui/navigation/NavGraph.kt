package pdalbert.apps.linked.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pdalbert.apps.linked.ui.screens.home.AllFoldersScreen
import pdalbert.apps.linked.ui.screens.home.AllLinksScreen
import pdalbert.apps.linked.ui.screens.home.HomeScreen
import pdalbert.apps.linked.ui.screens.login.LoginScreen
import pdalbert.apps.linked.ui.screens.splash.SplashScreen
import pdalbert.apps.linked.viewmodel.AllFoldersViewModel
import pdalbert.apps.linked.viewmodel.AllLinksViewModel
import pdalbert.apps.linked.viewmodel.HomeViewModel
import pdalbert.apps.linked.viewmodel.LoginViewModel
import pdalbert.apps.linked.viewmodel.SplashViewModel

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController = navController, viewModel = splashViewModel)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()

            LaunchedEffect(loginViewModel.navigationEvent) {
                loginViewModel.navigationEvent.collect { destination ->
                    destination?.let {
                        navController.navigate(it) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            }

            LoginScreen(navController = navController, viewModel = loginViewModel)
        }

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(Screen.Settings.route) {
            PlaceholderScreen("Settings")
        }

        composable(Screen.AllLinks.route) {
            val allLinksViewModel: AllLinksViewModel = hiltViewModel()
            AllLinksScreen(navController = navController, viewModel = allLinksViewModel)
        }

        composable(Screen.AllFolders.route) {
            val allFoldersViewModel: AllFoldersViewModel = hiltViewModel()
            AllFoldersScreen(navController = navController, viewModel = allFoldersViewModel)
        }

        composable(
            route = Screen.LinkDetail.route,
            arguments = listOf(
                navArgument("linkId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val linkId = backStackEntry.arguments?.getString("linkId") ?: ""
            PlaceholderScreen("Link Detail: $linkId")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name, fontSize = 24.sp)
    }
}
