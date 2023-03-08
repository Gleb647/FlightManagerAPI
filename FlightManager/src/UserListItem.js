import axios from 'axios';
import { Link } from 'react-router-dom';
import './App.css'

function UserListItem(props){
    // const myName = "sss"

    // let reqParams = {
    //     name: myName
    // }

    const handleChangeButtonClick = () =>{
        axios.put(`http://localhost:8080/users/change/${props.id}`,{
            params: {
                name: "sssd"
            }
        })
        .then(response => {
            console.log(response.data);
        });
        
    }

    const handleDeleteButtonClick = async () =>{
        await axios.delete(`http://localhost:8080/users/delete/${props.id}`,{});
        props.changeUpdate();
    }

    return (
        <tr className="setTextMid">
            <td className="setTextMid setColumnTextMid">{props.name}</td>
            <td className="setTextMid setColumnTextMid">{props.surname}</td>
            <td className="setTextMid setColumnTextMid">{props.age}</td>
            <td>
            <button type="button" className="addBtn btn btn-outline-dark btn-sm" onClick={() => {handleChangeButtonClick()}}>Изменить</button>
                <button type="button" className="addBtn btn btn-outline-dark deleteBtn btn-sm" onClick={() => {handleDeleteButtonClick()}}>Удалить</button>
            </td>
        </tr>
    )
}

export {UserListItem};