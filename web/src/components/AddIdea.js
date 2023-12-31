import React,{ useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { getSessionKey } from '../Situation';
import {decode as base64_decode, encode as base_64encode} from 'base64';

const AddIdea_STYLES = {
  position: 'fixed',
  top: '33%',
  left: '36%',
  right: '36%',
  backgroundColor: '#FFF',
  border: '3px solid pink',
  borderRadius: '20px',
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

export default function AddIdea({open, onClose}) {

  let encoded = base64_encode('YOUR_DECODED_STRING');
  let deocded = base64_decode('YOUR_ENCODED_STRING');

  const [title, setTitle] = useState();
  const [message, setMessage] = useState();
  const [link, setLink] = useState('');
  const [idea_id,setIdea_id] = useState();

  if(!open){
    return null;
  }
  const addNewLink = async(e) =>{
    axios.post(`https://cse216-fl22-team14-new.herokuapp.com/resources/:${idea_id}/-1?sessionKey=${getSessionKey()}`,{
      mLink: link,
    })
    .catch(error => {
      console.log(error.massage)
    });
    onClose();
  }
  const addNewIdea = async(e) =>{
    axios.post(`https://cse216-fl22-team14-new.herokuapp.com/ideas?sessionKey=${getSessionKey()}`,{
      mTitle: title,
      mMessage: message,
    })
    .then(response => {
      console.log(response.data);
    })
    .catch(error => {
      console.log(error.massage)
    });
    addNewLink();
    onClose();
  }

  const handleChange = e => {
    setLink(e.target.value);
  }

  const handleFile = e => {
    const Buffer = require("buffer").Buffer;
    let encodedAuth = new Buffer(decoded).toString("base64");
  }

  return ReactDOM.createPortal(
    <div style={OVERLAY_STYLE}>
      <div style={AddIdea_STYLES}>
        <label><b className='text'>Title of Idea:</b></label>
        <br></br>
        <input placeholder="Title for your bright idea..." type="text" id="newTitle" onChange={(e) => setTitle(e.target.value)}/>
        <br></br>
        <br></br>
        <label className='text'><b>Idea:</b></label>
        <br></br>
        <textarea rows="4" placeholder="What bright idea do you have..."id="newMessage" onChange={(e) => setMessage(e.target.value)}></textarea>
        <br></br>
        <label className='text'><b>Link:</b></label>
        <br></br>
        <input placeholder='Provide link to support your idea if needed' type="text" id="newLink" name="link" onChange={handleChange} value={link}></input>
        <hr></hr>
        <button className='acu-buttons' id="addButton" onClick={(e) => addNewIdea(e)}>Add</button>
        <button className='acu-buttons' id="addCancel" onClick={onClose}>Cancel</button>
        <br></br>
        <button className='acu-buttons' id="addFile" onClick={handleFile} type="file" >Upload File</button>
      </div>
    </div>,
    document.getElementById('portal')
  )
}
