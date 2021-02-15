//
//  FeedRow.swift
//  iosApp
//
//  Created by Ekaterina.Petrova on 11.11.2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import RssReader
import URLImage

struct FeedRow: View {
    let feed: Feed
    
    private enum Constants {
        static let imageWidth: CGFloat = 20.0
    }
    
    var body: some View {
        HStack {
            if let imageUrl = feed.imageUrl, let url = URL(string: imageUrl) {
                URLImage(url: url) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
    
                }
                .frame(width: Constants.imageWidth, height: Constants.imageWidth)
                .clipped()
                .cornerRadius(Constants.imageWidth / 2.0)
            }
            VStack(alignment: .leading, spacing: 5.0) {
                Text(feed.title).bold().font(.title3).lineLimit(1)
                Text(feed.desc).font(.body)
            }
        }
    }
}



