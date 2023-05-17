import UIKit
import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
		    ZStack {
		        Color(#colorLiteral(red: 0.973, green: 0.529, blue: 0.235, alpha: 1)).ignoresSafeArea(.all)
			    ContentView()
			}.preferredColorScheme(.dark)
		}
	}
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.all, edges: .bottom)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
