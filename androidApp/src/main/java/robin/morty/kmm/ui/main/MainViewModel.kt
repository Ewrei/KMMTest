package robin.morty.kmm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import robin.morty.kmm.fragment.CharacterDetailModel
import robin.morty.kmm.fragment.EpisodeDetailModel
import robin.morty.kmm.shared.MortyRepository
import robin.morty.kmm.ui.episodes.EpisodesDataSource


class MainViewModel(private val mortyRepository: MortyRepository) : ViewModel() {

    // currently only using MultiplatformPaging library for character data
    val characters = mortyRepository.characterPagingData

    // continuing to use androidx paging library directly (as constrast) for
    val episodes: Flow<PagingData<EpisodeDetailModel>> = Pager(PagingConfig(pageSize = 20)) {
        EpisodesDataSource(mortyRepository)
    }.flow.cachedIn(viewModelScope)


    suspend fun getCharacter(characterId: String): CharacterDetailModel = mortyRepository.getCharacter(characterId)

    suspend fun getEpisode(episodeId: String): EpisodeDetailModel = mortyRepository.getEpisode(episodeId)

}