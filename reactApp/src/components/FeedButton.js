import { useRssFeedContext } from '../context'

export function FeedButton({ feed, index, onClick }) {
  const { feedIndex } = useRssFeedContext()
  let classNames = 'Feed-button-container'
  
  if (!feed.imageUrl) classNames = classNames.concat(' Feed-button-all')
  if (index === feedIndex) classNames = classNames.concat(' Feed-selected')

  return (
    <div onClick={onClick} className={classNames}>
      {!feed.imageUrl && <span>{feed.title.slice(0, 1).toUpperCase()}</span>}
      {feed.imageUrl && <img src={feed.imageUrl} alt={feed.title} className="Feed-thumbnail" />}
    </div>
  )
}
