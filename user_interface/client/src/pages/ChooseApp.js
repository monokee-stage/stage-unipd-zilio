import React from "react";
import {Link} from "react-router-dom";
import AppConfirmed from "./AppConfirmed";
import WaitingAdminApproval from "./WaitingAdminApproval";
import Axios from "axios";

class ChooseApp extends React.Component{

    state = {
        listApp: [],
        found: false
    }

    getApp = (app) => {
        Axios.post("http://localhost:3001/getApp", {
            username: this.props.user,
            app: app
        }).then((response) => {
            if (response.data.message) {
                console.log(response)
                this.state.found = false;
            } else {
                console.log(response)
                this.state.found = true;
            }
        });
    }

    setAppProperties(app) {
        let needAdmin = false;
        if(app !== "GitHub")
            needAdmin = true;
        return [app, needAdmin]
    }

    handleChoice(e) {
        e.preventDefault()
        let app = e.target.app.value;
        this.props.choice(this.setAppProperties(app))
        this.getApp(app)
    }

    handleSignOut(e) {
        e.preventDefault()
        this.props.onSignOut()
    }

    handleList(list) {
        this.state.listApp = list;
        console.log(this.state.listApp)
    }


    handler = () => {
            if (this.props.isAdmin === "true") {
                return (
                    <div>
                        <AppConfirmed app={this.props.app[0]}
                                      listApp={this.handleList.bind(this)}
                                      user={this.props.user}
                                      found={this.state.found}
                        />
                    </div>
                );
            } else {
                if (this.props.app[0] === "GitHub")
                    return (
                        <div>
                            <AppConfirmed app={this.props.app[0]}
                                          listApp={this.handleList.bind(this)}
                                          user={this.props.user}
                                          found={this.state.found}
                            />
                        </div>
                    );
                else
                    return (
                        <div>
                            <WaitingAdminApproval listApp={this.state.listApp}
                                                  app={this.props.app[0]}
                                                  user={this.props.user}
                                                  found={this.state.found}/>
                        </div>
                    );
            }

    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleChoice.bind(this)}>
                    <label for="apps">Choose an application: </label>
                    <select name="apps" id="app">
                        <option value="GitHub">GitHub</option>
                        <option value="Google Drive">Google Drive</option>
                        <option value="Facebook">Facebook</option>
                    </select>
                    <p>
                        <input type="submit" value="Confirm"/>
                        <input type="submit" value="Sign Out" onClick={this.handleSignOut.bind(this)}/>
                    </p>
                </form>
                {this.handler()}
            </div>
        )
    }
}

export default ChooseApp;