import React from "react";
import Admin from "../components/Admin";
import SimpleUser from "../components/SimpleUser";

const {Component} = require("react");

class Registration extends Component {

    constructor(props) {
        super(props);
        this.getUsers()
    }


    state = {
        username: "",
        password: "",
        app: "",
        loginOk: false,
        admin: false
    }

    getUsers = () => {
      fetch("http://localhost:8081/api/users", {
              method: "GET",
              headers: {
                  'Accept': 'application/json',
                  'Content-Type': 'application/json',
              }
          }
      ).then((response) => {
          console.log(response)
      });
    }

    setUsername = (username) => {
        this.state.username = username;
    }

    setPassword = (password) => {
        this.state.password = password;
    }

    setIsAdmin = (isAdmin) => {
        this.state.admin = isAdmin;
    }


    setApp = (app) => {
        this.state.app = app;
    }

    setLoginOk = (loginOk) => {
        this.state.loginOk = loginOk;
    }

    onSignOut = () => {
        this.setUsername(null)
        this.setPassword(null)
        this.setApp(null)
        this.setLoginOk(false)
        this.setIsAdmin(false)
        window.location.reload();
    }

    onSignOutNoReload = () => {
        this.setUsername(null)
        this.setPassword(null)
        this.setApp(null)
        this.setLoginOk(false)
        this.setIsAdmin(false)
    }

    login = (username, password) => {
        fetch("http://localhost:8081/api/user/" + `${username}` + "&" + `${password}`, {
            method: "GET",
            headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            }
        }).then(response => {
            return response.json();
        }).then(data => {
            if (data)
                this.setState({
                  username: data.id_,
                  password: data.pwd_,
                  loginOk: true
                });
            if (data.id_ === "admin")
                this.setState({admin: true})
        }).catch(error => {
            //console.log(error)
            this.setState({loginOk: false, username: null, password: null})
        });
    }

    handleLogin = () => {
        this.login(this.state.username, this.state.password)
    }

    checkCombination = () => {
        const r = []
        if (this.state.loginOk === true && this.state.username !== null) {
          if (this.state.admin === true) {
            r.push(
                <div>
                  <h1>{this.state.username}</h1>
                  <Admin
                      user={this.state.username}
                      isAdmin={this.state.admin}
                      onSignOut={this.onSignOut}
                      choice={this.setApp.bind(this)}
                      app={this.state.app}/>
                </div>
            )
          } else {
            r.push(
                <div>
                  <h1>{this.state.username}</h1>
                  <SimpleUser
                      user={this.state.username}
                      isAdmin={this.state.admin}
                      onSignOut={this.onSignOut}
                      choice={this.setApp.bind(this)}
                      app={this.state.app} />
                </div>
            )
          }
        } else if(this.state.loginOk === true && this.state.username === null) {
            r.push(
                <div/>
            )
        } else if(this.state.loginOk === false && this.state.username === null) {
            r.push(
                <h2>Wrong username/password combination!</h2>
            )
        }
        return r;
    }

    render() {
        return (
            <div>
              <div className="App">
                <div className="login">
                  <h1>Login</h1>
                    <input
                      type="text"
                      id="username"
                      placeholder="Username..."
                      onChange={(e) => {
                        this.setUsername(e.target.value);
                      }}
                    />
                    <input
                      type="password"
                      id="password"
                      placeholder="Password..."
                      onChange={(e) => {
                        this.setPassword(e.target.value);
                      }}

                    />
                    <input type="submit" id="loginBtn" value="Login" onClick={this.handleLogin.bind(this)}/>
                </div>
                  {this.checkCombination()}
              </div>
            </div>
        );
    }
}

export default Registration;