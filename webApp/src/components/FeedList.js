import { useRssFeedContext } from '../context'
import { AddFeedButton } from './AddFeedButton'
import { FeedButton } from './FeedButton'

function AllFeedsButton() {
  return (
    <div className="Feed-button-container Feed-button-all">
      <span>All</span>
    </div>
  )
}

export function FeedList() {
  const { state, setFeedIndex } = useRssFeedContext()
  return (
    <header>
      <AddFeedButton />
      <AllFeedsButton />
      {state.feeds.map((f, i) => (<FeedButton feed={f} key={f.sourceUrl} index={i} onClick={() => setFeedIndex(i)} />))}
    </header>
  )
}
