package robin.morty.kmm.ui.episodes

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import robin.morty.kmm.fragment.EpisodeDetailModel
import robin.morty.kmm.ui.main.MainViewModel


@Composable
fun EpisodeDetailView(viewModel: MainViewModel, episodeId: String, popBack: () -> Unit) {
    val (episode, setEpisode) = remember { mutableStateOf<EpisodeDetailModel?>(null) }

    LaunchedEffect(episodeId) {
        setEpisode(viewModel.getEpisode(episodeId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = episode?.name ?: "") },
                contentColor = colorResource(id = R.color.white),
                backgroundColor = colorResource(id = R.color.holo_orange_dark),
                navigationIcon = {
                    IconButton(onClick = { popBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        })
    {
        Surface(color = Color.LightGray) {

            LazyColumn {

                episode?.let {

                    item(episode.id) {

                        Text(
                            "Characters",
                            style = MaterialTheme.typography.h5,
                            color = LocalContentColor.current,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )

                        Surface(color = Color.White) {
                            EpisodeCharactersList(episode)
                        }

                    }
                }
            }
        }
    }

}

@Composable
private fun EpisodeCharactersList(episode: EpisodeDetailModel) {

    Column {
        episode.characters.filterNotNull().forEach { character ->
            Row(modifier = Modifier.padding(vertical = 8.dp)) {

                Surface(
                    modifier = Modifier.size(28.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                ) {
                    Image(
                        painter = rememberImagePainter(character.image),
                        modifier = Modifier.size(28.dp),
                        contentDescription = character.name
                    )
                }

                Text(
                    character.name,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.h6
                )
            }
            Divider()
        }
    }
}

