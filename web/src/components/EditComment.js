import React,{ useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { getSessionKey } from '../Situation';

const AddIdea_STYLES = {
  position: 'fixed',
  top: '33%',
  left: '36%',
  right: '36%',
  backgroundColor: '#FFF',
  border: '3px solid pink',
  padding: '20px',
  zIndex: 1000
}

const OVERLAY_STYLE = {
  position: 'fixed',
  top:0,
  left:0,
  right:0,
  bottom:0,
  backgroundColor: 'rgba(0,0,0,.7)',
  zIndex:1000
}

export default function EditComment({open, onClose,idea_id, com_id,old_content}) {

    const [content, setContent] = useState();
    
    if(!open){
        return null;
      }
    
    const changeComment = async(e) =>{
    axios.put(`https://cse216-fl22-team14-new.herokuapp.com/ideas/${idea_id}/comment/${com_id}?sessionKey=${getSessionKey()}`,{
        mContent: content
    })
        .catch(error => {
            console.log(error.massage)
        });
        onClose();
    }

    return ReactDOM.createPortal(
        <div style={OVERLAY_STYLE}>
          <div style={AddIdea_STYLES}>
            <label className='text'><b>Change Idea Comment:</b></label>
            <input type="text" id="newConnent" onChange={(e) => setContent(e.target.value)}/>
            <hr></hr>
            <button className='acu-buttons' id="addButton" onClick={(e) => changeComment(e)}>Change</button>
            <button className='acu-buttons' id="addCancel" onClick={onClose}>Cancel</button>
          </div>
        </div>,
        document.getElementById('portal')
      )
}
