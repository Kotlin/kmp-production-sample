import SwiftUI
import RssReader
import URLImage

struct MainFeedView: ConnectedView {
    
    struct Props {
        let loading: Bool
        let items: [Post]
        let feedOptions: [FeedPickerOption]
        let selectedFeedOption: FeedPickerOption
        
        let onReloadFeed: (Bool) -> Void
        let onSelectFeed: (Feed?) -> Void
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
    
    func map(state: FeedState, dispatch: @escaping DispatchFunction) -> Props {
        let selectedFeedOption: FeedPickerOption
        if let selectedFeed = state.selectedFeed {
            selectedFeedOption = .feed(selectedFeed)
        } else {
            selectedFeedOption = .all
        }
        return Props(loading: state.progress,
              items: state.mainFeedPosts(),
              feedOptions: [.all] + state.feeds.map { FeedPickerOption.feed($0)},
              selectedFeedOption: selectedFeedOption,
              onReloadFeed: { reload in
                dispatch(FeedAction.Refresh(forceLoad: reload))
              },
              onSelectFeed: { feed in
                dispatch(FeedAction.SelectFeed(feed: feed))
              })
    }
    
    
    @SwiftUI.State private var showSelectFeed = false
    
    init() {
        UITableView.appearance().backgroundColor = .white
    }
    
    func body(props: Props) -> some View {
        VStack {
            if showSelectFeed {
                feedPicker(props: props)
            }
            List(props.items, rowContent: PostRow.init)
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarItems(leading: refreshButton(props: props), trailing: editFeedLink)
        .toolbar {
            ToolbarItem(placement: .principal) {
                navigationTitle(props: props)
            }
        }
        .onAppear {
            props.onReloadFeed(true)
        }
    }
    
    var refreshButtionAnimation: Animation {
        Animation.linear(duration: 0.8).repeatForever(autoreverses: false)
    }
    
    func navigationTitle(props: Props) -> some View {
        VStack {
            HStack {
                Text("RSS Reader").font(.headline)
                Button(action: {
                    withAnimation { showSelectFeed.toggle() }
                }) {
                    Image(systemName: showSelectFeed ? "chevron.up" : "chevron.down").imageScale(.small)
                }
            }
            Text(props.selectedFeedOption.title).font(.subheadline).lineLimit(1)
        }
    }
    
    
    func feedPicker(props: Props) -> some View {
        let binding = Binding<FeedPickerOption>(
            get: { props.selectedFeedOption },
            set: { props.onSelectFeed($0.feed) }
        )
        return Picker("", selection: binding) {
            ForEach(props.feedOptions, id: \.self) { option in
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
        .pickerStyle(.wheel)
    }
    
    func refreshButton(props: Props) -> some View {
        Button(action: {
            props.onReloadFeed(true)
        }) {
            Image(systemName: "arrow.clockwise")
                .imageScale(.large)
                .rotationEffect(Angle.degrees(props.loading ? 360 : 0)).animation( props.loading ? refreshButtionAnimation : .default)
        }
    }
    
    var editFeedLink: some View {
        NavigationLink(destination: NavigationLazyView<FeedsList>(FeedsList())) {
            Image(systemName: "pencil.circle").imageScale(.large)
        }
    }
    
}

extension Post: Identifiable { }
