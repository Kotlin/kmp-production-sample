import { useEffect, useState, createContext, useContext } from 'react'
import SharedRssreader from 'RssReader-shared';

const RssFeedContext = createContext()
export const useRssFeedContext = () => useContext(RssFeedContext)

export function RssFeedContextProvider({ children }) {
  const SharedNS = SharedRssreader.com.github.jetbrains.rssreader.app
  const viewModel = SharedNS.RssReaderJsViewModel
  const stateWrapper = new SharedNS.FeedStateJsWrapper(false, [], null)
  
  const [state, setState] = useState(stateWrapper)
  const [feedIndex, setFeedIndex] = useState(0)

  useEffect(() => {
    viewModel.observeState(setState)
    viewModel.refreshFeeds(true)
    return () => viewModel.cancel()
  }, [viewModel])

  return (
    <RssFeedContext.Provider value={{ viewModel, state, feedIndex, setFeedIndex }}>
      {children}
    </RssFeedContext.Provider>
  )
}
