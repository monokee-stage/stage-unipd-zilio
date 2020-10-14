import React, { useEffect, useState } from "react";
import Axios from "axios";
import "../App.css";
import SimpleUser from "../components/SimpleUser";
import Admin from "../components/Admin";
import ApiService from "../components/ApiService";

export default function Registration() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [app, setApp] = useState("");

  const [admin, setAdmin] = useState("");

  const [loginStatus, setLoginStatus] = useState("");

  Axios.defaults.withCredentials = true;

  const login = () => {
    Axios.post("http://localhost:3001/login", {
      username: username,
      password: password,
    }).then((response) => {
      if (response.data.message) {
        setLoginStatus(response.data.message);
      } else {
        setLoginStatus(response.data[0].ID_);
        if(response.data[0].REV_ === 1)
          setAdmin("true")
        else if(response.data[0].REV_ === 2)
          setAdmin("false")
      }
    });
  };

  useEffect(() => {
    Axios.get("http://localhost:3001/login").then((response) => {
      if (response.data.loggedIn === true) {
        setLoginStatus(response.data.user[0].username);
      }
    });
  }, []);

  const signOut = () => {
    setLoginStatus(null)
    setApp("")
  }

  return (
      <div className="App">
        <div className="login">
          <h1>Login</h1>
          <input
              type="text"
              placeholder="Username..."
              onChange={(e) => {
                setUsername(e.target.value);
              }}
          />
          <input
              type="password"
              placeholder="Password..."
              onChange={(e) => {
                setPassword(e.target.value);
              }}
          />
          <button onClick={login}> Login </button>
          <h1>{loginStatus}</h1>
        </div>
        {
          (loginStatus === username) ?
              <div>
                {
                  (admin === "true") ?
                      <div>
                        <Admin
                            user={loginStatus}
                            isAdmin={admin}
                            onSignOut={signOut}
                            choice={setApp.bind(this)}
                            app={app} />
                      </div>
                      :
                      <div>
                        <SimpleUser
                            user={loginStatus}
                            isAdmin={admin}
                            onSignOut={signOut}
                            choice={setApp.bind(this)}
                            app={app} />
                      </div>
                }
              </div>
              :
              null
        }
      </div>
  );
}
/*
import React from "react";
import Admin from "../components/Admin";
import SimpleUser from "../components/SimpleUser";

const {Component} = require("react");

class Registration extends Component {

    state = {
        username: "",
        password: "",
        app: "",
        admin: "",
        loginStatus: ""
    }

    signOut = () => {
        this.setLoginStatus(null)
        this.setApp("")
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

    setLoginStatus = (user) => {
        this.state.loginStatus = user;
    }

    setApp = (app) => {
        this.state.app = app;
    }

    /*useEffect = (user) => {
        fetch(
            "http://localhost:8081/api/user/" + `${user}`
        )
            .then(response => {
                if (response.data.loggedIn === true) {
                    this.setLoginStatus(response.data.user[0].username);
                }
            })
    }

    login = (user) => {
        fetch(
            "http://localhost:8081/api/user/" +`${user}`, {
                method: "GET",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        )   .then(response => {
                return response.json();
            })
            .then(data => {
                this.setState({
                    username: data.id_
                })
                this.setUsername(data[0].id_)
                this.setPassword(data[0].pwd_)
                this.setLoginStatus(data[0].id_)
            })
            .catch(error => {
              console.log(error);
            });
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
                        <button onClick={this.login(this.state.username)}> Login </button>
                        <h1>{this.state.loginStatus}</h1>
                    </div>
                    {
                        (this.state.loginStatus === this.state.username) ?
                            <div>
                                {
                                    (this.state.admin === "true") ?
                                        <div>
                                            <Admin
                                                user={this.state.loginStatus}
                                                isAdmin={this.state.admin}
                                                onSignOut={this.signOut()}
                                                choice={this.setApp.bind(this)}
                                                app={this.state.app} />
                                        </div>
                                        :
                                        <div>
                                            <SimpleUser
                                                user={this.state.loginStatus}
                                                isAdmin={this.state.admin}
                                                onSignOut={this.signOut()}
                                                choice={this.setApp.bind(this)}
                                                app={this.state.app} />
                                        </div>
                                }
                            </div>
                            :
                            null
                    }
                </div>
            </div>
        );
    }
}

export default Registration;*/