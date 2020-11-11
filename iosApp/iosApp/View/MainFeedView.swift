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
            .padding(.horizontal, 0)
            .navigationBarTitle("RSS Feed")
            .navigationBarTitleDisplayMode(.large)
            .navigationBarItems(trailing: Button(action: { print("tapped") }) {
                Image(systemName: "pencil.circle").imageScale(.large)
            })
            .listStyle(PlainListStyle())
            .pullToRefresh(isShowing: $viewModel.loading) {
                self.viewModel.loadFeed(forceReload: true)
            }
        }
    }
}

extension MainFeedView {
    
    class ViewModel: ObservableObject {
        let store: FeedStore
        
        @Published var loading = false
        @Published var items: [Post] = []

        init(store: FeedStore) {
            self.store = store
            
            store.watchState().watch { [weak self] state in
                guard let state = state  else {
                    // TODO: handle error
                    return
                }
                self?.loading = state.progress
                self?.items = state.feeds.flatMap{ $0.posts }.sorted(by: { $0.date > $1.date })
                
            }
            
            loadFeed(forceReload: true)
        }
        
        func loadFeed(forceReload: Bool) {
            DispatchQueue.main.async { [self] in
                self.store.dispatch(action: FeedAction.Refresh(forceLoad: true))
            }
        }
    
    }
}

extension Post: Identifiable { }
