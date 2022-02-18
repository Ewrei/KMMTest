package robin.morty.kmm.ui.episodes

import android.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import robin.morty.kmm.fragment.EpisodeDetailModel
import robin.morty.kmm.ui.main.MainViewModel

@Composable
fun EpisodesListView(
    viewModel: MainViewModel,
    bottomBar: @Composable () -> Unit,
    episodeSelected: (episode: EpisodeDetailModel) -> Unit
) {
    val lazyEpisodeList = viewModel.episodes.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Episodes") },
                backgroundColor = colorResource(id = R.color.holo_orange_dark),
                contentColor = colorResource(id = R.color.white)
            )
        },
        bottomBar = bottomBar
    )
    {
        LazyColumn(contentPadding = it) {
            items(lazyEpisodeList) { episode ->
                episode?.let {
                    EpisodesListRowView(episode, episodeSelected)
                }
            }
        }
    }
}

