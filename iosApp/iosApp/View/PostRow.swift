//
//  RssRow.swift
//  iosApp
//
//  Created by Ekaterina.Petrova on 25.10.2020.
//  Copyright © 2020 orgName. All rights reserved.
//

import SwiftUI
import RssReader
import URLImage

struct PostRow: View {
    let post: Post
    
    var body: some View {
        if let postURL = post.linkURL {
            Link(destination: postURL) {
                content
            }
            .foregroundColor(.black)
        } else {
            content
        }
    }
    
    var content: some View {
        VStack(alignment: .leading, spacing: 10.0) {
            Text(post.title).bold().font(.title3)
            if let imageUrl = post.imageUrl, let url = URL(string: imageUrl) {
                URLImage(url: url) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                }
                .frame(minWidth: 0, maxWidth: .infinity)
                .clipped()
            }
            Text(post.desc ?? "").font(.body)
            HStack{
                Spacer()
                Text(post.dateString).font(.footnote).foregroundColor(.gray)
            }
        }
    }
}

extension Post {
    static let dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "E, MMM d HH:mm"
        return formatter
    }()
    
    var dateString: String {
        return Post.dateFormatter.string(from: Date(timeIntervalSince1970: TimeInterval(date)))
    }
    
    var linkURL: URL? {
        if let link = link {
            return URL(string: link)
        } else {
            return nil
        }
    }
}

