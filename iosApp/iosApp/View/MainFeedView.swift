import SwiftUI
import RssReader

struct MainFeedView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    init(viewModel: ViewModel) {
        self.viewModel = viewModel
        UITableView.appearance().backgroundColor = .white
    }
    
    var body: some View {
        NavigationView {
            List(viewModel.items, rowContent: PostRow.init)
                .navigationBarTitle("RSS Feed")
                .navigationBarItems(trailing: NavigationLink(destination: viewModel.viewForFeeds()) {
                    Image(systemName: "pencil.circle").imageScale(.large)
                })
                .pullToRefresh(isShowing: $viewModel.loading) {
                    self.viewModel.loadFeed(forceReload: true)
                }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

extension MainFeedView {
    
    class ViewModel: ObservableObject {
        let store: FeedStore
        
        let viewForFeeds: () -> FeedsList
        
        @Published var loading = false
        @Published var items: [Post] = []
        
        init(store: FeedStore, viewForFeeds: @escaping () -> FeedsList ) {
            self.store = store
            self.viewForFeeds = viewForFeeds
            
            store.watchState().watch { [weak self] state in
                guard let state = state  else {
                    // TODO: handle error
                    return
                }
                self?.loading = state.progress
                self?.items = state.mainFeedPosts()
                
            }
            loadFeed(forceReload: false)
        }
        
        func loadFeed(forceReload: Bool) {
            store.dispatch(action: FeedAction.Refresh(forceLoad: forceReload))
        }
        
    }
}

extension Post: Identifiable { }
