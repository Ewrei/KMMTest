package robin.morty.kmm.ui.episodes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import robin.morty.kmm.shared.MortyRepository
import robin.morty.kmm.fragment.EpisodeDetailModel

class EpisodesDataSource(private val mortyRepository: MortyRepository) : PagingSource<Int, EpisodeDetailModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeDetailModel> {
        val pageNumber = params.key ?: 0

        val episodesResponse = mortyRepository.getEpisodes(pageNumber)
        val episodes = episodesResponse.results.mapNotNull { it?.episodeDetailModel }

        val prevKey = if (pageNumber > 0) pageNumber - 1 else null
        val nextKey = episodesResponse.info.next
        return LoadResult.Page(data = episodes, prevKey = prevKey, nextKey = nextKey)
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodeDetailModel>): Int? {
        return null
    }
}