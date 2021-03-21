import SwiftUI
import RssReader

struct RootView: View {
    @EnvironmentObject var store: ObservableFeedStore
    @SwiftUI.State var errorMessage: String?
    
    var body: some View {
        ZStack {
            NavigationView {
                MainFeedView()
            }.zIndex(0)
            if let errorMessage = self.errorMessage {
                VStack {
                    Spacer()
                    Text(errorMessage)
                        .foregroundColor(.white)
                        .padding(10.0)
                        .background(Color.black)
                        .cornerRadius(3.0)
                }
                .padding(.bottom, 10)
                .zIndex(1)
                .transition(.asymmetric(insertion: .move(edge: .bottom), removal: .opacity) )
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onReceive(store.$sideEffect) { value in
            if let errorMessage = (value as? FeedSideEffect.Error)?.error.message {
                withAnimation { self.errorMessage = errorMessage }
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                    withAnimation { self.errorMessage = nil }
                }
            }
        }
    }
}
