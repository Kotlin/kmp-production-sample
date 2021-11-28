import Modal from 'react-modal'
import { useState } from 'react'
import { useRssFeedContext } from '../context'
import { FeedButton } from './FeedButton';
import { ReactComponent as Pencil } from '../assets/pencil-outline.svg'
import { ReactComponent as Clear } from '../assets/clear.svg'

const modalStyles = {
  content: {
    margin: 0,
    top: 0,
    left: 0,
    bottom: 0,
    width: '100%',
  },
};

export function AddFeedButton() {
  const { viewModel, state, setFeedIndex } = useRssFeedContext()
  const [visible, setVisible] = useState(false)
  const [value, setValue] = useState('')

  const toggle = () => setVisible(!visible)
  const onChange = (ev) => setValue(ev.target.value)
  const onAdd = () => {
    if (value) {
      viewModel.addFeed(value)
      setValue('')
    }
  }
  const onDelete = (url) => {
    setFeedIndex(0)
    setVisible(false)
    viewModel.deleteFeed(url)
  }

  const renderFeedManagableItem = (feed, index) => (
    <div className="Feed-mgmt-container" key={index}>
      <div className="App-mt-16">
        <FeedButton feed={feed} index={index} />
      </div>
      <span>{feed.title}</span>
      <div onClick={() => onDelete(feed.sourceUrl)} className="App-mr-32">
        <Clear />
      </div>
    </div>
  )

  const renderFeedInput = () => (
    <div className="Feed-mgmt-container">
    <input type="text" className="Feed-input" placeholder="feed URL ..." value={value} onChange={onChange} />
    </div>
  )

  return (
    <>
    <div className="Feed-button-container Feed-add-button" onClick={toggle}>
      <Pencil />
    </div>
    <Modal
        isOpen={visible}
        onRequestClose={toggle}
        style={modalStyles}
      >
        <div className="App-container-90">
          {state.feeds.map(renderFeedManagableItem)}
          {renderFeedInput()}
          <div className="App-flex-sb">
            <button onClick={onAdd} className="App-button App-button-primary">Add</button>
            <button onClick={toggle} className="App-button App-button-secondary">close</button>
          </div>
        </div>
      </Modal>
    </>
  )
}
