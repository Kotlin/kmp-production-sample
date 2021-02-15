//
//  NavigationLazyView.swift
//  iosApp
//
//  Created by Ekaterina.Petrova on 15.02.2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import SwiftUI

struct NavigationLazyView<Content: View>: View {
    let build: () -> Content
    init(_ build: @autoclosure @escaping () -> Content) {
        self.build = build
    }
    var body: Content {
        build()
    }
}
