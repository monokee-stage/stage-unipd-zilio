/*
import React from "react";
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

export default ChooseApp;*/
import Axios from "axios";
import React from "react";
import ValidationRequest from "../task/ValidationRequest";
import Admin from "../components/Admin";
import WaitingAdminApproval from "./WaitingAdminApproval";
import AppConfirmed from "./AppConfirmed";

const {Component} = require("react");

class ChooseApp extends Component {
    constructor(props) {
        super(props);
        this.getAppFromDB()
    }

    state = {
        apps: [],
        selectedApp: "",
        needValidation: 0,
        validationError: "",
        userApps: [],
        valChoice: ""
    };

    handleSignOut(e) {
        e.preventDefault()
        this.props.onSignOut()
    }

    handleChoice(e) {
        e.preventDefault()
        this.checkApp()
    }

    componentDidMount() {
        fetch(
            "http://localhost:8081/api/appsName"
        )
            .then(response => {
                return response.json();
            })
            .then(data => {
                this.setState({
                    apps: data
                });
            })
            .catch(error => {
                console.log(error);
            });
    }

    getAppFromDB = () => {
        fetch("http://localhost:8081/api/getAppUser/" + `${this.props.user}`
        )
            .then(response => {
                return response.json();
            })
            .then(data => {
                this.setState({
                    userApps: data
                });
            })
            .catch(error => {
                console.log(error);
            });
    }

    checkApp = () => {
        //console.log(this.state.userApps)
        //console.log(this.state.userApps.includes(this.state.selectedApp))
        if(this.state.userApps.includes(this.state.selectedApp)){
            return (
                <p style={{color: "red"}}>You've already added this application.</p>
            )
        } else {
            this.props.choice(this.state.selectedApp) //set the prop
            //check if the app needs the admin approval
            this.checkIfNeedValidation(this.state.selectedApp)
            this.getChoice(this.props.user)
            //if it does not need, i'll just send the app to the DB table
            if(this.state.needValidation === 0) {
                this.sendAppToDB(this.state.selectedApp, 1) //app validated
                return (
                    <AppConfirmed />
                )
            }else{   //if it needs approval, i need the admin to approve this, so as long as the admin does not login, i can't do anything
                if(this.state.selectedApp !== "Github")
                    return (
                        <WaitingAdminApproval />
                    )
                /*
                (this.state.valChoice === 1) ?
                    this.sendAppToDB(this.state.selectedApp, 1) //app validated
                    :
                    this.sendAppToDB(this.state.selectedApp, 0) //app NOT validated
                    */
            }
        }
    }

    getChoice = (id) => {
        fetch("http://localhost:8081/api/getLastAppUser/" + `${id}`, {
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

    setValChoice = (val) => {
        if(val === "yes")
            this.setState({
                valChoice: 1
            })
        else
            this.setState({
                valChoice: 0
            })
    }

    checkIfNeedValidation = (app) => {
        fetch("http://localhost:8081/api/getIfNeedValidation/" + `${app}`, {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }).then(response => {
            return response.json();
        }).then(data => {
            if(data === true) {
                this.setState({needValidation: 1})
            } else
                this.setState({needValidation: 0})
        }).catch(error => {
            console.log(error);
        });
    }

    sendAppToDB = (app, validation) => {
        fetch("http://localhost:8081/api/addApp/" + `${this.props.user}` + "&" + `${app}` + "&" + `${validation}`, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }
        ).then((response) => {
            console.log(response)
        });
    }

    render() {
        return (
            <div>
                <form onSubmit={this.handleChoice.bind(this)}>
                    <select
                        value={this.state.selectedApp}
                        onChange={e =>
                            this.setState({
                                selectedApp: e.target.value,
                                validationError:
                                    e.target.value === ""
                                        ? "You must select your app"
                                        : ""
                            })
                        }
                    >
                        {this.state.apps.map(app => (
                            <option
                                key={app.name}
                                value={app.name}
                            >
                                {app.name}
                            </option>
                        ))}
                    </select>
                    <p>
                        <input type="submit" value="Confirm"/>
                    </p>
                </form>
                <div
                    style={{
                        color: "red",
                        marginTop: "5px"
                    }}
                >
                    {this.state.validationError}
                </div>
            </div>
        );
    }
}

export default ChooseApp;