//
//  FeedsList.swift
//  iosApp
//
//  Created by Ekaterina.Petrova on 11.11.2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import RssReader

struct FeedsList: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    @SwiftUI.State var showsAlert: Bool = false
        
    
    var body: some View {
        List(viewModel.feeds, rowContent: FeedRow.init)
            .alert(isPresented: $showsAlert, TextAlert(title: "Title") {
                if let link = $0 {
                    viewModel.addFeed(url: link)
                }
            })
            .navigationTitle("Feeds list")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(trailing: Button(action: {
                showsAlert = true
            }) {
                Image(systemName: "plus.circle").imageScale(.large)
            })
    }
}

extension FeedsList {
    
    class ViewModel: ObservableObject {
        let store: FeedStore
        
        @Published var feeds: [Feed] = []

        init(store: FeedStore) {
            self.store = store
            
            store.watchState().watch { [weak self] state in
                guard let state = state  else {
                    // TODO: handle error
                    return
                }
                self?.feeds = state.feeds
            }
        }
        
        func loadFeed(forceReload: Bool) {
            store.dispatch(action: .Refresh(forceLoad: true))
        }
        
        func addFeed(url: String) {
            store.dispatch(action: .Add(url: url))
        }
        
        func removeFeed(url: String) {
            store.dispatch(action: .Delete(url: url))
        }

    }
}

extension Feed: Identifiable { }

//struct FeedsList_Previews: PreviewProvider {
//    static var previews: some View {
//        FeedsList(v)
//    }
//}
