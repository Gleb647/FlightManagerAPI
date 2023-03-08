import axios from "axios";
import { useEffect, useState } from "react";
import { UserListItem } from "../UserListItem";

function UserFindInput(props){

    const [name, setName] = useState("");
    const [user, setUser] = useState([]);
    const [selectValue, setSelectValue] = useState("Имя..");

    useEffect(() => {
        props.changeUserFindName(name);
    }, [name])

    useEffect(() => {
        props.changeUserFindParam(selectValue);
    }, [selectValue])
    
        useEffect(() => {
            let reqParam = {};
            if (selectValue == "Фамилия.."){
                reqParam = {
                    surname: name
                }
            }else{
                reqParam = {
                    name: name
                }
            }
            const timer = setTimeout(() => {
                const fetchData = async () => {
                    
                await axios
                .get(
                    'http://localhost:8080/users/get', {
                        params: reqParam
                    })
                .then(response => {
                    setUser(response.data);});
                };
        
                fetchData();
              }, 100);
              return () => clearTimeout(timer);
            //console.log(data.length);
        }, [name]);

    
    
    const style = {placeholder: "Имя"}
    return(
        <div>
            <div className="input-group">
                <input className="form-control userFind form-control-sm" placeholder={selectValue} onChange={(event) => setName(event.target.value)}/>
                <span className="input-group-btn col-sm-8">
                <select className="btn btn-info" value={selectValue} onChange={(event) => setSelectValue(event.target.value)}>
                    <option value="Имя..">Имя</option>
                    <option value="Фамилия..">Фамилия</option>
                </select>
                </span>
            </div>
            <div>
            </div>
        </div>
    )
}

export default UserFindInput;