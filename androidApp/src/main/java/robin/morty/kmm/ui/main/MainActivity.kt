package robin.morty.kmm.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import robin.morty.kmm.ui.characters.CharacterDetailView
import robin.morty.kmm.ui.characters.CharactersListView
import robin.morty.kmm.ui.episodes.EpisodeDetailView
import robin.morty.kmm.ui.episodes.EpisodesListView
import org.koin.androidx.compose.getViewModel
import robin.morty.kmm.ui.model.ScreensListITem

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MainLayout()
            }
        }
    }
}

@Composable
fun MainLayout() {
    val viewModel = getViewModel<MainViewModel>()

    val navController = rememberNavController()

    val bottomNavigationItems = listOf(ScreensListITem.CharactersScreen, ScreensListITem.EpisodesScreen)
    val bottomBar: @Composable () -> Unit =
        { MortyBottomNavigation(navController, bottomNavigationItems) }

    NavHost(navController, startDestination = ScreensListITem.CharactersScreen.route) {
        composable(ScreensListITem.CharactersScreen.route) {
            CharactersListView(viewModel, bottomBar) {
                navController.navigate(ScreensListITem.CharacterDetailsScreen.route + "/${it.id}")
            }
        }
        composable(ScreensListITem.CharacterDetailsScreen.route + "/{id}") { backStackEntry ->
            CharacterDetailView(
                viewModel,
                backStackEntry.arguments?.get("id") as String,
                popBack = { navController.popBackStack() })
        }
        composable(ScreensListITem.EpisodesScreen.route) {
            EpisodesListView(viewModel, bottomBar) {
                navController.navigate(ScreensListITem.EpisodeDetailsScreen.route + "/${it.id}")
            }
        }
        composable(ScreensListITem.EpisodeDetailsScreen.route + "/{id}") { backStackEntry ->
            EpisodeDetailView(
                viewModel,
                backStackEntry.arguments?.get("id") as String,
                popBack = { navController.popBackStack() })
        }
    }
}


@Composable
private fun MortyBottomNavigation(
    navController: NavHostController,
    items: List<ScreensListITem>
) {
    BottomNavigation(
        backgroundColor = colorResource(id= android.R.color.holo_orange_dark),
        contentColor = colorResource(id= android.R.color.white)
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    screen.icon?.let {
                        Icon(
                            screen.icon,
                            contentDescription = screen.label
                        )
                    }
                },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }

}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

