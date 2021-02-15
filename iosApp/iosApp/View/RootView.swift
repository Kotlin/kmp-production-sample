import SwiftUI
import RssReader

struct RootView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    let mainFeedView: MainFeedView
    @SwiftUI.State var errorMessage: String?
    
    var body: some View{
        ZStack {
            NavigationView {
                mainFeedView
            }.zIndex(0)
            if let errorMessage = errorMessage {
                VStack {
                    Spacer()
                    Text(errorMessage)
                        .foregroundColor(.white)
                        .padding(10.0)
                        .background(Color.black)
                        .cornerRadius(3.0)
                }
                .zIndex(1)
                .transition(.asymmetric(insertion: .move(edge: .bottom), removal: .opacity) )
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onReceive(viewModel.$errorMessage) { value in
            withAnimation {
                self.errorMessage = value
            }
        }
    }
}

extension RootView {
    class ViewModel: ObservableObject {
        let store: FeedStore
        
        @Published var errorMessage: String?
        
        init(store: FeedStore) {
            self.store = store
            
            store.watchSideEffect().watch { [weak self] effect in
                if let effect = effect as? FeedSideEffect.Error {
                    self?.errorMessage = effect.error.message
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                        self?.errorMessage = nil
                    }
                }
            }
        }
    }
}

