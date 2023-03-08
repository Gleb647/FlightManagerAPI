import "./App.css"
import {UserListItem} from './UserListItem'
import axios from 'axios';
import  React,{ Component, useState, useEffect } from 'react';
import UserFindInput from "./UserFindInput/UserFindInput";

const UserList = (props) =>{

    const [update, setUpdate] = useState({...props.update});
    const [data, setData] = useState([]);
    const [userFindParam, setUserFindParam] = useState("");
    const [userFindName, setUserFindName] = useState("");
    
    // React.useEffect(() => {
    //     setUserFindParam(props.userFindParam);
    // }, [props.userFindParam])

    // React.useEffect(() => {
    //     setUserFindName(props.userFindName);
    // }, [props.userFindName])

    const changeUpdate = () =>{
        setUpdate(!update);
    }


    React.useEffect(() => {
        setUpdate(props.update);
    }, [props.update])

    useEffect(() => {
        const timer = setTimeout(() => {
            const fetchData = async () => {
                
            await axios(
                'http://localhost:8080/users/get',
            ).then(response => {
                setData(response.data);});
            };
    
            fetchData();
          }, 100);
          return () => clearTimeout(timer);
    }, [update]);

    const changeUserFindName = (name) =>{
        setUserFindName(name);
    }

    const changeUserFindParam = (param) =>{
        setUserFindParam(param);
    }

  return(
    <div>
        <UserFindInput changeUserFindName={changeUserFindName} changeUserFindParam={changeUserFindParam}/>
            <table className="table table-striped">
                <tbody>
                    <tr className="setTextMid">
                        <th scope="col">Name</th>
                        <th scope="col">Surname</th>
                        <th scope="col">Age</th>
                        <th scope="col"></th>
                    </tr>
            {data.filter(items => items.name === userFindName).length > 0 && userFindParam === "Имя.." 
            || data.filter(items => items.surname === userFindName).length > 0 && userFindParam === "Фамилия.." ?
            userFindParam === "Имя.." ? data.filter(items => items.name === userFindName).map(items=>{
                return (             
                    <UserListItem className='UsersTable' changeUpdate={changeUpdate} key={items.id} id={items.id} name={items.name} surname={items.surname} age={items.age}/>
                );
            }) : data.filter(items => items.surname === userFindName).map(items=>{
                return (             
                    <UserListItem className='UsersTable' changeUpdate={changeUpdate} key={items.id} id={items.id} name={items.name} surname={items.surname} age={items.age}/>
                );
            }) : data.map(items =>{
                return (             
                    <UserListItem className='UsersTable' changeUpdate={changeUpdate} key={items.id} id={items.id} name={items.name} surname={items.surname} age={items.age}/>
                );
            })}
                </tbody>
            </table>
        </div>
  )
}

export default UserList;