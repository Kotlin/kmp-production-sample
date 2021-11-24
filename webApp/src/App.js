import { createContext, useContext, useEffect, useState } from 'react'
import SharedRssreader from 'RssReader-shared';
import './App.css';

const RssFeedContext = createContext()
const useRssFeedContext = () => useContext(RssFeedContext)

function RssFeedContextProvider({ children }) {
  const SharedNS = SharedRssreader.com.github.jetbrains.rssreader.app
  const viewModel = SharedNS.RssReaderJsViewModel
  const stateWrapper = new SharedNS.FeedStateJsWrapper(false, [], null)
  
  const [state, setState] = useState(stateWrapper)

  useEffect(() => {
    viewModel.observeState(setState)
    viewModel.refreshFeeds(true)
    return () => viewModel.cancel()
  }, [viewModel])

  return (
    <RssFeedContext.Provider value={{ state }}>
      {children}
    </RssFeedContext.Provider>
  )
}

function FeedList() {
  const { state } = useRssFeedContext()  
  return state.feeds.map(f => (
    <img src={f.imageUrl} alt={f.title} className="App-feed-thumbnail" key={f.title} />
  ))
}

function Feed() {
  const { state } = useRssFeedContext();

  if (state.feeds.length === 0) return null;

  return (
    <>
      <h1>{state.feeds[0].title}</h1>
      <h2 className="App-feed-description">{state.feeds[0].desc}</h2>
      <div className="App-posts">
        <Posts posts={state.feeds[0].posts} />
      </div>
    </>
  )
}

function Posts({ posts }) {
  const { state } = useRssFeedContext();

  const renderPost = (post) => (
    <div key={`${Math.random()}-${post.title}`} className="App-post">
      <h3>{post.title}</h3>
      <img src={post.imageUrl} alt={post.title} />
      <p>{post.desc}</p>
      <a target="_blank" rel="noreferrer" href={post.link}>{post.link}</a>
    </div>
  )

  if (!posts) return null
  if (state.progress) return <span>Loading feed ...</span>

  return posts.map(renderPost)
}

function App() {
  return (
    <RssFeedContextProvider>
      <div className="App">
        <header>
          <FeedList />
        </header>
        <main>
          <Feed />
        </main>
      </div>
    </RssFeedContextProvider>
  )
}

export default App;
