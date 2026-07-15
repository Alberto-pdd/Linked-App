package pdalbert.apps.linked.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pdalbert.apps.linked.data.local.SessionManager
import pdalbert.apps.linked.data.repository.FolderRepository
import pdalbert.apps.linked.data.repository.LinkRepository
import pdalbert.apps.linked.ui.screens.home.AllFoldersScreen
import pdalbert.apps.linked.ui.screens.home.AllLinksScreen
import pdalbert.apps.linked.ui.screens.home.HomeScreen
import pdalbert.apps.linked.ui.screens.login.LoginScreen
import pdalbert.apps.linked.ui.screens.splash.SplashScreen
import pdalbert.apps.linked.viewmodel.AllFoldersViewModel
import pdalbert.apps.linked.viewmodel.AllFoldersViewModelFactory
import pdalbert.apps.linked.viewmodel.AllLinksViewModel
import pdalbert.apps.linked.viewmodel.AllLinksViewModelFactory
import pdalbert.apps.linked.viewmodel.HomeViewModel
import pdalbert.apps.linked.viewmodel.HomeViewModelFactory
import pdalbert.apps.linked.viewmodel.LoginViewModel
import pdalbert.apps.linked.viewmodel.LoginViewModelFactory
import pdalbert.apps.linked.viewmodel.SplashViewModel
import pdalbert.apps.linked.viewmodel.SplashViewModelFactory

@Composable
fun NavGraph(
    navController: NavHostController,
    sessionManager: SessionManager,
    linkRepository: LinkRepository,
    folderRepository: FolderRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = viewModel(
                factory = SplashViewModelFactory(sessionManager)
            )
            SplashScreen(navController = navController, viewModel = splashViewModel)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(sessionManager)
            )

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
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(sessionManager, linkRepository, folderRepository)
            )
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(Screen.Settings.route) {
            PlaceholderScreen("Settings")
        }

        composable(Screen.AllLinks.route) {
            val allLinksViewModel: AllLinksViewModel = viewModel(
                factory = AllLinksViewModelFactory(linkRepository)
            )
            AllLinksScreen(navController = navController, viewModel = allLinksViewModel)
        }

        composable(Screen.AllFolders.route) {
            val allFoldersViewModel: AllFoldersViewModel = viewModel(
                factory = AllFoldersViewModelFactory(folderRepository)
            )
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
