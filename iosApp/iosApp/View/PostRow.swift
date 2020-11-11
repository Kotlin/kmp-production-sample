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
}

struct RssRow_Previews: PreviewProvider {
    static var previews: some View {
        PostRow(post: Post(title: "Introducing StateFlow and SharedFlow", link: "Today we’re pleased to announce the release of version 1.4.0 of the Kotlin Coroutines library. The highlights of the release are StateFlow and SharedFlow, which are being promoted to stable API. StateFlow and SharedFlow are designed to be used in cases where state management is required in an asynch", desc: "test", imageUrl: "https://blog.jetbrains.com/wp-content/uploads/2020/10/kotlin_blog_coroutines140.png", date: 1604048455))
    }
}
