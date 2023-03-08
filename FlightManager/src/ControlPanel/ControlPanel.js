import React, {useState, useEffect, useCallback} from 'react';
import ReactDOM from 'react-dom';
import '../App.css'
import axios from 'axios';
import 'react-dropzone-uploader/dist/styles.css'
import { Link } from 'react-router-dom';



const ControlPanel = (props) => {

    const [inputFormDisplay, setInputFormDisplay] = useState('none');
    const [nameInput, setNameInput] = useState('');
    const [surnameInput, setSurnameInput] = useState('');
    const [ageInput, setAgeInput] = useState('');
    const [clearInputs, setClearInputs] = useState('');
    const [file, setFile] = useState('');

    const AddUserRequest = async () => {
        setInputFormDisplay('block');
        // let formData = new FormData();
        // formData.append('image', file);
        // formData.append('name', file.name)

        const MyUser = {
            name: nameInput,
            surname: surnameInput,
            age: ageInput,
            // image: formData,
        }
        await axios.post('http://localhost:8080/users/add', MyUser)
          .then(function (response) {
            console.log(response);
          })
          .catch(function (error) {
            console.log(error);
          }
          );    
        setNameInput('');
        setSurnameInput('');
        setAgeInput('');
    }

        // const getUploadParams = ({ meta }) => { return { url: 'https://httpbin.org/post' } }
        
        // // called every time a file's `status` changes
        // const handleChangeStatus = ({ meta, file }, status) => { setFile(file) }
        
        // // receives array of files that are done uploading when submit button is clicked
        // const handleSubmit = (files) => { console.log(files.map(f => f.meta)) }

        // const handleFile = (e) =>{
        //     let file = e.target.files[0];
        //     setFile(file);
        // }
    

    return(
            <div className="ControlPanel">             
                <div className='addForm centered'>
                    {/* <button type="button" className="addBtn btn btn-dark" onClick={() =>{setInputFormDisplay('block')}}>Добавить</button> */}
                    <div id = "addFormCard" className='addFormInput' style = {{display: "block"}}>
                    <table>
                        <tbody>
                        <tr>
                            <div className="form-group row">
                            <label for="inputName3" className="col-sm-3 col-form-label">Имя:</label>
                                <div className="col-sm-8">
                                    <input type="name" value={nameInput} className="form-control" id="inputName3" placeholder="Имя" name={nameInput} onChange={(event) => setNameInput(event.target.value)}/>
                                </div>
                            </div>
                        </tr>

                        <tr>
                            <div className="form-group row">
                            <label for="inputSurname3" className="col-sm-3 col-form-label">Фамилия:</label>
                                <div className="col-sm-8">
                                    <input type="name" value={surnameInput} className="form-control" id="inputSurname3" placeholder="Фамилия" name={surnameInput} onChange={(event) => setSurnameInput(event.target.value)}/>
                                </div>
                            </div>
                        </tr>
                        <tr>
                            <div className="form-group row">
                            <label for="inputAge3" className="col-sm-3 col-form-label">Возраст:</label>
                                <div className="col-sm-8">
                                    <input type="name" value={ageInput} className="form-control" id="inputAge3" placeholder="Возраст" name={ageInput} onChange={(event) => setAgeInput(event.target.value)}/>
                                </div>
                            </div>
                        </tr>
                        <tr>
                            <td>
                            <Link to="/getusers"><input className="btn btn-info" type="button" value="Post" onClick={ () =>{AddUserRequest();props.setTrueUpdateState()}}/></Link>             
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    </div>
                </div>
            </div>          
    )
}

export default ControlPanel;