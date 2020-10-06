import React, { useEffect, useState } from "react";
import Axios from "axios";
import "../App.css";
import SimpleUser from "../components/SimpleUser";
import Admin from "../components/Admin";

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
/* <div className="registration">
   <h1>Registration</h1>
   <label>Username</label>
   <input
     type="text"
     onChange={(e) => {
       setUsernameReg(e.target.value);
     }}
   />
   <label>Password</label>
   <input
     type="text"
     onChange={(e) => {
       setPasswordReg(e.target.value);
     }}
   />
   <button onClick={register}> Register </button>
 </div>
*/