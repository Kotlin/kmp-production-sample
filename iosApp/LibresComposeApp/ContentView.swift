//
//  ContentView.swift
//  LibresComposeApp
//
//  Created by Konstantin.Tskhovrebov on 11/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ContentView: View {
    @Binding var document: LibresComposeAppDocument

    var body: some View {
        TextEditor(text: $document.text)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(document: .constant(LibresComposeAppDocument()))
    }
}
