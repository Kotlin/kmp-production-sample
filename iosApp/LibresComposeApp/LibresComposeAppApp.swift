//
//  LibresComposeAppApp.swift
//  LibresComposeApp
//
//  Created by Konstantin.Tskhovrebov on 11/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

@main
struct LibresComposeAppApp: App {
    var body: some Scene {
        DocumentGroup(newDocument: LibresComposeAppDocument()) { file in
            ContentView(document: file.$document)
        }
    }
}
