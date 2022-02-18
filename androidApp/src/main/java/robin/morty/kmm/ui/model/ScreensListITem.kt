package robin.morty.kmm.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tv
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreensListITem(val route: String, val label: String, val icon: ImageVector? = null) {
    object CharactersScreen : ScreensListITem("Characters", "Characters", Icons.Default.Person)
    object EpisodesScreen : ScreensListITem("Episodes", "Episodes", Icons.Default.Tv)
    object CharacterDetailsScreen : ScreensListITem("CharacterDetails", "CharacterDetails")
    object EpisodeDetailsScreen : ScreensListITem("EpisodeDetails", "EpisodeDetails")
}