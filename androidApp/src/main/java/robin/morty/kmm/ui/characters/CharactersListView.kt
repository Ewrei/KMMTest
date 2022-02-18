package robin.morty.kmm.ui.characters

import android.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import robin.morty.kmm.fragment.CharacterDetailModel
import robin.morty.kmm.ui.main.MainViewModel


@Composable
fun CharactersListView(
    viewModel: MainViewModel,
    bottomBar: @Composable () -> Unit,
    characterSelected: (characterDetailModel: CharacterDetailModel) -> Unit
) {
    val lazyCharacterList = viewModel.characters.collectAsLazyPagingItems()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                backgroundColor = colorResource(id = R.color.holo_orange_dark),
                contentColor = colorResource(id = R.color.white)
            )
        },
        bottomBar = bottomBar
    )
    {
        LazyColumn(contentPadding = it) {
            items(lazyCharacterList) { character ->
                character?.let {
                    CharactersListRowView(character, characterSelected)
                }
            }
        }
    }
}
