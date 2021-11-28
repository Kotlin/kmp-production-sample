import { useRssFeedContext } from '../context'

function Posts({ posts }) {
  const renderPost = (post) => (
    <div key={post.link} className="Feed-post">
      <h3>{post.title}</h3>
      <img src={post.imageUrl} alt={post.title} />
      <p>{post.desc}</p>
      <a target="_blank" rel="noreferrer" href={post.link} className="App-button App-button-primary">Read</a>
    </div>
  )

  return posts.map(renderPost)
}

export function Feed() {
  const { state, feedIndex } = useRssFeedContext();

  const renderLoading = () => <span className="App-m-32">Loading feed ...</span>
  const renderFeed = () => (
    <>
      <h1>{state.feeds[feedIndex].title}</h1>
      <h2 className="Feed-description">{state.feeds[feedIndex].desc}</h2>
      <div className="Feed-posts">
        <Posts posts={state.feeds[feedIndex].posts} />
      </div>
    </>
  )

  if (state.feeds.length === 0) return null;

  return (
    <main>
      {state.loading && renderLoading()}
      {renderFeed()}      
    </main>
  )
}
