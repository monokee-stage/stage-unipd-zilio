import React from "react";

const {Component} = require("react");

class ChooseApp extends Component {

    constructor(props) {
        console.log("costruttore ChooseApp")
        super(props);
        this.getApps()
    }

    state = {
        apps: [],
        selectedApp: "",
        needValidation: 0,
        validationError: "",
        userApps: [],
        valChoice: ""
    };

    getApps = () => {
        fetch("http://localhost:8081/api/appsName"
        ).then(response => {
            return response.json();
        }).then(data => {
            //this.state.apps = data
            for (let key in data) {
                if (Object.prototype.hasOwnProperty.call(data, key)) {
                    let val = data[key];
                    this.state.apps.push({name: val.name})
                }
            }
            this.forceUpdate()
        }).catch(error => {
            console.log(error);
        });
    }

    handleChoice(e) {
        e.preventDefault()
        this.checkApp()
        alert("Task executed.")
        window.location.reload()
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.checkIfNeedValidation()
    }

    /*
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

        addAppValidated = () => {
            fetch("http://localhost:8081/api/addAppValidated/" + `${this.props.user}` + "&" + `${this.state.selectedApp}` + "&" + "not_required", {
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
    */

    checkApp = () => {
        this.props.choice(this.state.selectedApp) //set the prop
        //check if the app needs the admin approval
        console.log(this.state.selectedApp + " " + this.state.needValidation)
        //if it does not need, i'll just send the app to the DB table
        if(this.state.needValidation === 0) {
            console.log("sono nell'if")
            this.sendAppToDB(this.state.selectedApp, 1).then(r => this.getChoice(this.props.user))
        }else{   //if it needs approval, i need the admin to approve this, so as long as the admin does not login, i can't do anything
            console.log("sono nell'else")
            this.sendAppToDB(this.state.selectedApp, 0).then(r => this.getChoice(this.props.user))
             //waiting for admin approval but i execute the process anyways
        }
        console.log("sono fuori dal controllo if-else")
        //this.forceUpdate()
        //this.getChoice(this.props.user)
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

    checkIfNeedValidation = () => {
        fetch("http://localhost:8081/api/getIfNeedValidation/" + `${this.state.selectedApp}`, {
            method: "GET",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            }
        }).then(response => {
            return response.json();
        }).then(data => {
            if(data === true) {
                this.state.needValidation = 1;
                //this.setState({needValidation: 1})
            }
        }).catch(error => {
            console.log(error);
        });
    }

    async sendAppToDB (app, validation){
        let response = await fetch("http://localhost:8081/api/addApp/" + `${this.props.user}` + "&" + `${app}` + "&" + `${validation}`, {
                method: "POST",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                }
            }
        ).then((response) => {
            console.log(response)
        });
        return 1;
    }

    changeSelectedOption = (e) => {
        this.setState({
            selectedApp: e.target.value
        })
    }

    render() {
        return (
            <div className="appChoice">
                <form onSubmit={this.handleChoice.bind(this)}>
                    <select
                        id="selectApp"
                        value={this.state.selectedApp}
                        onChange={this.changeSelectedOption.bind(this)}
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
                        <input type="submit" id="confirmValidationBtn" value="Confirm"/>
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