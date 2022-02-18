import Foundation
import SwiftUI
import shared

class EpisodeListViewModel: ObservableObject {
    @Published public var episodes: [EpisodeDetailModel] = []
    let repository = MortyRepository()
    var hasNextPage: Bool = false
    
    func fetchEpisodes() {
        repository.episodePagingData.watch { nullablePagingData in
            guard let list = nullablePagingData?.compactMap({ $0 as? EpisodeDetailModel }) else {
                return
            }
            
            self.episodes = list
            self.hasNextPage = self.repository.episodePager.hasNextPage
        }
    }
    
    
    func fetchNextData() {
        repository.episodePager.loadNext()
    }
    
    public var shouldDisplayNextPage: Bool {
        return hasNextPage
    }
}

