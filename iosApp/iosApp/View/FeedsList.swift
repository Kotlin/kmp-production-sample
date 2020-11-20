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
    
    var body: some View {
        List(viewModel.feeds, rowContent: FeedRow.init)
            .navigationTitle("Feeds list")
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarItems(trailing: Button(action: { print("add item")} ) {
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
            DispatchQueue.main.async { [self] in
                self.store.dispatch(action: FeedAction.Refresh(forceLoad: true))
            }
        }
    
    }
}

extension Feed: Identifiable { }

//struct FeedsList_Previews: PreviewProvider {
//    static var previews: some View {
//        FeedsList(v)
//    }
//}
