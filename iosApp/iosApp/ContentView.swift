import SwiftUI
import RssReader

struct ContentView: View {
  @ObservedObject private(set) var viewModel: ViewModel
    
    init(viewModel: ViewModel) {
        self.viewModel = viewModel
        UITableView.appearance().backgroundColor = .white
    }

    var body: some View {
        NavigationView {
            List(viewModel.items, rowContent: RssRow.init)
                .padding(.horizontal, 0)
                .navigationBarTitle("RSS Feed")
                .navigationBarItems(trailing: Button("Reload") {
                    self.viewModel.loadFeed(forceReload: true)
                })
                .listStyle(PlainListStyle())
        }
    }
}

extension ContentView {
    
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
