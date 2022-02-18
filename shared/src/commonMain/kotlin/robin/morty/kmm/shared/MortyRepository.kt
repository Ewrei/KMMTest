package robin.morty.kmm.shared

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.kuuurt.paging.multiplatform.Pager
import com.kuuurt.paging.multiplatform.PagingConfig
import com.kuuurt.paging.multiplatform.PagingData
import com.kuuurt.paging.multiplatform.PagingResult
import com.kuuurt.paging.multiplatform.helpers.cachedIn
import kotlinx.coroutines.MainScope
import robin.morty.kmm.GetCharacterQuery
import robin.morty.kmm.GetCharactersQuery
import robin.morty.kmm.GetEpisodeQuery
import robin.morty.kmm.GetEpisodesQuery
import robin.morty.kmm.fragment.CharacterDetailModel
import robin.morty.kmm.fragment.EpisodeDetailModel
import robin.morty.kmm.shared.util.CommonFlow
import robin.morty.kmm.shared.util.asCommonFlow


class MortyRepository {
    private val scope = MainScope()

    // Creates a 10MB MemoryCacheFactory
    val cacheFactory = MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024)

    private val apolloClient = ApolloClient.Builder()
        .serverUrl("https://rickandmortyapi.com/graphql")
        .normalizedCache(cacheFactory)
        .build()

    suspend fun getCharacters(page: Int): GetCharactersQuery.Characters {
        val response = apolloClient.query(GetCharactersQuery(page)).execute()
        return response.dataAssertNoErrors.characters
    }

    suspend fun getCharacter(characterId: String): CharacterDetailModel {
        val response = apolloClient.query(GetCharacterQuery(characterId)).execute()
        return response.dataAssertNoErrors.character.characterDetailModel
    }

    suspend fun getEpisodes(page: Int): GetEpisodesQuery.Episodes {
        val response = apolloClient.query(GetEpisodesQuery(page)).execute()
        return response.dataAssertNoErrors.episodes
    }

    suspend fun getEpisode(episodeId: String): EpisodeDetailModel {
        val response = apolloClient.query(GetEpisodeQuery(episodeId)).execute()
        return response.dataAssertNoErrors.episode.episodeDetailModel
    }

    private val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    // also accessed from iOS
    val characterPager = Pager(clientScope = scope, config = pagingConfig, initialKey = 1,
        getItems = { currentKey, size ->
            val charactersResponse = getCharacters(currentKey)
            val items = charactersResponse.results.mapNotNull { it?.characterDetailModel }
            PagingResult(
                items = items,
                currentKey = currentKey,
                prevKey = { null },
                nextKey = { charactersResponse.info.next }
            )
        }
    )

    val characterPagingData: CommonFlow<PagingData<CharacterDetailModel>>
        get() = characterPager.pagingData
            .cachedIn(scope) // cachedIn from AndroidX Paging. on iOS, this is a no-op
            .asCommonFlow() // So that iOS can consume the Flow

}