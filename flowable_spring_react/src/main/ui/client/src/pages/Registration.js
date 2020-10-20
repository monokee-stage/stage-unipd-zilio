import React from "react";
import Admin from "../components/Admin";
import SimpleUser from "../components/SimpleUser";

const {Component} = require("react");

class Registration extends Component {

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

  componentDidMount() {
      this.getUsers()
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

  login = (username, password) => {
    fetch("http://localhost:8081/api/user/" + `${username}` + "&" + `${password}`, {
      method: "GET",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }
    })
        .then(response => {
          return response.json();
        })
        .then(data => {
          if (data)
            this.setState({
              username: data.id_,
              password: data.pwd_,
              loginOk: true
            });
          if (data.id_ === "admin")
            this.setState({admin: true})
        })
        .catch(error => {
          console.log(error)
          this.setState({username: null, password: null, admin: false, loginOk: false})
        });
  }

  handleLogin = () => {
    this.login(this.state.username, this.state.password)
  }

  checkCombination = () => {
    console.log(this.state.loginOk)
    console.log(this.state.username)

    if (this.state.loginOk === true && this.state.username !== null) {
      if (this.state.admin === true) {
        return (
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
        return (
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
        return (
            <div/>
        )
    } else if(this.state.loginOk === false && this.state.username === null) {
        return (
            <h2>Wrong username/password combination!</h2>
        )
    }
  }

  render() {
    return (
        <div>
          <div className="App">
            <div className="login">
              <h1>Login</h1>
              <input
                  type="text"
                  placeholder="Username..."
                  onChange={(e) => {
                    this.setUsername(e.target.value);
                  }}
              />
              <input
                  type="password"
                  placeholder="Password..."
                  onChange={(e) => {
                    this.setPassword(e.target.value);
                  }}
              />
              <button onClick={this.handleLogin.bind(this)}> Login </button>
              {this.checkCombination()}
            </div>
          </div>
        </div>
    );
  }
}

export default Registration;