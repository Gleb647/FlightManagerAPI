import './App.css';
import { Component } from 'react';
import ControlPanel from './ControlPanel/ControlPanel';
import UserList from './UserList';
import UserFindInput from './UserFindInput/UserFindInput'
import ButtonAppBar from './header/ButtonAppBar';
import { Link, Route, Routes } from 'react-router-dom';
import { BrowserRouter as Router } from 'react-router-dom';

export default class App extends Component{

  constructor(props){
    super(props);
    this.state = {
      update: false,
      hideList: false,
      // userFindName: "",
      // userFindParam: "",
    }
  }

  // changeUserFindName = (name) =>{
  //   this.setState({
  //     userFindName: name
  //   });
  // }

  // changeUserFindParam = (param) =>{
  //   this.setState({
  //     userFindParam: param
  //   });
  // }


  setTrueUpdateState = () =>{
    console.log("changing update state");
    this.setState({
      update: !this.state.update
    })
  }

  setFalseUpdateState = () =>{
    this.setState({
      update: false
    })
  }

  changeHideList = () =>{
    this.setState({
      hideList: true
    })
  }

  // componentDidMount() {
  //   this.postedStateChange();
  //   this.interval = setInterval(() => this.setState({ time: Date.now() }), 1000);
  // }
  // componentWillUnmount() {
  //   clearInterval(this.interval);
  // }

  // postedStateChange = () =>{
  //   this.setState({
  //     posted: !this.state.posted
  //   })
  // }


  render(){
    //console.log("rendering");
    const style1 = {flexDirection:'row', alignItems:'center', justifyContent:'center'}
    return (
      <div className="App">
          <ButtonAppBar></ButtonAppBar>
          {/* <div style={style1}> */}
          <Routes>
            <Route path="/" element={<UserList update={this.state.update}/>}/>
            <Route path="/adduser" element={<ControlPanel setTrueUpdateState={this.setTrueUpdateState}/>} />
            <Route path="/change-users-data" element={<ControlPanel setTrueUpdateState={this.setTrueUpdateState}/>} />
            <Route path="/getusers" element={<UserList update={this.state.update}/>}/>
          </Routes>
      </div>
    )
  }
}
