import { createContext, useContext, useEffect, useState } from 'react'
import './App.css';
import SharedRssreader from 'RssReader-shared';

const RssFeedContext = createContext()
const useRssFeedContext = () => useContext(RssFeedContext)

function RssFeedContextProvider({ children }) {
  const viewModel = SharedRssreader.com.github.jetbrains.rssreader.app.RssReaderJsViewModel
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(false)
  const [selectedFeed, setSelectedFeed] = useState(null)

  const onStateUpdate = (state) => {
    setLoading(state.progress)
    setSelectedFeed(state.selectedFeed)

    // EmptyList lacks the `toArray` interface
    if (typeof state?.feeds?.toArray !== 'undefined') {
      const feed = state.feeds.toArray()[0]
      if (typeof feed?.posts?.toArray !== 'undefined') {
        setPosts(feed.posts.toArray())
      }
    }

    // https://youtrack.jetbrains.com/issue/KT-28245 (I commented as App Manager)
    // The following is possible if prototype chain is extended is cjs module
    // EmptyList.prototype.toArray = function () {
    //   return []
    // };
    // state.feeds.toArray().forEach(f => setPosts(f.posts.toArray()))
  }

  useEffect(() => {
    viewModel.observeStore(onStateUpdate)
    viewModel.refreshFeeds()
    return () => viewModel.cancel()
  }, [viewModel])

  return (
    <RssFeedContext.Provider value={{ loading, selectedFeed, posts }}>
      {children}
    </RssFeedContext.Provider>
  )
}

function Feed() {
  const { loading, posts } = useRssFeedContext();

  const renderPost = (post) => (
    <div key={`${Math.random()}-${post.title}`} className="App-post">
      <h2>{post.title}</h2>
      <img src={post.imageUrl} alt={post.title} />
      <p>{post.desc}</p>
      <a target="_blank" rel="noreferrer" href={post.link}>{post.link}</a>
    </div>
  )

  if (loading) {
    return <span>Loading feed ...</span>
  }

  return posts.map(renderPost)
}

function App() {
  return (
    <RssFeedContextProvider>
      <div className="App">
        <main>
          <Feed />
        </main>
      </div>
    </RssFeedContextProvider>
  )
}

export default App;
