//
//  FeedsList.swift
//  iosApp
//
//  Created by Ekaterina.Petrova on 11.11.2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import RssReader

struct FeedsList: ConnectedView {
    
    struct Props {
        let feeds: [Feed]
        let onAdd: (String) -> ()
        let onRemove: (String) -> ()
    }
    
    func map(state: FeedState, dispatch: @escaping DispatchFunction) -> Props {
        return Props(feeds: state.feeds, onAdd: { url in
            dispatch(FeedAction.Add(url: url))
        }, onRemove: { url in
            dispatch(FeedAction.Delete(url: url))
        })
    }
    
    @SwiftUI.State var showsAlert: Bool = false
    
    func body(props: Props) -> some View {
        List {
            ForEach(props.feeds) { feed in
                FeedRow(feed: feed)
            }
            .onDelete( perform: { set in
                set.map { props.feeds[$0] }.forEach { props.onRemove($0.sourceUrl) }
            })
        }
        .alert(isPresented: $showsAlert, TextAlert(title: "Title") {
            if let url = $0 {
                props.onAdd(url)
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

extension Feed: Identifiable { }

