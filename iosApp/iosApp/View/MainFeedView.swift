import SwiftUI
import RssReader
import URLImage

struct MainFeedView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    @SwiftUI.State private var showSelectFeed = false
    
    init(viewModel: ViewModel) {
        self.viewModel = viewModel
        UITableView.appearance().backgroundColor = .white
    }
    
    var body: some View{
        VStack {
            if showSelectFeed {
                feedPicker.transition(.move(edge: .top))
            }
            List(viewModel.items, rowContent: PostRow.init)
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(leading: refreshButton, trailing: editFeedLink)
        .toolbar {
            ToolbarItem(placement: .principal) {
                navigationTitle
            }
        }
        .onAppear {
            viewModel.loadFeed(forceReload: true)
        }
    }
    
    var refreshButtionAnimation: Animation {
        Animation.linear(duration: 0.8).repeatForever(autoreverses: false)
    }
    
    var navigationTitle: some View {
        VStack {
            HStack {
                Text("RSS Reader").font(.headline)
                Button(action: {
                    withAnimation { showSelectFeed.toggle() }
                }) {
                    Image(systemName: showSelectFeed ? "chevron.up" : "chevron.down").imageScale(.small)
                }
            }
            Text(viewModel.selectedFeedOption.title).font(.subheadline).lineLimit(1)
        }
    }
    
    var feedPicker: some View {
        let binding = Binding<FeedPickerOption>(
            get: { self.viewModel.selectedFeedOption },
            set: { self.viewModel.selectFeed(feedOption: $0) }
        )
        return Picker("", selection: binding) {
            ForEach(viewModel.feedOptions, id: \.self) { option in
                HStack {
                    if let imageUrl = option.feed?.imageUrl, let url = URL(string: imageUrl) {
                        
                        URLImage(url: url) { image in
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                        }
                        .frame(width: 24, height: 24)
                        .cornerRadius(12.0)
                        .clipped()
                    }
                    Text(option.title)
                }
            }
        }
        .background(Color("FeedPicker"))
        .pickerStyle(WheelPickerStyle())
    }
    
    var refreshButton: some View {
        Button(action: {
            viewModel.loadFeed(forceReload: true)
        }) {
            Image(systemName: "arrow.clockwise")
                .imageScale(.large)
                .rotationEffect(Angle.degrees(viewModel.loading ? 360 : 0)).animation( viewModel.loading ? refreshButtionAnimation : .default)
        }
    }
    
    var editFeedLink: some View {
        NavigationLink(destination: NavigationLazyView<FeedsList>(viewModel.viewForFeeds())) {
            Image(systemName: "pencil.circle").imageScale(.large)
        }
    }
    
}

extension MainFeedView {
    
    class ViewModel: ObservableObject {
        
        let store: FeedStore
        
        let viewForFeeds: () -> FeedsList
        
        @Published var loading = false
        @Published var items: [Post] = []
        @Published var feedOptions: [FeedPickerOption] = []
        @Published var selectedFeedOption = FeedPickerOption.all
        
        init(store: FeedStore, viewForFeeds: @escaping () -> FeedsList ) {
            self.store = store
            self.viewForFeeds = viewForFeeds
            
            store.watchState().watch { [weak self] state in
                self?.loading = state.progress
                self?.items = state.mainFeedPosts()
                self?.feedOptions = [.all] + state.feeds.map { FeedPickerOption.feed($0)}
                if let selectedFeed = state.selectedFeed {
                    self?.selectedFeedOption = .feed(selectedFeed)
                } else {
                    self?.selectedFeedOption = .all
                }
            }
            loadFeed(forceReload: false)
        }
        
        func loadFeed(forceReload: Bool) {
            self.store.dispatch(action: FeedAction.Refresh(forceLoad: forceReload))
        }
        
        func selectFeed(feedOption: FeedPickerOption) {
            store.dispatch(action: FeedAction.SelectFeed(feed: feedOption.feed))
        }
    }
    
    enum FeedPickerOption: Hashable {
        case all, feed(Feed)
        
        var title: String {
            return String((self.feed?.title ?? "All").prefix(20))
        }
        
        var feed: Feed? {
            switch self {
            case .all:
                return nil
            case .feed(let feed):
                return feed
            }
        }
    }
}

extension Post: Identifiable { }
