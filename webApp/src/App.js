import { RssFeedContextProvider } from './context'
import { FeedList } from './components/FeedList'
import { Feed } from './components/Feed'

function App() {
  return (
    <RssFeedContextProvider>
      <div className="App">
        <FeedList />
        <Feed />
      </div>
    </RssFeedContextProvider>
  )
}

export default App
