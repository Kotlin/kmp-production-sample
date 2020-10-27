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

struct RssRow: View {
    var post: Post
    
    var body: some View {
        VStack {
            Text(post.title).bold()
            if let imageUrl = post.imageUrl, let url = URL(string: imageUrl) {
                URLImage(url: url) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                }.frame(height: 200)
            } else {
                Rectangle().fill(Color.gray).frame(width: 50, height: 50)
            }
            Text(post.desc ?? "")
        }
    }
}


struct RssRow_Previews: PreviewProvider {
    static var previews: some View {
        RssRow(post: Post(title: "test", link: "tet", desc: "test", imageUrl: "test", date: 1))
    }
}
